package me.lukas81298.flexmc.world;

import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.procedure.TObjectProcedure;
import lombok.Getter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.EntityObject;
import me.lukas81298.flexmc.entity.FlexEntity;
import me.lukas81298.flexmc.entity.FlexItem;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.message.play.server.*;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.block.FlexBlock;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;
import me.lukas81298.flexmc.world.chunk.ChunkSection;
import me.lukas81298.flexmc.world.generator.ChunkGenerator;
import me.lukas81298.flexmc.world.generator.OverWorldChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntUnaryOperator;

/**
 * @author lukas
 * @since 06.08.2017
 */
@SuppressWarnings( "deprecation" )
public class FlexWorld implements World {

    private static final int maxWorldTicks = 180000;

    @Getter
    private final String name;

    private final ReadWriteLock chunkLock = new ReentrantReadWriteLock();
    private final TByteObjectMap<TByteObjectMap<ChunkColumn>> columns = new TByteObjectHashMap<>();
    @Getter
    private final Set<FlexEntity> entitySet = ConcurrentHashMap.newKeySet();
    @Getter
    private final Set<FlexPlayer> playerSet = ConcurrentHashMap.newKeySet();

    private final AtomicInteger time = new AtomicInteger( 1000 );
    private final AtomicInteger worldAge = new AtomicInteger( 0 );
    private final AtomicInteger entityIdCounter = new AtomicInteger( 0 );

    private final ChunkGenerator generator = new OverWorldChunkGenerator();
    @Getter
    private final WorldBorder worldBorder = new FlexWorldBorder( this );

    @Getter
    private Difficulty difficulty = Difficulty.PEACEFUL;
    @Getter
    private final Dimension dimension = Dimension.OVER_WORLD;

    @Getter
    private volatile boolean generated = false;

    private byte timeCounter = 0;

    private final UUID uid = UUID.randomUUID();
    private final Map<String, String> gameRules = new ConcurrentHashMap<>();

    public FlexWorld( String name ) {
        this.name = name;
        Flex.getServer().getExecutorService().execute( new Runnable() {
            @Override
            public void run() {
                System.out.print( "Generating chunks for " + name + " [" );
                float count = 64 * 64;
                int k = 0;
                int f = (int) ( count / 30 );
                for ( int x = -32; x < 32; x++ ) {
                    for ( int z = -32; z < 32; z++ ) {
                        generateColumn( x, z );
                        if ( k % f == 0 ) {
                            System.out.print( "." );
                        }

                        k++;

                    }
                }
                System.out.println( "]" );
                //         System.out.println( "Server has started! Took " + Math.round( ( System.nanoTime() - start ) / 1000 ) / 1000 + "ms" );

                generated = true;
            }
        } );

        Flex.getServer().getExecutorService().execute( new Runnable() {
            @Override
            public void run() {
                while ( Flex.getServer().isRunning() ) {
                    long start = System.currentTimeMillis();
                    tickEntities();
                    long diff = System.currentTimeMillis() - start;
                    try {
                        Thread.sleep( Math.max( 0, 50 - diff ) );
                    } catch ( InterruptedException e ) {
                        e.printStackTrace();
                    }

                }
            }
        } );
        Flex.getServer().getExecutorService().execute( new Runnable() {
            @Override
            public void run() {
                while ( Flex.getServer().isRunning() ) {
                    long start = System.currentTimeMillis();
                    tick();
                    long diff = System.currentTimeMillis() - start;
                    try {
                        Thread.sleep( Math.max( 0, 50 - diff ) );
                    } catch ( InterruptedException e ) {
                        e.printStackTrace();
                    }

                }
            }
        } );
        Flex.getServer().getExecutorService().execute( new Runnable() {
            @Override
            public void run() {
                while ( Flex.getServer().isRunning() ) {
                    long start = System.currentTimeMillis();
                    tickPlayers();
                    long diff = System.currentTimeMillis() - start;
                    try {
                        Thread.sleep( Math.max( 0, 50 - diff ) );
                    } catch ( InterruptedException e ) {
                        e.printStackTrace();
                    }

                }
            }
        } );
    }

