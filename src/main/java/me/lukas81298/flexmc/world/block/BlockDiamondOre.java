package me.lukas81298.flexmc.world.block;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
    public Collection<ItemStack> getDrops( FlexPlayer player, int data ) {
        ItemStack itemStack = player.getItemInHand();
        if( itemStack != null && ( itemStack.getType() == Material.IRON_PICKAXE || itemStack.getType() == Material.DIAMOND_PICKAXE ) ) {
            return Collections.singletonList( new ItemStack( Material.DIAMOND ) );
        }
        return Collections.emptyList();
    }
}
