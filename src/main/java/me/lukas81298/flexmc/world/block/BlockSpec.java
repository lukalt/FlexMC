package me.lukas81298.flexmc.world.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

    public Collection<ItemStack> getDrops( FlexPlayer player, int data ) {
        return Collections.singletonList( new ItemStack( type, 1, (short) data ) );
    }

    public void breakBlock( FlexPlayer player, FlexWorld world, Vector3i position ) {

    }

    public void click( FlexPlayer player ) {

    }
}
