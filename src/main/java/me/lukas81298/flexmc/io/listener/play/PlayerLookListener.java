package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC0FPlayerLook;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.Location;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class PlayerLookListener implements MessageInboundListener<MessageC0FPlayerLook> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC0FPlayerLook message ) {
        Location l = connectionHandler.getPlayer().getLocation();
        connectionHandler.getPlayer().teleport( new Location( l.x(), l.y(), l.z(), message.getYaw(),message.getPitch() ), message.isOnGround() );
    }

}
