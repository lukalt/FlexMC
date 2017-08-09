package me.lukas81298.flexmc.world.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.inventory.Material;

import java.util.Collection;
import java.util.Collections;

/**
 * @author lukas
 * @since 09.08.2017
 */
@RequiredArgsConstructor
public class BlockSpec {

    @Getter private final int type;

    public BlockSpec( Material type ) {
        this.type = type.getId();
    }

    public Collection<ItemStack> getDrops( Player player, int data ) {
        return Collections.singletonList( new ItemStack( type, 1, (short) data ) );
    }
}
