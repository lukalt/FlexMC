package me.lukas81298.flexmc.world.block;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class BlockCactus extends BlockSpec {

    public BlockCactus() {
        super( Material.CACTUS );
    }

    @Override
    public void breakBlock( FlexPlayer player, FlexWorld world, Vector3i position ) {
        position = position.add(0, 1, 0 );
        if( position.getY() < 255 ) {
            BlockState state;
            do {
               state = world.getBlockAt( position );
               if( state.getType() == Material.CACTUS ) {
                   world.setBlock( position, new BlockState( Material.AIR ) );
                   world.dropItem( position.toLocation( world ), new ItemStack( Material.CACTUS ) );
                   position = position.add( 0, 1, 0 );
               }
            } while ( state.getType() == Material.CACTUS && position.getY() < 255 );

        }
        super.breakBlock( player, world, position );
    }
}
