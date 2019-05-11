package me.lukas81298.flexmc.entity;

import io.netty.buffer.Unpooled;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.entity.metadata.EntityFlag;
import me.lukas81298.flexmc.inventory.FlexInventory;
import me.lukas81298.flexmc.inventory.FlexInventoryView;
import me.lukas81298.flexmc.inventory.FlexPlayerInventory;
import me.lukas81298.flexmc.inventory.FlexWorkbenchInventory;
import me.lukas81298.flexmc.io.message.play.client.MessageC02ChatMessage;
import me.lukas81298.flexmc.io.message.play.client.MessageC04ClientSettings;
import me.lukas81298.flexmc.io.message.play.client.MessageC08CloseWindow;
import me.lukas81298.flexmc.io.message.play.server.*;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import me.lukas81298.flexmc.util.IdProvider;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.Dimension;
import me.lukas81298.flexmc.world.FlexWorld;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author lukas
 * @since 05.08.2017
 */
@EqualsAndHashCode( of = { "name", "uuid" }, callSuper = false )
public class FlexPlayer extends FlexLivingEntity implements Player {

    @Getter
    private final String name;
    @Getter
    private final UUID uuid;

    @Getter
    private final ConnectionHandler connectionHandler;
    @Setter
    @Getter
    private volatile long lastKeepAlive = System.currentTimeMillis();
    @Getter
    private volatile GameMode gameMode = GameMode.SURVIVAL;
    private final AtomicBoolean online = new AtomicBoolean( true );
    @Getter
    private final FlexPlayerInventory inventory;
    @Getter
    @Setter
    private volatile int heldItemSlot = 0;
    private AtomicInteger foodLevel = new AtomicInteger( 20 );
    private final Set<ChunkColumn> shownChunks = ConcurrentHashMap.newKeySet();

    @Setter
    private int viewDistance = 8;

    @Setter
    private String displayName;
    @Setter
    private String playerListName;
    @Getter
    private Location spawnLocation;
    @Setter
    private MessageC04ClientSettings settings;
    @Getter
    private final IdProvider windowIdProvider = new IdProvider( (byte) 127 );

    private final Set<String> pluginMessageChannels = ConcurrentHashMap.newKeySet();

    private byte healthCounter = 0;

    private final Map<Statistic, AtomicInteger> statistics = Collections.synchronizedMap( new EnumMap<>( Statistic.class ) );
    private final Set<Achievement> achievements = ConcurrentHashMap.newKeySet();
    private final Set<UUID> hiddenPlayers = ConcurrentHashMap.newKeySet();

    @Getter
    private FlexInventoryView openInventory;

    public FlexPlayer( int entityId, Location position, String name, UUID uuid, ConnectionHandler connectionHandler, FlexWorld world ) {
        super( entityId, position, world );
        this.spawnLocation = new Location( world, 0, 100, 0 );
        this.name = name;
        this.uuid = uuid;
        this.connectionHandler = connectionHandler;
        this.inventory = new FlexPlayerInventory( this );
        this.openInventory = new FlexInventoryView( this );
    }

    public void spawnPlayer() {
        connectionHandler.sendMessage( new MessageS23JoinGame( this.getEntityId(), gameMode, Dimension.OVER_WORLD, Difficulty.PEACEFUL, "default", false ) );
        connectionHandler.sendMessage( new MessageS0DServerDifficulty( Difficulty.PEACEFUL ) );
        connectionHandler.sendMessage( new MessageS46SpawnPosition( new Vector3i( 0, 10, 0 ) ) );
        connectionHandler.sendMessage( new MessageS2CPlayerAbilities( (byte) 0, .2F, .2F ) );
        connectionHandler.sendMessage( new MessageS2FPlayerPositionAndLook( getLocation().getX(), getLocation().getY(), getLocation().getZ(), 0F, 0F, (byte) 0, 0 ) );
        this.refreshShownChunks( true );
        inventory.addItem( new ItemStack( 278, 1 ) );
        inventory.addItem( new ItemStack( 279, 1 ) );
        inventory.addItem( new ItemStack( 277, 1 ) );
        inventory.addItem( new ItemStack( Material.WORKBENCH, 1 ) );
        inventory.addItem( new ItemStack( Material.LOG, 16 ) );
        inventory.addItem( new ItemStack( Material.FURNACE ) );
    }

