package me.lukas81298.flexmc.io.message.handshake;

import me.lukas81298.flexmc.io.message.MessageRegistry;
import me.lukas81298.flexmc.io.message.handshake.client.MessageC00HandHake;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class MessageRegistryHandshake extends MessageRegistry {

    @Override
    public void register() {
        this.registerClientMessage( 0, MessageC00HandHake.class );
    }

}
