package me.lukas81298.flexmc.world;

import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.procedure.TObjectProcedure;
import lombok.Getter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.Entity;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.play.client.MessageS47TimeUpdate;
import me.lukas81298.flexmc.io.message.play.server.MessageS05SpawnPlayer;
import me.lukas81298.flexmc.io.message.play.server.MessageS0BBlockChange;
import me.lukas81298.flexmc.io.message.play.server.MessageS32DestroyEntities;
import me.lukas81298.flexmc.io.message.play.server.MessageS3CEntityMetaData;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.generator.ChunkGenerator;
import me.lukas81298.flexmc.world.generator.FlatGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntUnaryOperator;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class World {

    private static final int maxWorldTicks = 180000;

    @Getter
    private final String name;

    private final ReadWriteLock chunkLock = new ReentrantReadWriteLock();
    private final TByteObjectMap<TByteObjectMap<ChunkColumn>> columns = new TByteObjectHashMap<>();

    private final Set<Entity> entities = ConcurrentHashMap.newKeySet();
    @Getter
    private final Set<Player> players = ConcurrentHashMap.newKeySet();

    private final AtomicInteger time = new AtomicInteger( 1000 );
    private final AtomicInteger worldAge = new AtomicInteger( 0 );
    private final AtomicInteger entityIdCounter = new AtomicInteger( 0 );

    private final ChunkGenerator generator = new FlatGenerator( 3, 1, new BlockState( 3, 0 ), new BlockState( 2, 0 ) );

    private byte timeCounter = 0;

    public World( String name ) {
        this.name = name;
        for ( int x = -7; x < 7; x++ ) {
            for ( int z = -7; z < 7; z++ ) {
                this.generateColumn( x, z );
            }
        }
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
    }

    public void tick() {
        int time = this.time.updateAndGet( new IntUnaryOperator() {
            @Override
            public int applyAsInt( int operand ) {
                return operand < maxWorldTicks ? ( operand + 1 ) : 0;
            }
        } );
        int age = worldAge.incrementAndGet();
        if( timeCounter == 20 ) {
            timeCounter = 0;
            for ( Player player : players ) {
                player.getConnectionHandler().sendMessage( new MessageS47TimeUpdate( age, time ) );
            }
        } else {
            timeCounter++;
        }
    }

    public void addEntity( Entity entity ) {
        entity.changeWorld( this, nextEntityId() );
        if ( entity instanceof Player ) {
            Player player = (Player) entity;
            for ( Player t : players ) {
                sendToPlayer( t, player.getConnectionHandler() );
                sendToPlayer( player, t.getConnectionHandler() );
            }
            synchronized ( this ) {
                this.entities.add( entity );
                this.players.add( player );
            }
        } else {
            this.entities.add( entity );
        }
    }

    private void sendToPlayer( Player player, ConnectionHandler connectionHandler ) {
        Location playerLocation = player.getLocation();
        connectionHandler.sendMessage( new MessageS05SpawnPlayer( player.getEntityId(), player.getUuid(), playerLocation.x(), playerLocation.y(), playerLocation.z(),
                playerLocation.yaw(), playerLocation.pitch(), player.getMetaData() ) );
    }

    public void removeEntity( Entity entity ) {
        if ( entity instanceof Player ) {
            synchronized ( this ) {
                this.entities.remove( entity );
                this.players.remove( entity );
            }
        } else {
            this.entities.remove( entity );
        }
        for( Player player : this.players ) {
            player.getConnectionHandler().sendMessage( new MessageS32DestroyEntities( entity.getEntityId() ) );
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

    public ChunkColumn getChunkAt( int x, int z ) {
        this.chunkLock.readLock().lock();
        try {
            int i = x / 16;
            int j = z / 16;
            TByteObjectMap<ChunkColumn> map = this.columns.get( (byte) i );
            if ( map == null ) {
                return null;
            }
            return map.get( (byte) j );
        } finally {
            this.chunkLock.readLock().unlock();
        }
    }

    private void generateColumn( int x, int z ) {
        this.chunkLock.writeLock().lock();
        try {
            System.out.println( "Generating chunk column " + x + ", " );
            ChunkColumn chunkColumnColumn = new ChunkColumn( x, z );
            for ( int i = 0; i < chunkColumnColumn.getSections().length; i++ ) {
                ChunkSection section = new ChunkSection();
                chunkColumnColumn.getSections()[i] = section;
            }
            this.generator.generate( chunkColumnColumn.getSections() );
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

    public void setBlock( Vector3i position, BlockState state ) {
        ChunkColumn column = this.getChunkAt( position.getX(), position.getZ() );
        int sectionIndex = position.getY() / 16;
        ChunkSection section = column.getSections()[sectionIndex];
        section.setBlock( fixIndex( position.getX() % 16 ), position.getY() % 16, fixIndex( position.getZ() % 16 ), state.getId(), state.getData() );
        for( Player player : this.players ) {
            player.getConnectionHandler().sendMessage( new MessageS0BBlockChange( position, state ) );
        }
    }

    private int fixIndex( int z ) {
        if( z >= 0 ) {
            return z;
        }
        return 16 + z;
    }
}
