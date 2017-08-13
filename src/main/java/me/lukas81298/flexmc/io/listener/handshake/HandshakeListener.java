package me.lukas81298.flexmc.io.listener.handshake;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.handshake.client.MessageC00HandHake;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.io.protocol.ProtocolState;
import me.lukas81298.flexmc.util.ConnectionInfo;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class HandshakeListener implements MessageInboundListener<MessageC00HandHake> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00HandHake message ) {
        connectionHandler.setVersion( message.getProtocolVersion() );
        connectionHandler.setConnectionInfo( new ConnectionInfo( message.getServerAddress(), message.getPort() ) );
        if( message.getNextState() == 1 ) { // status
            connectionHandler.setProtocolState( ProtocolState.STATUS );
        } else {
            connectionHandler.setProtocolState( ProtocolState.LOGIN );
        }

    }
}
