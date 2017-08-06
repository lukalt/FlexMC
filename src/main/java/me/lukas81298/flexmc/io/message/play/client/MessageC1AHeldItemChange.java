package me.lukas81298.flexmc.io.message.play.client;

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
public class MessageC1AHeldItemChange extends Message {

    private short slot;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        slot = buf.readShort();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeShort( slot );
    }
}
