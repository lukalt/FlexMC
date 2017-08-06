package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC0DPlayerPosition;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.Location;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class PositionListener implements MessageInboundListener<MessageC0DPlayerPosition> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC0DPlayerPosition message ) {
        Player player = connectionHandler.getPlayer();
        player.teleport( new Location( message.getX(), message.getY(), message.getZ(), player.getLocation().yaw(), player.getLocation().pitch() ), message.isOnGround() );
    }
}
