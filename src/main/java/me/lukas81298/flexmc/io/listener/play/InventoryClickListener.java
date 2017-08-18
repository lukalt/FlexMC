package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC07ClickWindow;
import me.lukas81298.flexmc.io.message.play.server.MessageS11ConfirmTransaction;
import me.lukas81298.flexmc.io.message.play.server.MessageS14WindowItems;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class InventoryClickListener implements MessageInboundListener<MessageC07ClickWindow> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC07ClickWindow message ) throws Exception {
        boolean confirm = connectionHandler.getPlayer().getOpenInventory().click( message.getWindowId(), message.getSlot(), message.getButton(), message.getMode(), message.getItemStack() );
        connectionHandler.sendMessage( new MessageS11ConfirmTransaction( (byte) message.getWindowId(), message.getAction(), confirm ) );
        if ( !confirm ) {
            connectionHandler.sendMessage( new MessageS14WindowItems( (byte) message.getWindowId(), connectionHandler.getPlayer().getInventory().getRawSlotsArray() ) );
        }
    }

}
