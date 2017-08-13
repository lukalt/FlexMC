package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC15EntityAction;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class EntityActionListener implements MessageInboundListener<MessageC15EntityAction> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC15EntityAction message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        switch ( message.getActionId() ) {
            case 0:
                player.setSneaking( true );
                EventFactory.call( new PlayerToggleSneakEvent( player, true ) );
                break;
            case 1:
                player.setSneaking( false );
                EventFactory.call( new PlayerToggleSneakEvent( player, false ) );
                break;
            case 2:
                player.setSprinting( true );
                EventFactory.call( new PlayerToggleSprintEvent( player, true ) );
                break;
            case 3:
                player.setSprinting( false );
                EventFactory.call( new PlayerToggleSprintEvent( player, false ) );
                break;

            // todo handle other cases
        }
    }

}