    private void tickPlayers() {
        for ( FlexPlayer player : this.playerSet ) {
            player.tick();
        }
    }

    private void tickEntities() {
        for ( FlexEntity entity : this.entitySet ) {
            entity.tick();
            if ( !entity.isAlive() && !( entity instanceof FlexPlayer ) ) { // repawning is handled differently for playerSet
                this.removeEntity( entity );
            }
        }
    }

    private void tick() {
        int time = this.time.updateAndGet( new IntUnaryOperator() {
            @Override
            public int applyAsInt( int operand ) {
                return operand < maxWorldTicks ? ( operand + 1 ) : 0;
            }
        } );
        int age = worldAge.incrementAndGet();
        if ( timeCounter == 20 ) {
            timeCounter = 0;
            for ( FlexPlayer player : playerSet ) {
                player.getConnectionHandler().sendMessage( new MessageS47TimeUpdate( age, time ) );
            }
        } else {
            timeCounter++;
        }
    }

    public FlexItem spawnItem( Location location, ItemStack itemStack ) {
        FlexItem item = new FlexItem( nextEntityId(), location, this );
        item.setItemStack( itemStack );
        if ( !EventFactory.call( new ItemSpawnEvent( item, location ) ).isCancelled() ) {
            this.addEntity( item, false );
        }
        return item;
    }

    public void addEntity( FlexEntity FlexEntity, boolean changeWorld ) {
        if ( changeWorld ) {
            FlexEntity.changeWorld( this, nextEntityId() );
        }
        if ( FlexEntity instanceof FlexPlayer ) {
            FlexPlayer player = (FlexPlayer) FlexEntity;
            for ( FlexPlayer t : playerSet ) {
                sendToPlayer( t, player.getConnectionHandler() );
                sendToPlayer( player, t.getConnectionHandler() );
            }
            synchronized ( this ) {
                this.entitySet.add( FlexEntity );
                this.playerSet.add( player );
            }
        } else {
            this.entitySet.add( FlexEntity );
            if ( FlexEntity instanceof EntityObject ) {
                byte t = ( (EntityObject) FlexEntity ).getObjectType();
                Location l = FlexEntity.getLocation();
                for ( FlexPlayer player : playerSet ) {
                    player.getConnectionHandler().sendMessage( new MessageS00SpawnObject( FlexEntity.getEntityId(), UUID.randomUUID(), t, l.getX(), l.getY(), l.getZ(), 3F, 3F ) );
                    player.getConnectionHandler().sendMessage( new MessageS3CEntityMetaData( FlexEntity.getEntityId(), FlexEntity.getMetaData() ) );
                }
            }
        }
    }

