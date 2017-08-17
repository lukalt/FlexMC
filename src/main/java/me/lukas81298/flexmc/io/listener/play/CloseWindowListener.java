package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC08CloseWindow;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class CloseWindowListener implements MessageInboundListener<MessageC08CloseWindow> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC08CloseWindow message ) throws Exception {
        connectionHandler.getPlayer().getInventory().resetCrafting();
    }

}
