package me.lukas81298.flexmc.world.block;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Material;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class BlockWorkbench extends BlockSpec {

    public BlockWorkbench() {
        super( Material.WORKBENCH );
    }

    @Override
    public void click( FlexPlayer player, FlexBlock block ) {
        player.openWorkbench( block.getLocation(), false );
    }
}
