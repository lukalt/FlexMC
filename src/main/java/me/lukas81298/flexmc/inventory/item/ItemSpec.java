package me.lukas81298.flexmc.inventory.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 12.08.2017
 */
@RequiredArgsConstructor
public abstract class ItemSpec {

    @Getter
    private final int type;

    public ItemSpec( Material type ) {
        this.type = type.getId();
    }

    public ItemStack breakBlock( FlexPlayer player, ItemStack itemStack ) {
        return itemStack;
    }
}
