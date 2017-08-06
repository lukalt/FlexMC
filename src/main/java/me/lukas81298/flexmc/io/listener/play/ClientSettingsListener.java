package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC04ClientSettings;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class ClientSettingsListener implements MessageInboundListener<MessageC04ClientSettings> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC04ClientSettings message ) {
        // todo implement
    }
}
