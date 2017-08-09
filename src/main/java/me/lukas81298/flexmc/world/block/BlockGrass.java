package me.lukas81298.flexmc.world.block;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.inventory.Material;

import java.util.Collection;
import java.util.Collections;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class BlockGrass extends BlockSpec {

    public BlockGrass() {
        super( 2 );
    }

    @Override
    public Collection<ItemStack> getDrops( Player player, int data ) {
        return Collections.singletonList( new ItemStack( Material.DIRT ) );
    }
}
