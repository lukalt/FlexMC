package me.lukas81298.flexmc.world.block;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class BlockLongGrass extends BlockSpec {

    public BlockLongGrass() {
        super( Material.LONG_GRASS );
    }

    @Override
    public Collection<ItemStack> getDrops( FlexPlayer player, int data ) {
        System.out.println( "ADF " + data );
        if( data == 1 ) {
            return Collections.singletonList( new ItemStack( Material.SEEDS ) );
        }
        return Collections.emptyList();
    }
}
