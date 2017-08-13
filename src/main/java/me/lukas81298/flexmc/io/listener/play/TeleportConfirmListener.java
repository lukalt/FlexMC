package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC00TeleportConfirm;
import me.lukas81298.flexmc.io.message.play.server.MessageS2FPlayerPositionAndLook;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import org.bukkit.Location;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class TeleportConfirmListener implements MessageInboundListener<MessageC00TeleportConfirm> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00TeleportConfirm message ) {
        if( message.getTeleportId() == 0 ) {
            Location position = connectionHandler.getPlayer().getLocation();
            connectionHandler.sendMessage( new MessageS2FPlayerPositionAndLook( position.getX(), position.getY(), position.getZ(), 0F, 0F, (byte) 0, (int) ( Math.random() * Integer.MAX_VALUE ) ) );
        }
    }

}
