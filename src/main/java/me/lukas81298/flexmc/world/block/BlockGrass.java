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
public class BlockGrass extends BlockSpec {

    public BlockGrass() {
        super( 2 );
    }

    @Override
    public Collection<ItemStack> getDrops( FlexPlayer player, int data ) {
        return Collections.singletonList( new ItemStack( Material.DIRT ) );
    }
}
