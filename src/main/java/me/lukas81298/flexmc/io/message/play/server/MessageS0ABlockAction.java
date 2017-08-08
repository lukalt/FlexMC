package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

import java.io.IOException;

/**
 * @author lukas
 * @since 08.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS0ABlockAction extends Message {

    private Vector3i position;
    private byte actionId;
    private byte actionParam;
    private int blockType;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeLong( position.asLong() );
        buf.writeByte( actionId );
        buf.writeByte( actionParam );
        writeVarInt( blockType, buf );
    }

}
