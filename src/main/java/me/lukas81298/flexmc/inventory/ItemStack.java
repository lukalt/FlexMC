package me.lukas81298.flexmc.inventory;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.NBTConstants;
import com.flowpowered.nbt.stream.NBTOutputStream;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    private final CompoundTag compoundTag = new CompoundTag( "ItemData", new CompoundMap() );

    public ItemStack( int type ) {
        this.type = type;
    }

    public ItemStack( int type, int amount ) {
        this.type = type;
        this.amount = amount;
    }

    public void serialize( ByteBuf buf ) {
        buf.writeShort( type );
        if( type != -1 ) {
            buf.writeByte( amount );
            buf.writeShort( damage );
            buf.writeByte( (byte) 0 ); // end
            /*ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                NBTOutputStream nbtOutputStream = new NBTOutputStream( os );
                nbtOutputStream.writeTag( compoundTag );
                buf.writeBytes( os.toByteArray() );
            } catch ( IOException e ) {
                e.printStackTrace();
            }*/
        }
    }
}
