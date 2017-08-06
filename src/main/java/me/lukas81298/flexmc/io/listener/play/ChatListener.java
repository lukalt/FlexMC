package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC02ChatMessage;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class ChatListener implements MessageInboundListener<MessageC02ChatMessage> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC02ChatMessage message ) {
        Player player = connectionHandler.getPlayer();
        if( player != null && message.getMessage().length() < 256 ) {
            for( Player target : Flex.getServer().getPlayerManager().getOnlinePlayers() ) {
                target.sendMessage( "<" + player.getName() + "> " + message.getMessage() );
            }
        }
    }
}
