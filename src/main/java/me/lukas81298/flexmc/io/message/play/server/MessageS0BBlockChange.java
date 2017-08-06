package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;

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
public class MessageS0BBlockChange extends Message {

    private Vector3i position;
    private BlockState block;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeLong( position.asLong() );
        writeVarInt( block.getId() << 4 | ( block.getData() & 15 ), buf );
    }
}
