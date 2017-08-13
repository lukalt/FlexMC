package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC0EPPlayerPosAndLook;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import org.bukkit.Location;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class PositionAndLookListener implements MessageInboundListener<MessageC0EPPlayerPosAndLook> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC0EPPlayerPosAndLook message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        player.teleport( new Location( null, message.getX(), message.getY(), message.getZ(), message.getYaw(), message.getPitch() ), message.isOnGround() );
    }

}
