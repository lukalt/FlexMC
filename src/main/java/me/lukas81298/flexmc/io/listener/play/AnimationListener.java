package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC1DAnimation;
import me.lukas81298.flexmc.io.message.play.server.MessageS06Animation;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class AnimationListener implements MessageInboundListener<MessageC1DAnimation> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC1DAnimation message ) {
        for( Player player : connectionHandler.getPlayer().getWorld().getPlayers() ) {
            if( !player.equals( connectionHandler.getPlayer() ) ) {
                player.getConnectionHandler().sendMessage( new MessageS06Animation( connectionHandler.getPlayer().getEntityId(), message.getHand() == 0 ? MessageS06Animation.AnimationType.SWING_MAIN_ARM : MessageS06Animation.AnimationType.SWING_OFF_HAND ) );
            }
        }
    }

}
