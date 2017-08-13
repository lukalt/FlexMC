package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC02ChatMessage;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashSet;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class ChatListener implements MessageInboundListener<MessageC02ChatMessage> {

    @SuppressWarnings( "deprecation" )
    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC02ChatMessage message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        if( player != null && message.getMessage().length() < 256 ) {
            if( message.getMessage().startsWith( "/" ) ) {
                player.sendMessage( "Unknown command. Type /help for help" );
                return;
            }
            PlayerChatEvent oldEvent = EventFactory.call( new PlayerChatEvent( player, message.getMessage(), "<%1$s> %2$s", new HashSet<>( Flex.getServer().getPlayerManager().getOnlinePlayers() ) ) );
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent( true, player, oldEvent.getMessage(), oldEvent.getRecipients() );
            event.setFormat( oldEvent.getFormat() );
            EventFactory.call( event );
            if( !event.isCancelled() ) {
                for( Player target : event.getRecipients() ) {
                    if( target.isOnline() ) {
                        target.sendMessage( String.format( event.getFormat(), player.getDisplayName(), message.getMessage() ) );
                    }
                }
            }
        }
    }
}
