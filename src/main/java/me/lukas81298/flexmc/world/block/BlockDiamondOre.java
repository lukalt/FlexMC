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
public class BlockDiamondOre extends BlockSpec {

    public BlockDiamondOre() {
        super( Material.DIAMOND_ORE );
    }

    @Override
    public Collection<ItemStack> getDrops( Player player, int data ) {
        ItemStack itemStack = player.getItemInHand();
        if( itemStack != null && ( itemStack.getType() == Material.IRON_PICK_AXE.getId() || itemStack.getType() == Material.DIAMOND_PICK_AXE.getId() ) ) {
            return Collections.singletonList( new ItemStack( Material.DIAMOND ) );
        }
        return Collections.emptyList();
    }
}
