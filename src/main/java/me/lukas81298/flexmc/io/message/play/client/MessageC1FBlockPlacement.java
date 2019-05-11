package me.lukas81298.flexmc.io.message.play.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

import java.io.IOException;

/**
 * @author lukas
 * @since 07.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageC1FBlockPlacement extends Message {

    private Vector3i position;
    private int face;
    private int hand;
    private float curX, curY, curZ;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        position = Vector3i.fromLong( buf.readLong() );
        face = readVarInt( buf );
        hand = readVarInt( buf );
        curX = buf.readFloat();
        curY = buf.readFloat();
        curZ = buf.readFloat();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
