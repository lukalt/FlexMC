package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.io.message.Message;

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
public class MessageS3FEntityEquipment extends Message {

    private int entityId;
    private int slot;
    private ItemStack itemStack;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( entityId, buf );
        writeVarInt( slot, buf );
        if( itemStack == null ) {
            ItemStack.AIR.serialize( buf );
        } else {
            itemStack.serialize( buf );
        }
    }
}