    private void sendToPlayer( FlexPlayer player, ConnectionHandler connectionHandler ) {
        Location playerLocation = player.getLocation();
        connectionHandler.sendMessage( new MessageS05SpawnPlayer( player.getEntityId(), player.getUuid(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(),
                playerLocation.getYaw(), playerLocation.getPitch(), player.getMetaData() ) );
    }

    public void removeEntity( FlexEntity FlexEntity ) {
        if ( FlexEntity instanceof FlexPlayer ) {
            synchronized ( this ) {
                this.entitySet.remove( FlexEntity );
                this.playerSet.remove( FlexEntity );
            }
        } else {
            this.entitySet.remove( FlexEntity );
        }
        for ( FlexPlayer player : this.playerSet ) {
            player.getConnectionHandler().sendMessage( new MessageS32DestroyEntities( FlexEntity.getEntityId() ) );
        }
    }

    private int nextEntityId() {
        return this.entityIdCounter.updateAndGet( new IntUnaryOperator() {
            @Override
            public int applyAsInt( int operand ) {
                return operand < Integer.MAX_VALUE ? ( operand + 1 ) : 0; // just in case :P
            }
        } );
    }

    public Location getSpawnLocation() {
        return new Location( this, 0, 70, 0 );
    }

    @Override
    public boolean setSpawnLocation( int i, int i1, int i2 ) {
        return false;
    }

    @Override
    public long getTime() {
        return time.get();
    }

    @Override
    public void setTime( long l ) {
        this.time.set( (int) l );
    }

    @Override
    public long getFullTime() {
        return this.worldAge.get();
    }

    @Override
    public void setFullTime( long l ) {
        this.worldAge.set( (int) l );
    }

    @Override
    public boolean hasStorm() {
        return false;
    }

    @Override
    public void setStorm( boolean b ) {

    }

    @Override
    public int getWeatherDuration() {
        return 0;
    }

    @Override
    public void setWeatherDuration( int i ) {

    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public void setThundering( boolean b ) {

    }

    @Override
    public int getThunderDuration() {
        return 0;
    }

    @Override
    public void setThunderDuration( int i ) {

    }

    @Override
    public boolean createExplosion( double v, double v1, double v2, float v3 ) {
        return false;
    }

    @Override
    public boolean createExplosion( double v, double v1, double v2, float v3, boolean b ) {
        return false;
    }

    @Override
    public boolean createExplosion( double v, double v1, double v2, float v3, boolean b, boolean b1 ) {
        return false;
    }

    @Override
    public boolean createExplosion( Location location, float v ) {
        return false;
    }

    @Override
    public boolean createExplosion( Location location, float v, boolean b ) {
        return false;
    }

    @Override
    public Environment getEnvironment() {
        return Environment.NORMAL;
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public boolean getPVP() {
        return false;
    }

    @Override
    public void setPVP( boolean b ) {

    }

    @Override
    public org.bukkit.generator.ChunkGenerator getGenerator() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return null;
    }

    @Override
    public <T extends Entity> T spawn( Location location, Class<T> aClass ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public <T extends Entity> T spawn( Location location, Class<T> aClass, Consumer<T> consumer ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock( Location location, MaterialData materialData ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock( Location location, Material material, byte b ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock( Location location, int i, byte b ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void playEffect( Location location, Effect effect, int i ) {

    }

    @Override
    public void playEffect( Location location, Effect effect, int i, int i1 ) {

    }

    @Override
    public <T> void playEffect( Location location, Effect effect, T t ) {

    }

    @Override
    public <T> void playEffect( Location location, Effect effect, T t, int i ) {

    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot( int i, int i1, boolean b, boolean b1 ) {
        return null;
    }

    @Override
    public void setSpawnFlags( boolean b, boolean b1 ) {

    }

    @Override
    public boolean getAllowAnimals() {
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        return false;
    }

    @Override
    public Biome getBiome( int i, int i1 ) {
        return getChunkAt( i, i1 ).getBiome( i & 0xF, i1 & 0xF );
    }

    @Override
    public void setBiome( int i, int i1, Biome biome ) {
        getChunkAt( i, i1 ).setBiome( i & 0xF, i1 & 0xF, (byte) biome.ordinal() );
    }

    @Override
    public double getTemperature( int i, int i1 ) {
        return 0;
    }

    @Override
    public double getHumidity( int i, int i1 ) {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return false;
    }

    @Override
    public void setKeepSpawnInMemory( boolean b ) {

    }

    @Override
    public boolean isAutoSave() {
        return false;
    }

    @Override
    public void setAutoSave( boolean b ) {

    }

    @Override
    public void setDifficulty( Difficulty difficulty ) {

    }

    @Override
    public File getWorldFolder() {
        return new File( Flex.getServer().getWorldManager().getConfig().getWorldContainer(), getName() );
    }

    @Override
    public WorldType getWorldType() {
        return WorldType.NORMAL;
    }

    @Override
    public boolean canGenerateStructures() {
        return false;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerAnimalSpawns( int i ) {

    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerMonsterSpawns( int i ) {

    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public void setMonsterSpawnLimit( int i ) {

    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setAnimalSpawnLimit( int i ) {

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit( int i ) {

    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setAmbientSpawnLimit( int i ) {

    }

    @Override
    public void playSound( Location location, Sound sound, float v, float v1 ) {

    }

    @Override
    public void playSound( Location location, String s, float v, float v1 ) {

    }

    @Override
    public void playSound( Location location, Sound sound, SoundCategory soundCategory, float v, float v1 ) {

    }

    @Override
    public void playSound( Location location, String s, SoundCategory soundCategory, float v, float v1 ) {

    }

    @Override
    public String[] getGameRules() {
        return gameRules.keySet().toArray( new String[ gameRules.keySet().size() ] );
    }

    @Override
    public String getGameRuleValue( String s ) {
        return this.gameRules.get( s );
    }

    @Override
    public boolean setGameRuleValue( String s, String s1 ) {
        this.gameRules.put( s, s1 );
        return true;
    }

    @Override
    public boolean isGameRule( String s ) {
        return  "true".equals( this.getGameRuleValue( s ) );
    }

    @Override
    public void spawnParticle( Particle particle, Location location, int i ) {

    }

    @Override
    public void spawnParticle( Particle particle, double v, double v1, double v2, int i ) {

    }

    @Override
    public <T> void spawnParticle( Particle particle, Location location, int i, T t ) {

    }

    @Override
    public <T> void spawnParticle( Particle particle, double v, double v1, double v2, int i, T t ) {

    }

    @Override
    public void spawnParticle( Particle particle, Location location, int i, double v, double v1, double v2 ) {

    }

    @Override
    public void spawnParticle( Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5 ) {

    }

    @Override
    public <T> void spawnParticle( Particle particle, Location location, int i, double v, double v1, double v2, T t ) {

    }

    @Override
    public <T> void spawnParticle( Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t ) {

    }

    @Override
    public void spawnParticle( Particle particle, Location location, int i, double v, double v1, double v2, double v3 ) {

    }

    @Override
    public void spawnParticle( Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6 ) {

    }

    @Override
    public <T> void spawnParticle( Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t ) {

    }

    @Override
    public <T> void spawnParticle( Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t ) {

    }

    @Override
    public FlexBlock getBlockAt( int i, int i1, int i2 ) {
        return this.getBlock0( i, i1, i2, false );
    }

    public FlexBlock getBlock0( int i, int i1, int i2, boolean virtual ) {
        Vector3i position = new Vector3i( i, i1, i2 );
        BlockState state = this.getBlockAt( position );
        return new FlexBlock( this, position, state, virtual );
    }

    @Override
    public FlexBlock getBlockAt( Location location ) {
        return getBlockAt( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
    }

    @Override
    public int getBlockTypeIdAt( int i, int i1, int i2 ) {
        return getBlockAt( i, i1, i2 ).getTypeId();
    }

    @Override
    public int getBlockTypeIdAt( Location location ) {
        return getBlockAt( location ).getTypeId();
    }

    @Override
    public int getHighestBlockYAt( int i, int i1 ) {
        ChunkColumn column = this.getChunkAt( i, i1 );
        return column.getHighestYAt( i & 0xF, i1 & 0xF );
    }

    @Override
    public int getHighestBlockYAt( Location location ) {
        return getHighestBlockYAt( location.getBlockX(), location.getBlockZ() );
    }

    @Override
    public Block getHighestBlockAt( int i, int i1 ) {
        return getBlockAt( i, getHighestBlockYAt( i, i1 ), i1 );
    }

    @Override
    public Block getHighestBlockAt( Location location ) {
        return getHighestBlockAt( location.getBlockX(), location.getBlockZ() );
    }

    public ChunkColumn getChunkAt( int x, int z ) {
        this.chunkLock.readLock().lock();
        try {
            int i = x / 16;
            if ( x < 0 ) {
                i--;
            }
            int j = z / 16;
            if ( z < 0 ) {
                j--;
            }
            TByteObjectMap<ChunkColumn> map = this.columns.get( (byte) i );
            if ( map == null ) {
                return null;
            }
            return map.get( (byte) j );
        } finally {
            this.chunkLock.readLock().unlock();
        }
    }

    @Override
    public Chunk getChunkAt( Location location ) {
        return this.getChunkAt( location.getBlockX(), location.getBlockZ() );
    }

    @Override
    public Chunk getChunkAt( Block block ) {
        return this.getChunkAt( block.getX(), block.getZ() );
    }

    @Override
    public boolean isChunkLoaded( Chunk chunk ) {
        return true;
    }

    @Override
    public Chunk[] getLoadedChunks() {
        List<ChunkColumn> columns = this.getColumns();
        return columns.toArray( new Chunk[ columns.size() ] );
    }

    @Override
    public void loadChunk( Chunk chunk ) {

    }

    @Override
    public boolean isChunkLoaded( int i, int i1 ) {
        return false;
    }

    @Override
    public boolean isChunkInUse( int i, int i1 ) {
        return false;
    }

    @Override
    public void loadChunk( int i, int i1 ) {

    }

    @Override
    public boolean loadChunk( int i, int i1, boolean b ) {
        return false;
    }

    @Override
    public boolean unloadChunk( Chunk chunk ) {
        return false;
    }

    @Override
    public boolean unloadChunk( int i, int i1 ) {
        return this.getChunkAt( i, i1 ).unload();
    }

    @Override
    public boolean unloadChunk( int i, int i1, boolean b ) {
        return this.getChunkAt( i, i1 ).unload( b );
    }

    @Override
    public boolean unloadChunk( int i, int i1, boolean b, boolean b1 ) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest( int i, int i1 ) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest( int i, int i1, boolean b ) {
        return false;
    }

    @Override
    public boolean regenerateChunk( int i, int i1 ) {
        return false;
    }

    @Override
    public boolean refreshChunk( int i, int i1 ) {
        return false;
    }

    @Override
    public org.bukkit.entity.Item dropItem( Location location, org.bukkit.inventory.ItemStack itemStack ) {
        return spawnItem( location, itemStack );
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally( Location location, org.bukkit.inventory.ItemStack itemStack ) {
        return spawnItem( location, itemStack );
    }

    @Override
    public Arrow spawnArrow( Location location, Vector vector, float v, float v1 ) {
        return null;
    }

    @Override
    public <T extends Arrow> T spawnArrow( Location location, Vector vector, float v, float v1, Class<T> aClass ) {
        return null;
    }

    @Override
    public boolean generateTree( Location location, TreeType treeType ) {
        return false;
    }

    @Override
    public boolean generateTree( Location location, TreeType treeType, BlockChangeDelegate blockChangeDelegate ) {
        return false;
    }

    @Override
    public Entity spawnEntity( Location location, EntityType entityType ) {
        return null;
    }

    @Override
    public LightningStrike strikeLightning( Location location ) {
        return null;
    }

    @Override
    public LightningStrike strikeLightningEffect( Location location ) {
        return null;
    }

    @Override
    public List<Entity> getEntities() {
        return new ArrayList<>( this.entitySet );
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> livingEntities = new ArrayList<>();
        for ( FlexEntity flexEntity : entitySet ) {
            if ( flexEntity instanceof LivingEntity ) {
                livingEntities.add( (LivingEntity) flexEntity );
            }
        }
        return livingEntities;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass( Class<T>... classes ) {
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass( Class<T> aClass ) {
        List<T> filteredEnties = new ArrayList<>();
        for ( FlexEntity flexEntity : entitySet ) {
            if ( aClass.isAssignableFrom( flexEntity.getClass() ) ) {
                filteredEnties.add( (T) flexEntity );
            }
        }
        return filteredEnties;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses( Class<?>... classes ) {
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>( playerSet );
    }

    @Override
    public Collection<Entity> getNearbyEntities( Location location, double v, double v1, double v2 ) {
        List<Entity> nearbyEntities = new ArrayList<>();
        for ( FlexEntity flexEntity : entitySet ) {
            if ( Math.abs( flexEntity.getLocation().getX() - location.getX() ) < v
                    && Math.abs( flexEntity.getLocation().getY() - location.getY() ) < v1
                    && Math.abs( flexEntity.getLocation().getZ() - location.getZ() ) < v2 ) {
                nearbyEntities.add( flexEntity );
            }
        }
        return nearbyEntities;
    }

    @Override
    public UUID getUID() {
        return uid;
    }

    private void generateColumn( int x, int z ) {
        this.chunkLock.writeLock().lock();
        try {
            ChunkColumn chunkColumnColumn = new ChunkColumn( x, z, this );
            for ( int i = 0; i < chunkColumnColumn.getSections().length; i++ ) {
                ChunkSection section = new ChunkSection( chunkColumnColumn );
                chunkColumnColumn.getSections()[i] = section;
            }
            this.generator.generate( chunkColumnColumn );
            TByteObjectMap<ChunkColumn> c = this.columns.get( (byte) x );
            if ( c == null ) {
                this.columns.put( (byte) x, c = new TByteObjectHashMap<>() );
            }
            c.put( (byte) z, chunkColumnColumn );
        } finally {
            this.chunkLock.writeLock().unlock();
        }
    }

    public List<ChunkColumn> getColumns() {
        List<ChunkColumn> list = new ArrayList<>();
        this.chunkLock.readLock().lock();
        try {
            this.columns.forEachValue( new TObjectProcedure<TByteObjectMap<ChunkColumn>>() {
                @Override
                public boolean execute( TByteObjectMap<ChunkColumn> value ) {
                    list.addAll( value.valueCollection() );
                    return true;
                }
            } );
        } finally {
            this.chunkLock.readLock().unlock();
        }
        return list;
    }

    public BlockState getBlockAt( Vector3i position ) {
        ChunkColumn column = this.getChunkAt( position.getX(), position.getZ() );
        int sectionIndex = position.getY() / 16;
        ChunkSection section = column.getSections()[sectionIndex];
        int i = section.getBlock( position.getX() & 0xF, position.getY() & 0xF, position.getZ() & 0xF );
        int type = i >> 4;
        int data = i & 15;
        return new BlockState( type, data );
    }

    public void setBlock( Vector3i position, BlockState state ) {
        ChunkColumn column = this.getChunkAt( position.getX(), position.getZ() );
        int sectionIndex = position.getY() / 16;
        ChunkSection section = column.getSections()[sectionIndex];
        section.setBlock( position.getX() & 0xF, position.getY() & 0xF, position.getZ() & 0xF, state.getTypeId(), state.getData() );
        for ( FlexPlayer player : this.playerSet ) {
            player.getConnectionHandler().sendMessage( new MessageS0BBlockChange( position, state ) );
        }
    }

    @Override
    public void setMetadata( String s, MetadataValue metadataValue ) {

    }

    @Override
    public List<MetadataValue> getMetadata( String s ) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasMetadata( String s ) {
        return false;
    }

    @Override
    public void removeMetadata( String s, Plugin plugin ) {

    }

    @Override
    public void sendPluginMessage( Plugin plugin, String s, byte[] bytes ) {

        for ( FlexPlayer flexPlayer : getPlayerSet() ) {
            flexPlayer.sendPluginMessage( plugin, s, bytes );
        }

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return Collections.emptySet();
    }
}
