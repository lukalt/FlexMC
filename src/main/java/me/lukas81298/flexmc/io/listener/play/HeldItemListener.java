package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC1AHeldItemChange;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class HeldItemListener implements MessageInboundListener<MessageC1AHeldItemChange> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC1AHeldItemChange message ) {
        connectionHandler.getPlayer().handleSetHeldItemSlot( message.getSlot() );
    }

}
