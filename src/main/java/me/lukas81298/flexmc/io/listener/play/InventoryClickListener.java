package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC07ClickWindow;
import me.lukas81298.flexmc.io.message.play.server.MessageS11ConfirmTransaction;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class InventoryClickListener implements MessageInboundListener<MessageC07ClickWindow> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC07ClickWindow message ) throws Exception {
        if( message.getWindowId() == 0 ) {
            boolean confirm = connectionHandler.getPlayer().getInventory().click( connectionHandler.getPlayer(), message.getSlot(), message.getButton(), message.getMode(), message.getItemStack() );
            connectionHandler.sendMessage( new MessageS11ConfirmTransaction( (byte) 0, message.getAction(), confirm ) );
        } else {
            connectionHandler.sendMessage( new MessageS11ConfirmTransaction( (byte) message.getWindowId(), message.getAction(), false ) );
        }
    }

}
