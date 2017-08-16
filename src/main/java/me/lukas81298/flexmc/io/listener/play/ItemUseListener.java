package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC20UseItem;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class ItemUseListener implements MessageInboundListener<MessageC20UseItem> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC20UseItem message ) throws Exception {
        connectionHandler.getPlayer().getInventory().setItemInHand( connectionHandler.getPlayer().getInventory().getItemInHand() );
    }

}