    public void refreshShownChunks( boolean first ) {
        Location location = this.getLocation();
        int x = location.getBlockX() / 16, z = location.getBlockZ() / 16;
        List<ChunkColumn> columns = this.getWorld().getColumns();
        if( first ) { // send the chunks close to the player first so they do not glitch in void
            columns.sort( new Comparator<ChunkColumn>() {
                @Override
                public int compare( ChunkColumn o1, ChunkColumn o2 ) {
                    return Double.compare( location.distanceSquared( new Location( getWorld(), o1.getX(), 0, o1.getZ() ) ), location.distanceSquared( new Location( getWorld(), o2.getX(), 0, o2.getZ() ) ) );
                }
            } );
        }
        for ( ChunkColumn column : columns ) {
            if ( Math.abs( x - column.getX() ) <= viewDistance && Math.abs( z - column.getZ() ) <= viewDistance ) {
                if ( shownChunks.add( column ) ) {
                    this.connectionHandler.sendMessage( new MessageS20ChunkData( column ) );
                }
            } else {
                if ( shownChunks.remove( column ) ) {
                    this.connectionHandler.sendMessage( new MessageS1DUnloadChunk( column.getX(), column.getZ() ) );
                }
            }
        }
    }

    @Override
    public String getDisplayName() {
        String displayName = this.displayName;
        return displayName == null ? this.name : displayName;
    }

    @Override
    public String getPlayerListName() {
        String playerListName = this.playerListName;
        return playerListName == null ? this.name : playerListName;
    }

    @Override
    public void setCompassTarget( Location location ) {
        this.spawnLocation = location;
        connectionHandler.sendMessage( new MessageS46SpawnPosition( new Vector3i( location.getBlockX(), location.getBlockY(), location.getBlockZ() ) ) );
    }

    @Override
    public Location getCompassTarget() {
        return this.spawnLocation;
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.connectionHandler.getSocketAddress();
    }

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput( String s ) {

    }

    @Override
    public boolean beginConversation( Conversation conversation ) {
        return false;
    }

    @Override
    public void abandonConversation( Conversation conversation ) {

    }

    @Override
    public void abandonConversation( Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent ) {

    }


    @Override
    public void sendRawMessage( String s ) {
        this.connectionHandler.sendMessage( new MessageS0FChatMessage( s, (byte) 0 ) );
    }


    @Override
    public void sendMessage( String message  ) {
        this.sendMessage( new String[] { message } );
    }

    public void kickPlayer( String reason ) {
        if ( this.online.compareAndSet( true, false ) ) {
            this.connectionHandler.sendMessage( new MessageS1ADisconnect( new TextComponent( reason ) ) );
        }
    }

    @Override
    public void chat( String s ) {
        connectionHandler.getConnectionManager().getListenerManager().invokeListeners( connectionHandler, new MessageC02ChatMessage( s ) );
    }

    @Override
    public boolean performCommand( String s ) {
        return false;
    }

    public int getLatency() {
        return 0b00101010; // 42 ;D
    }

    @Override
    public void sendMessage( String... messages ) {
        for ( String message : messages ) {
            this.sendMessage( TextComponent.fromLegacyText( message ) );
        }
    }

    public void sendMessage( BaseComponent... components ) {
        this.connectionHandler.sendMessage( new MessageS0FChatMessage( components, (byte) 0 ) );
    }

    @Override
    public boolean hasPermission( String permission ) {
        return true; // todo change
    }

