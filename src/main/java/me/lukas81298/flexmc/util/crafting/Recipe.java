package me.lukas81298.flexmc.util.crafting;

import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 10.08.2017
 */
public interface Recipe {

    boolean apply( CraftingInput input );

    ItemStack getResult();
}
