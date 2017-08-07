package me.lukas81298.flexmc.inventory;

import com.evilco.mc.nbt.tag.TagCompound;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.lukas81298.flexmc.io.message.Message;

/**
 * @author lukas
 * @since 07.08.2017
 */
@AllArgsConstructor
@Getter
@Setter
@Accessors( chain = true )
public class ItemStack {

    private int type;
    private int amount;
    private short damage;
    private final TagCompound meta = new TagCompound( "ItemStack" );

    public ItemStack( int type ) {
        this.type = type;
    }

    public ItemStack( int type, int amount ) {
        this.type = type;
        this.amount = amount;
    }

    public void serialize( ByteBuf buf ) {
        buf.writeShort( type );
        if ( type != -1 ) {
            buf.writeByte( amount );
            buf.writeShort( damage );
            Message.writeNbtTag( this.meta, buf );
        }
    }
}
