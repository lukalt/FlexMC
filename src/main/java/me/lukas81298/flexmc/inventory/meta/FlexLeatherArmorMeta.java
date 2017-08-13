package me.lukas81298.flexmc.inventory.meta;

import io.gomint.taglib.NBTTagCompound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Color;

/**
 * @author lukas
 * @since 13.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlexLeatherArmorMeta extends FlexItemMeta {

    private Color color = Color.fromRGB( 0xA06540 );

    @Override
    public synchronized void write( NBTTagCompound tagCompound ) {
        super.write( tagCompound );
        tagCompound.addValue( "color", color.asRGB() );
    }

}
