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
public class BlockCoalOre extends BlockSpec {

    public BlockCoalOre() {
        super( Material.COAL_ORE );
    }

    @Override
    public Collection<ItemStack> getDrops( Player player, int data ) {
        ItemStack itemStack = player.getItemInHand();
        if( itemStack == null || itemStack.getType() == 0 ) {
            return Collections.emptyList();
        }
        return Collections.singletonList( new ItemStack( 263 ) );
    }
}
