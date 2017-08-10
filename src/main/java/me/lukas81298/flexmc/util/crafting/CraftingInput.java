package me.lukas81298.flexmc.util.crafting;

import me.lukas81298.flexmc.inventory.ItemStack;

import java.util.Collection;

/**
 * @author lukas
 * @since 10.08.2017
 */
public interface CraftingInput {

    boolean hasInputItems( ItemStack itemStack, int amount );

    Collection<ItemStack> getInputs();

    int getInputSize();

}
