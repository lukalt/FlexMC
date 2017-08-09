package me.lukas81298.flexmc.io.message.login.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 05.08.2017
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageC01EncryptionResponse extends Message {

    private byte[] sharedSecret;
    private byte[] verifyToken;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        sharedSecret = new byte[ readVarInt( buf ) ];
        for ( int i = 0; i < sharedSecret.length; i++ ) {
            sharedSecret[i] = buf.readByte();
        }
        verifyToken = new byte[ readVarInt( buf ) ];
        for ( int i = 0; i < verifyToken.length; i++ ) {
            verifyToken[i] = buf.readByte();
        }
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }

}
