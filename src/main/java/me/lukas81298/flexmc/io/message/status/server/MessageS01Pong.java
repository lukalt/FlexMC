package me.lukas81298.flexmc.io.message.status.server;

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
public class MessageS01Pong extends Message {

    private long payload;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        payload = buf.readLong();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeLong( payload );
    }
}
