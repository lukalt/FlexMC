package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC14PlayerDigging;
import me.lukas81298.flexmc.io.message.play.server.MessageS08BlockBreakAnimation;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.world.BlockState;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class DiggingListener implements MessageInboundListener<MessageC14PlayerDigging> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC14PlayerDigging message ) {
        Player player = connectionHandler.getPlayer();
        if( player != null ) {
            if( message.getStatus() == 2 ) {
                player.getWorld().setBlock( message.getPosition(), new BlockState( 0, 0 ) );
            } else if( message.getStatus() ==  0 ) {
                for( Player t : player.getWorld().getPlayers() ) {
                    t.getConnectionHandler().sendMessage( new MessageS08BlockBreakAnimation( player.getEntityId(), message.getPosition(), (byte) 1 ) );
                }
            } else if ( message.getStatus() == 0 ) {
                for( Player t : player.getWorld().getPlayers() ) {
                    t.getConnectionHandler().sendMessage( new MessageS08BlockBreakAnimation( player.getEntityId(), message.getPosition(), (byte) -1 ) );
                }
            }
        }
    }

}
