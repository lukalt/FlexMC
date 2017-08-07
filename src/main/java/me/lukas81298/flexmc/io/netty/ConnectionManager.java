package me.lukas81298.flexmc.io.netty;

import lombok.Getter;
import me.lukas81298.flexmc.io.listener.ListenerManager;
import me.lukas81298.flexmc.io.listener.handshake.HandshakeListener;
import me.lukas81298.flexmc.io.listener.login.MessageClientLoginListener;
import me.lukas81298.flexmc.io.listener.play.*;
import me.lukas81298.flexmc.io.listener.status.ClientPingListener;
import me.lukas81298.flexmc.io.listener.status.ClientRequestStatusListener;
import me.lukas81298.flexmc.io.message.MessageRegistry;
import me.lukas81298.flexmc.io.message.handshake.MessageRegistryHandshake;
import me.lukas81298.flexmc.io.message.login.MessageRegistryLogin;
import me.lukas81298.flexmc.io.message.play.MessageRegistryPlay;
import me.lukas81298.flexmc.io.message.status.MessageRegistryStatus;
import me.lukas81298.flexmc.io.protocol.ProtocolState;

import java.util.EnumMap;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class ConnectionManager {

    private final EnumMap<ProtocolState, MessageRegistry> registries = new EnumMap<>( ProtocolState.class );
    @Getter private final ListenerManager listenerManager = new ListenerManager();

    public ConnectionManager() {
        this.registries.put( ProtocolState.HANDSHAKE, new MessageRegistryHandshake() );
        this.registries.put( ProtocolState.LOGIN, new MessageRegistryLogin() );
        this.registries.put( ProtocolState.STATUS, new MessageRegistryStatus() );
        this.registries.put( ProtocolState.PLAY, new MessageRegistryPlay() );
        this.registries.values().forEach( d -> d.register() );

        this.listenerManager.registerListener( new HandshakeListener() );
        this.listenerManager.registerListener( new ClientPingListener() );
        this.listenerManager.registerListener( new ClientRequestStatusListener() );
        this.listenerManager.registerListener( new MessageClientLoginListener() );
        this.listenerManager.registerListener( new ClientSettingsListener() );
        this.listenerManager.registerListener( new TeleportConfirmListener() );
        this.listenerManager.registerListener( new KeepAliveListener() );
        this.listenerManager.registerListener( new ChatListener() );
        this.listenerManager.registerListener( new DiggingListener() );
        this.listenerManager.registerListener( new PositionAndLookListener() );
        this.listenerManager.registerListener( new PositionListener() );
        this.listenerManager.registerListener( new PlayerLookListener() );
        this.listenerManager.registerListener( new AnimationListener() );
        this.listenerManager.registerListener( new EntityActionListener() );
        this.listenerManager.registerListener( new BlockPlaceListener() );
        this.listenerManager.registerListener( new HeldItemListener() );
    }

    public MessageRegistry getRegistry( ConnectionHandler connectionHandler ) {
        return this.registries.get( connectionHandler.getProtocolState() );
    }

}
