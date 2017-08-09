package me.lukas81298.flexmc.inventory;

import com.evilco.mc.nbt.tag.TagCompound;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class ItemStack {

    public static ItemStack AIR = new ItemStack( 0 );

    private int type;
    private int amount = 1;
    private short damage;
    private TagCompound meta = new TagCompound( "ItemStack" );

    public ItemStack( Material material ) {
        this.type = material.getId();
    }

    public ItemStack( Material material, int amount ) {
        this.type = material.getId();
        this.amount = amount;
    }

    public ItemStack( Material material, int amount, short data ) {
        this.type = material.getId();
        this.amount = amount;
        this.damage = data;
    }

    public ItemStack( int type ) {
        this.type = type;
    }

    public ItemStack( int type, int amount ) {
        this.type = type;
        this.amount = amount;
    }

    public ItemStack( int type, int amount, short s ) {
        this.type = type;
        this.amount = amount;
        this.damage = s;
    }

    public void serialize( ByteBuf buf ) {
        buf.writeShort( type );
        if ( type != -1 ) {
            buf.writeByte( amount );
            buf.writeShort( damage );
            Message.writeNbtTag( this.meta, buf );
        }
    }

    public boolean isSimilar( ItemStack other ) {
        return other != null && ( other == this || ( other.type == type && other.damage == damage ) );
    }

    public boolean isEmpty() {
        return this.amount <= 0;
    }

    public static boolean isEqual( ItemStack a, ItemStack b ) {
        return a == b || a == null && b.type <= 0 || b == null && a.type <= 0 || b != null &&
                a != null && ( b.type <= 0 && a.type <= 0 || a.type == b.type &&
                a.amount == b.amount && a.damage == b.damage );
    }
}
