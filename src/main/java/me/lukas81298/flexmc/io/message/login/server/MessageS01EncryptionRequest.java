package me.lukas81298.flexmc.io.message.login.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 06.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageS01EncryptionRequest extends Message {

    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    @Override
    public void read( ByteBuf buf ) throws IOException {
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeString( serverId, buf );
        writeVarInt( publicKey.length, buf );
        for ( byte b : publicKey ) {
            buf.writeByte( b );
        }
        writeVarInt( verifyToken.length, buf );
        for ( byte b : verifyToken ) {
            buf.writeByte( b );
        }
    }
}
