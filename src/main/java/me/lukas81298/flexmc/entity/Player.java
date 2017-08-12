package me.lukas81298.flexmc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.entity.metadata.EntityFlag;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.inventory.Material;
import me.lukas81298.flexmc.inventory.PlayerInventory;
import me.lukas81298.flexmc.io.message.play.server.*;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.*;
import me.lukas81298.flexmc.world.Dimension;
import me.lukas81298.flexmc.world.World;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lukas
 * @since 05.08.2017
 */
@EqualsAndHashCode( of = { "name", "uuid" }, callSuper = false )
public class Player extends LivingEntity implements CommandSender {

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
    private final PlayerInventory inventory;
    @Getter
    private volatile int heldItemSlot = 0;
    @Getter
    private AtomicInteger foodLevel = new AtomicInteger( 20 );
    private final Set<ChunkColumn> shownChunks = ConcurrentHashMap.newKeySet();

    private byte healthCounter = 0;

    public Player( int entityId, Location position, String name, UUID uuid, ConnectionHandler connectionHandler, World world ) {
        super( entityId, position, world );
        this.name = name;
        this.uuid = uuid;
        this.connectionHandler = connectionHandler;
        this.inventory = new PlayerInventory( this );
    }

    public void spawnPlayer() {
        connectionHandler.sendMessage( new MessageS23JoinGame( this.getEntityId(), gameMode, Dimension.OVER_WORLD, Difficulty.PEACEFUL, "default", false ) );
        connectionHandler.sendMessage( new MessageS0DServerDifficulty( Difficulty.PEACEFUL ) );
        connectionHandler.sendMessage( new MessageS46SpawnPosition( new Vector3i( 0, 10, 0 ) ) );
        connectionHandler.sendMessage( new MessageS2CPlayerAbilities( (byte) 0, .2F, .2F ) );
        connectionHandler.sendMessage( new MessageS2FPlayerPositionAndLook( getLocation().x(), getLocation().y(), getLocation().z(), 0F, 0F, (byte) 0, 0 ) );
        this.refreshShownChunks();
        inventory.addItem( new ItemStack( 278, 1 ) );
        inventory.addItem( new ItemStack( 279, 1 ) );
        inventory.addItem( new ItemStack( 277, 1 ) );
        inventory.addItem( new ItemStack( 276, 1 ) );
        for( int i = 4; i < 10; i++ ) {
            getInventory().setItem( i, new ItemStack( Material.LOG ) );
        }
    }

    public void refreshShownChunks() {
        Location location = this.getLocation();
        int viewDistance = 7;
        int x = (int) (location.x() / 16), z = (int) (location.z() / 16);
        for( ChunkColumn column : this.getWorld().getColumns() ) {
            if( Math.abs( x - column.getX() ) <= viewDistance && Math.abs( z - column.getZ() ) <= viewDistance ) {
                if( shownChunks.add( column ) ) {
                    this.connectionHandler.sendMessage( new MessageS20ChunkData( column ) );
                }
            } else {
                if( shownChunks.remove( column ) ) {
                    this.connectionHandler.sendMessage( new MessageS1DUnloadChunk( column.getX(), column.getZ() ) );
                }
            }
        }
    }

    public void disconnect( String reason ) {
        if ( this.online.compareAndSet( true, false ) ) {
            this.connectionHandler.sendMessage( new MessageS1ADisconnect( new TextComponent( reason ) ) );
        }
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

    @Override
    public void sendMessage( BaseComponent... components ) {
        this.connectionHandler.sendMessage( new MessageS0FChatMessage( components, (byte) 0 ) );
    }

    @Override
    public boolean hasPermission( String permission ) {
        return true; // todo change
    }

    public void setGameMode( GameMode gameMode ) {
        synchronized ( this ) {
            if( this.gameMode != gameMode ) {
                this.gameMode = gameMode;
                connectionHandler.sendMessage( new MessageS1EChangeGameState( (byte) 3, gameMode.ordinal() ) );
            }
        }
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

    public boolean isSneaking() {
        return this.getFlag( EntityFlag.CROUCHED );
    }

    public boolean isSprinting() {
        return this.getFlag( EntityFlag.SPRINTING );
    }

    public void handleSetHeldItemSlot( int slot ) {
        this.heldItemSlot = slot; // todo update equipment to other players
    }

    public ItemStack getItemInHand() {
        return this.inventory.getItem( heldItemSlot );
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    protected void updateHealth( double health ) {
        super.updateHealth( health );
        if( health >= 0 ) {
            connectionHandler.sendMessage( new MessageS41UpdateHealth( (float) health, foodLevel.get(), 0F ) );
        }
    }

    @Override
    public void teleport( Location l, boolean onGround ) {
        Location k = this.getLocation();
        if( (int)l.x()/16 != (int)k.x()/16 ) {
            if( (int)l.z()/16 != (int)k.z()/16 ) {
                this.refreshShownChunks();
            }
        }
        super.teleport( l, onGround );

    }

    public synchronized void respawn() {
        alive = true;
        foodLevel.set( 20 );
        setHealth( 20D );
        connectionHandler.sendMessage( new MessageS35Respawn( getWorld().getDimension(), getWorld().getDifficulty(), gameMode, "default" ) );
        shownChunks.clear();
        refreshShownChunks();
        location = getWorld().getSpawnLocation();
        connectionHandler.sendMessage( new MessageS2FPlayerPositionAndLook( location.x(), location.y(), location.z(), location.yaw(), location.pitch(), (byte) 0,  0 ) );
        getInventory().setContents( new ItemStack[ 36 ] );

    }

    public void dropItem( ItemStack itemStack ) {
        float yaw = location.yaw(), pitch = location.pitch();
        Vector3d vector = new Vector3d( -Math.cos( pitch ) * Math.sin( yaw ), 0D, Math.cos( pitch ) * Math.sin( yaw ) ).multiply( 3D );
        vector.vz( Math.min( 2, vector.vz() ) );
        vector.vx( Math.min( 2, vector.vx() ) );
        this.getWorld().spawnItem( new Location( location.x() + vector.vx(), location.y(), location.z() + vector.vz() ), itemStack );
    }

    @Override
    public void tick() {
        super.tick();
        healthCounter++;
        if( healthCounter == 80 ) {
            healthCounter = 0;
            double health = getHealth();
            double maxHealth = getMaxHealth();
            if( health < maxHealth ) {
                setHealth( Math.min( health + 1, maxHealth ) );
            }
        }
    }
}
