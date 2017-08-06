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
public class MessageC06EnchantItem extends Message {

    private byte windowId;
    private byte enchantment;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        windowId = buf.readByte();
        enchantment = buf.readByte();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
