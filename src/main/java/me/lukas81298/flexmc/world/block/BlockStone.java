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
public class BlockStone extends BlockSpec {

    public BlockStone() {
        super( 1 );
    }

    @Override
    public Collection<ItemStack> getDrops( Player player, int data ) {
        ItemStack itemInHand = player.getItemInHand();
        if( itemInHand == null || itemInHand.getType() == 0 ) {
            return Collections.emptyList();
        }
        if( data == 0 ) {
            return Collections.singletonList( new ItemStack( Material.COBBLESTONE ) );
        }
        return super.getDrops( player, data );
    }
}
