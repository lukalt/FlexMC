package me.lukas81298.flexmc.io.message.play.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

import java.io.IOException;
import java.util.Vector;

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
public class MessageC14PlayerDigging extends Message {

    private int status;
    private Vector3i position;
    private byte face;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        status = readVarInt( buf );
        position = Vector3i.fromLong( buf.readLong() );
        face = buf.readByte();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
