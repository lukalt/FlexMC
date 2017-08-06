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
@ToString
public class MessageC05ConfirmTransaction extends Message {

    private byte windowId;
    private short action;
    private boolean accepted;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        windowId = buf.readByte();
        action = buf.readShort();
        accepted = buf.readBoolean();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
