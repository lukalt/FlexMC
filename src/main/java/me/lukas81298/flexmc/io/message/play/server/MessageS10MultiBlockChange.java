package me.lukas81298.flexmc.io.message.play.server;

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
public class MessageS10MultiBlockChange extends Message {

    private int x;
    private int z;
    private Entry[] entries;

    public static class Entry {
        private int x;
        private int y;
        private int z;
        private int type;
        private int data;
    }

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeInt( x );
        buf.writeInt( z );
        writeVarInt( entries.length, buf );

    }
}