    public void setGameMode( GameMode gameMode ) {
        synchronized ( this ) {
            if ( this.gameMode != gameMode ) {
                this.gameMode = gameMode;
                connectionHandler.sendMessage( new MessageS1EChangeGameState( (byte) 3, gameMode.getValue() ) );
            }
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isHandRaised() {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return 0;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        return null;
    }

    @Override
    public void setShoulderEntityLeft( Entity entity ) {

    }

    @Override
    public Entity getShoulderEntityRight() {
        return null;
    }

    @Override
    public void setShoulderEntityRight( Entity entity ) {

    }

    public String getIpAddress() {
        return connectionHandler.getIpAddress();
    }

    public void setSneaking( boolean flag ) {
        this.setFlag( EntityFlag.CROUCHED, flag );
    }

    public void setSprinting( boolean flag ) {
        this.setFlag( EntityFlag.SPRINTING, false );
    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void setSleepingIgnored( boolean b ) {

    }

    @Override
    public boolean isSleepingIgnored() {
        return false;
    }

    @Override
    public void playNote( Location location, byte b, byte b1 ) {

    }

    @Override
    public void playNote( Location location, Instrument instrument, Note note ) {

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
    public void stopSound( Sound sound ) {

    }

    @Override
    public void stopSound( String s ) {

    }

    @Override
    public void stopSound( Sound sound, SoundCategory soundCategory ) {

    }

    @Override
    public void stopSound( String s, SoundCategory soundCategory ) {

    }

    @Override
    public void playEffect( Location location, Effect effect, int i ) {

    }

    @Override
    public <T> void playEffect( Location location, Effect effect, T t ) {

    }

    @Override
    public void sendBlockChange( Location location, Material material, byte b ) {
        sendBlockChange( location, material.getId(), b );
    }

    @Override
    public boolean sendChunkChange( Location location, int i, int i1, int i2, byte[] bytes ) {
        return false;
    }

    @Override
    public void sendBlockChange( Location location, int i, byte b ) {
        getConnectionHandler().sendMessage( new MessageS0BBlockChange( new Vector3i( location ), new BlockState( i, b ) ) );

    }

    @Override
    public void sendSignChange( Location location, String[] strings ) throws IllegalArgumentException {

    }

    @Override
    public void sendMap( MapView mapView ) {

    }

    @Override
    public void updateInventory() {
        getInventory().setContents( getInventory().getContents() );
    }

    @Override
    public void awardAchievement( Achievement achievement ) {
        this.achievements.add( achievement );
    }

    @Override
    public void removeAchievement( Achievement achievement ) {
        this.achievements.remove( achievement );
    }

    @Override
    public boolean hasAchievement( Achievement achievement ) {
        return this.achievements.contains( achievement );
    }

    @Override
    public void incrementStatistic( Statistic statistic ) throws IllegalArgumentException {
        incrementStatistic( statistic, 1 );
    }

    @Override
    public void decrementStatistic( Statistic statistic ) throws IllegalArgumentException {
        incrementStatistic( statistic, -1 );
    }

    @Override
    public void incrementStatistic( Statistic statistic, int i ) throws IllegalArgumentException {
        this.statistics.computeIfAbsent( statistic, new Function<Statistic, AtomicInteger>() {
            @Override
            public AtomicInteger apply( Statistic statistic ) {
                return new AtomicInteger();
            }
        } ).addAndGet( i );
    }

    @Override
    public void decrementStatistic( Statistic statistic, int i ) throws IllegalArgumentException {
        incrementStatistic( statistic, -i );
    }

    @Override
    public void setStatistic( Statistic statistic, int i ) throws IllegalArgumentException {
        this.statistics.computeIfAbsent( statistic, new Function<Statistic, AtomicInteger>() {
            @Override
            public AtomicInteger apply( Statistic statistic ) {
                return new AtomicInteger();
            }
        } ).set( i );
    }

    @Override
    public int getStatistic( Statistic statistic ) throws IllegalArgumentException {
        AtomicInteger i = this.statistics.get( statistic );
        return i == null ? 0 : i.get();
    }

    @Override
    public void incrementStatistic( Statistic statistic, Material material ) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic( Statistic statistic, Material material ) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic( Statistic statistic, Material material ) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic( Statistic statistic, Material material, int i ) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic( Statistic statistic, Material material, int i ) throws IllegalArgumentException {

    }

    @Override
    public void setStatistic( Statistic statistic, Material material, int i ) throws IllegalArgumentException {

    }

    @Override
    public void incrementStatistic( Statistic statistic, EntityType entityType ) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic( Statistic statistic, EntityType entityType ) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic( Statistic statistic, EntityType entityType ) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic( Statistic statistic, EntityType entityType, int i ) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic( Statistic statistic, EntityType entityType, int i ) {

    }

    @Override
    public void setStatistic( Statistic statistic, EntityType entityType, int i ) {

    }

    @Override
    public void setPlayerTime( long l, boolean b ) {

    }

    @Override
    public long getPlayerTime() {
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return false;
    }

    @Override
    public void resetPlayerTime() {

    }

    @Override
    public void setPlayerWeather( WeatherType weatherType ) {

    }

    @Override
    public WeatherType getPlayerWeather() {
        return null;
    }

    @Override
    public void resetPlayerWeather() {

    }

    @Override
    public void giveExp( int i ) {

    }

    @Override
    public void giveExpLevels( int i ) {

    }

    @Override
    public float getExp() {
        return 0;
    }

    @Override
    public void setExp( float v ) {

    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel( int i ) {

    }

    @Override
    public int getTotalExperience() {
        return 0;
    }

    @Override
    public void setTotalExperience( int i ) {

    }

    @Override
    public float getExhaustion() {
        return 0;
    }

    @Override
    public void setExhaustion( float v ) {

    }

    @Override
    public float getSaturation() {
        return 0;
    }

    @Override
    public void setSaturation( float v ) {

    }

    @Override
    public int getFoodLevel() {
        return foodLevel.get();
    }

    @Override
    public void setFoodLevel( int i ) {
        this.foodLevel.set( i );
    }

    @Override
    public boolean isOnline() {
        return online.get();
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public void setWhitelisted( boolean b ) {

    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public void setBedSpawnLocation( Location location ) {

    }

    @Override
    public void setBedSpawnLocation( Location location, boolean b ) {

    }

    @Override
    public boolean getAllowFlight() {
        return false;
    }

    @Override
    public void setAllowFlight( boolean b ) {

    }

    @Override
    public void hidePlayer( Player target ) {
        if( this.hiddenPlayers.add( target.getUniqueId() ) ) {
            this.connectionHandler.sendMessage( new MessageS32DestroyEntities( target.getEntityId() ) );
            this.connectionHandler.sendMessage( new MessageS2EPlayerList( MessageS2EPlayerList.Action.REMOVE_PLAYER, Collections.singletonList(
                    new MessageS2EPlayerList.PlayerItem().setUuid( target.getUniqueId() ) )
            ) );
        }
    }

    @Override
    public void showPlayer( Player player ) {
        FlexPlayer target = (FlexPlayer) player;
        if( this.hiddenPlayers.contains( target.getUniqueId() ) ) {
            this.connectionHandler.sendMessage( new MessageS2EPlayerList( MessageS2EPlayerList.Action.ADD_PLAYER,
                    Collections.singletonList( new MessageS2EPlayerList.PlayerItem().setUuid( target.getUniqueId() )
                            .setName( target.getName() ).setGameMode( target.getGameMode() ).setPing( 0 ) ) ) );
            this.connectionHandler.sendMessage( new MessageS05SpawnPlayer( target.getEntityId(), target.getUniqueId(), target.getLocation(), target.metaData ) );
        }
    }

    @Override
    public boolean canSee( Player player ) {
        return this.hiddenPlayers.contains( player.getUniqueId() );
    }

    @Override
    public boolean isFlying() {
        return false;
    }

    @Override
    public void setFlying( boolean b ) {

    }

    @Override
    public void setFlySpeed( float v ) throws IllegalArgumentException {

    }

    @Override
    public void setWalkSpeed( float v ) throws IllegalArgumentException {

    }

    @Override
    public float getFlySpeed() {
        return 0;
    }

    @Override
    public float getWalkSpeed() {
        return 0;
    }

    @Override
    public void setTexturePack( String s ) {

    }

    @Override
    public void setResourcePack( String s ) {

    }

    @Override
    public void setResourcePack( String s, byte[] bytes ) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public void setScoreboard( Scoreboard scoreboard ) throws IllegalArgumentException, IllegalStateException {

    }

    @Override
    public boolean isHealthScaled() {
        return false;
    }

    @Override
    public void setHealthScaled( boolean b ) {

    }

    @Override
    public void setHealthScale( double v ) throws IllegalArgumentException {

    }

    @Override
    public double getHealthScale() {
        return 0;
    }

    @Override
    public Entity getSpectatorTarget() {
        return null;
    }

    @Override
    public void setSpectatorTarget( Entity entity ) {

    }

    @Override
    public void sendTitle( String s, String s1 ) {

    }

    @Override
    public void sendTitle( String s, String s1, int i, int i1, int i2 ) {

    }

    @Override
    public void resetTitle() {

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
    public AdvancementProgress getAdvancementProgress( Advancement advancement ) {
        return null;
    }

    @Override
    public String getLocale() {
        return settings == null ? "en_US" : settings.getLocale();
    }

    public boolean isSneaking() {
        return this.getFlag( EntityFlag.CROUCHED );
    }

    public boolean isSprinting() {
        return this.getFlag( EntityFlag.SPRINTING );
    }

    public void handleSetHeldItemSlot( int slot ) {
        this.heldItemSlot = slot; // todo update equipment to other players
    }

    @Override
    public Inventory getEnderChest() {
        return null;
    }

    @Override
    public MainHand getMainHand() {
        return MainHand.RIGHT;
    }

    @Override
    public boolean setWindowProperty( InventoryView.Property property, int i ) {
        return false;
    }

    public int getNextWindowId() {
        return this.windowIdProvider.getNextId();
    }

    @Override
    public InventoryView openInventory( Inventory inventory ) {
        if( !( inventory instanceof FlexInventory ) ) {
            throw new IllegalArgumentException( "Inventory does not extend FlexInventory" );
        }
        FlexInventory flexInventory = (FlexInventory) inventory;
        this.openInventory.setTopInventory( flexInventory );
        byte size = (byte) flexInventory.getSize();
        switch ( inventory.getType() ) {
            case CRAFTING:
            case FURNACE:
                size = 0;
        }
        this.connectionHandler.sendMessage( new MessageS13OpenWindow( flexInventory.getWindowId(), flexInventory.getRawType(), new TextComponent( flexInventory.getTitle() ),
                size, -1 ) );
        return this.openInventory;
    }

    @Override
    public InventoryView openWorkbench( Location location, boolean b ) {
        return openInventory( new FlexWorkbenchInventory( this.windowIdProvider.getNextId(), this ) );
    }

    @Override
    public InventoryView openEnchanting( Location location, boolean b ) {
        return null;
    }

    @Override
    public void openInventory( InventoryView inventoryView ) {
        this.openInventory( inventoryView.getTopInventory() );
    }

    @Override
    public InventoryView openMerchant( Villager villager, boolean b ) {
        return null;
    }

    @Override
    public InventoryView openMerchant( Merchant merchant, boolean b ) {
        return null;
    }

    @Override
    public void closeInventory() {
        this.connectionHandler.sendMessage( new MessageC08CloseWindow() );
        FlexInventory i = this.openInventory.getTopInventory();
        if( i != null ) {
            i.onClose();
        }
    }

    public ItemStack getItemInHand() {
        return this.inventory.getItem( heldItemSlot );
    }

    @Override
    public void setItemInHand( ItemStack itemStack ) {
        this.getInventory().setItem( this.heldItemSlot, itemStack );
    }

    @Override
    public ItemStack getItemOnCursor() {
        return this.inventory.getItemOnCursor();
    }

    @Override
    public void setItemOnCursor( ItemStack itemStack ) {
        this.inventory.setItemOnCursor( itemStack );
    }

    @Override
    public boolean hasCooldown( Material material ) {
        return false;
    }

    @Override
    public int getCooldown( Material material ) {
        return 0;
    }

    @Override
    public void setCooldown( Material material, int i ) {

    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public int getSleepTicks() {
        return 0;
    }

    @Override
    protected void updateHealth( double health ) {
        super.updateHealth( health );
        if ( health >= 0 ) {
            connectionHandler.sendMessage( new MessageS41UpdateHealth( (float) health, foodLevel.get(), 0F ) );
        }
    }

    @Override
    public void teleport( Location l, boolean onGround ) {
        Location k = this.getLocation();
        if ( ( l.getBlockX() / 16 != k.getBlockX() / 16 ) || (  l.getBlockZ() / 16 != k.getBlockZ() / 16 ) ) {
            this.refreshShownChunks( false );
        }
        super.teleport( l, onGround );
        EventFactory.call( new PlayerMoveEvent( this, k, l ) );

    }

    public synchronized void respawn() {
        alive = true;
        foodLevel.set( 20 );
        setHealth( 20D );
        connectionHandler.sendMessage( new MessageS35Respawn( getWorld().getDimension(), getWorld().getDifficulty(), gameMode, "default" ) );
        shownChunks.clear();
        refreshShownChunks( true );
        location = getWorld().getSpawnLocation();
        connectionHandler.sendMessage( new MessageS2FPlayerPositionAndLook( location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), (byte) 0, 0 ) );
        getInventory().setContents( new ItemStack[36] );

    }

    public void dropItem( ItemStack itemStack ) {
        float yaw = location.getYaw(), pitch = location.getPitch();
        Vector vector = new Vector( -Math.cos( pitch ) * Math.sin( yaw ), 0D, Math.cos( pitch ) * Math.sin( yaw ) ).multiply( 3D );
        vector.setX( Math.min( 2, vector.getX() ) );
        vector.setZ( Math.min( 2, vector.getZ() ) );
        this.getWorld().spawnItem( new Location( getWorld(),location.getX() + vector.getX(), location.getY(), location.getZ() + vector.getZ() ), itemStack );
    }

    @Override
    public void tick() {
        super.tick();
        healthCounter++;
        if ( healthCounter == 80 ) {
            healthCounter = 0;
            double health = getHealth();
            double maxHealth = getMaxHealth();
            if ( health < maxHealth ) {
                setHealth( Math.min( health + 1, maxHealth ) );
            }
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public Map<String, Object> serialize() {
        return Collections.emptyMap();
    }

    @Override
    public void sendPluginMessage( Plugin plugin, String s, byte[] bytes ) {
        this.connectionHandler.sendMessage( new MessageS18PluginMessage( s, Unpooled.wrappedBuffer(  bytes ) ) );
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return this.pluginMessageChannels;
    }
}
