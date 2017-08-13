package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC0FPlayerLook;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import org.bukkit.Location;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class PlayerLookListener implements MessageInboundListener<MessageC0FPlayerLook> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC0FPlayerLook message ) {
        Location l = connectionHandler.getPlayer().getLocation();
        connectionHandler.getPlayer().teleport( new Location( null, l.getX(), l.getY(), l.getZ(), message.getYaw(),message.getPitch() ), message.isOnGround() );
    }

}
