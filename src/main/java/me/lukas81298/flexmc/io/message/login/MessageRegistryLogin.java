package me.lukas81298.flexmc.io.message.login;

import me.lukas81298.flexmc.io.message.MessageRegistry;
import me.lukas81298.flexmc.io.message.login.client.MessageC00LoginStart;
import me.lukas81298.flexmc.io.message.login.client.MessageC01EncryptionResponse;
import me.lukas81298.flexmc.io.message.login.server.MessageS00Disconnect;
import me.lukas81298.flexmc.io.message.login.server.MessageS01EncryptionRequest;
import me.lukas81298.flexmc.io.message.login.server.MessageS02LoginSuccess;
import me.lukas81298.flexmc.io.message.login.server.MessageS03SetCompression;
import me.lukas81298.flexmc.io.message.play.client.MessageC01TabComplete;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class MessageRegistryLogin extends MessageRegistry {

    @Override
    public void register() {
        registerClientMessage( 0x00, MessageC00LoginStart.class );
        registerClientMessage( 0x01, MessageC01EncryptionResponse.class );

        registerServerMessage( 0x00, MessageS00Disconnect.class );
        registerServerMessage( 0x01, MessageS01EncryptionRequest.class );
        registerServerMessage( 0x02, MessageS02LoginSuccess.class );
        registerServerMessage( 0x03, MessageS03SetCompression.class );
    }

}
