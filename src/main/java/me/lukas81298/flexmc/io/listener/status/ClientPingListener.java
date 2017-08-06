package me.lukas81298.flexmc.io.listener.status;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.status.client.MessageC01Ping;
import me.lukas81298.flexmc.io.message.status.server.MessageS01Pong;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class ClientPingListener implements MessageInboundListener<MessageC01Ping> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC01Ping message ) {
        connectionHandler.sendMessage( new MessageS01Pong( message.getPayload() ) );
    }
}
