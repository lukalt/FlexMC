package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.inventory.ItemStackConstants;
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
public class MessageS14WindowItems extends Message {

    private byte windowID;
    private ItemStack[] slots;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeByte( windowID );
        buf.writeShort( slots.length );
        for ( ItemStack slot : slots ) {
            if( slot == null ) {
                Message.writeItemStack( ItemStackConstants.AIR, buf );
            } else {
                Message.writeItemStack( slot, buf );
            }
        }
    }
}
