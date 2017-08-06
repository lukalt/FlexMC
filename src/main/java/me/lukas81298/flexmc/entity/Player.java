package me.lukas81298.flexmc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.io.message.play.server.*;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.Difficulty;
import me.lukas81298.flexmc.util.GameMode;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.ChunkColumn;
import me.lukas81298.flexmc.world.Dimension;
import me.lukas81298.flexmc.world.World;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public Player( int entityId, Location position, String name, UUID uuid, ConnectionHandler connectionHandler, World world ) {
        super( entityId, position, world );
        this.name = name;
        this.uuid = uuid;
        this.connectionHandler = connectionHandler;
    }

    public void spawnPlayer() {


        connectionHandler.sendMessage( new MessageS23JoinGame( this.getEntityId(), gameMode, Dimension.OVER_WORLD, Difficulty.PEACEFUL, "default", false ) );
        connectionHandler.sendMessage( new MessageS46SpawnPosition( new Vector3i( 0, 10, 0 ) ) );
        connectionHandler.sendMessage( new MessageS2CPlayerAbilities( (byte) 0, .2F, .2F ) );
        connectionHandler.sendMessage( new MessageS2FPlayerPositionAndLook( getLocation().x(), getLocation().y(), getLocation().z(), 0F, 0F, (byte) 0, 0 ) );

        this.sendChunks();

        connectionHandler.sendMessage( new MessageS16SetSlot( (byte) 0, (short) 36, new ItemStack( 3, 1, (short) 0 ) ) );

    }

    private void sendChunks() {
        for( ChunkColumn column : this.getWorld().getColumns() ) {
            this.connectionHandler.sendMessage( new MessageS20ChunkData( column ) );
        }
        for ( ChunkColumn column : this.getWorld().getColumns() ) {
            this.connectionHandler.sendMessage( new MessageS0BBlockChange( new Vector3i( column.getX() * 16, 4, column.getZ() * 16 ), new BlockState( column.getSections()[0].getBlock( 0, 4, 0 ), 0 ) ) );

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
    public UUID getUniqueId() {
        return uuid;
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
        return this.connectionHandler.getChannelHandlerContext().channel().remoteAddress().toString();
    }
}
