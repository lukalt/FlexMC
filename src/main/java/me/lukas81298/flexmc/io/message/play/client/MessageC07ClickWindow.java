package me.lukas81298.flexmc.io.message.play.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import org.bukkit.inventory.ItemStack;

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
public class MessageC07ClickWindow extends Message {

    private int windowId;
    private short slot;
    private byte button;
    private short action;
    private int mode;
    private ItemStack itemStack;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        windowId = buf.readUnsignedByte();
        slot = buf.readShort();
        button = buf.readByte();
        action = buf.readShort();
        mode = readVarInt( buf );
        itemStack = readItemStack( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
