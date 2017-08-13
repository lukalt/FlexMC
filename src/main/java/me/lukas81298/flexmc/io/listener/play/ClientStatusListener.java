package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC03ClientStatus;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class ClientStatusListener implements MessageInboundListener<MessageC03ClientStatus> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC03ClientStatus message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        if( message.getAction() == 0 ) {
            if( !player.isAlive() ) {
                player.respawn();
            }
        } else {
            player.sendMessage( "Keine Stats für dich, gibts nicht ;D" );
        }
    }

}
