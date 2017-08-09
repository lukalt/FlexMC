package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC07ClickWindow;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class InventoryClickListener implements MessageInboundListener<MessageC07ClickWindow> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC07ClickWindow message ) throws Exception {
        if( message.getWindowId() == 0 ) {
            connectionHandler.getPlayer().getInventory().click( message.getSlot(), message.getButton(), message.getAction(), message.getMode(), message.getItemStack() );
        }
    }

}
