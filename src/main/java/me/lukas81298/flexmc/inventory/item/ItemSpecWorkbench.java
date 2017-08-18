package me.lukas81298.flexmc.inventory.item;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Material;

/**
 * @author lukas
 * @since 18.08.2017
 */
public class ItemSpecWorkbench extends ItemSpec {

    public ItemSpecWorkbench() {
        super( Material.WORKBENCH );
    }

    @Override
    public void click( FlexPlayer player ) {
        player.openWorkbench( player.getLocation(), true );
    }
}
