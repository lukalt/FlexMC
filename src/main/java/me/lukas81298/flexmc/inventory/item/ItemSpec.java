package me.lukas81298.flexmc.inventory.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.inventory.Material;

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

    public ItemStack breakBlock( Player player, ItemStack itemStack ) {
        return itemStack;
    }
}
