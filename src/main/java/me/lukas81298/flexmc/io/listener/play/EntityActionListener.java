package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC15EntityAction;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class EntityActionListener implements MessageInboundListener<MessageC15EntityAction> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC15EntityAction message ) {
        Player player = connectionHandler.getPlayer();
        switch ( message.getActionId() ) {
            case 0:
                player.setSneaking( true );
                break;
            case 1:
                player.setSneaking( false );
                break;
            case 2:
                player.setSprinting( true );
                break;
            case 3:
                player.setSprinting( false );
                break;

            // todo handle other cases
        }
    }

}
