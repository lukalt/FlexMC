package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC0BKeepAlive;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class KeepAliveListener implements MessageInboundListener<MessageC0BKeepAlive> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC0BKeepAlive message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        if( player != null ) {
            player.setLastKeepAlive( System.currentTimeMillis() );
        }
    }
}
