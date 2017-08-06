package me.lukas81298.flexmc.io.message.play.client;

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
public class MessageC08CloseWindow extends Message {

    private byte windowId;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        windowId = buf.readByte();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
