package me.lukas81298.flexmc.io.message.status;

import me.lukas81298.flexmc.io.message.MessageRegistry;
import me.lukas81298.flexmc.io.message.status.client.MessageC00RequestStatus;
import me.lukas81298.flexmc.io.message.status.client.MessageC01Ping;
import me.lukas81298.flexmc.io.message.status.server.MessageS00ResponseStatus;
import me.lukas81298.flexmc.io.message.status.server.MessageS01Pong;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class MessageRegistryStatus extends MessageRegistry {

    @Override
    public void register() {

        registerClientMessage( 0x00, MessageC00RequestStatus.class );
        registerClientMessage( 0x01, MessageC01Ping.class );

        registerServerMessage( 0x00, MessageS00ResponseStatus.class );
        registerServerMessage( 0x01, MessageS01Pong.class );
    }

}
