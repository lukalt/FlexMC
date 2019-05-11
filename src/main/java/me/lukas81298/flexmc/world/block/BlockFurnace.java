package me.lukas81298.flexmc.world.block;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.inventory.FlexFurnaceInventory;
import org.bukkit.Material;

/**
 * @author lukas
 * @since 02.09.2017
 */
public class BlockFurnace extends BlockSpec {

    public BlockFurnace() {
        super( Material.FURNACE );
    }

    @Override
    public void click( FlexPlayer player, FlexBlock block ) {
        player.openInventory( new FlexFurnaceInventory( (byte) player.getNextWindowId(), player, null ) );
    }
}
