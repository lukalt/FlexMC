package me.lukas81298.flexmc.io.message.handshake.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 04.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageC00HandHake extends Message {

    private int protocolVersion;
    private String serverAddress;
    private short port;
    private int nextState; // mojang, that field only has two fields, why the fuck an int?

    @Override
    public void read( ByteBuf buf ) throws IOException {
        protocolVersion = readVarInt( buf );
        serverAddress = readString( buf );
        port = buf.readShort();
        nextState = readVarInt( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
