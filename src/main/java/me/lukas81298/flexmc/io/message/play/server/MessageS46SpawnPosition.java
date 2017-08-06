package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

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
public class MessageS46SpawnPosition extends Message {

    private Vector3i position;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        position = Vector3i.fromLong( buf.readLong() );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeLong( position.asLong() );
    }
}
