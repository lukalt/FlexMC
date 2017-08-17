package me.lukas81298.flexmc.inventory.crafting;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * @author lukas
 * @since 10.08.2017
 */
public interface CraftingInput {

    boolean hasInputItems( ItemStack itemStack, int amount );

    Collection<ItemStack> getInputs();

    ItemStack[][] getInputArray();

    int getInputSize();

    int getInputHeight();

    int getInputWidth();

}
