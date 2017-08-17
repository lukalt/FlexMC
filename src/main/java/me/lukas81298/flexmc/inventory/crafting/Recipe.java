package me.lukas81298.flexmc.inventory.crafting;

import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 10.08.2017
 */
public interface Recipe {

    boolean apply( CraftingInput input );

    ItemStack getResult();

    org.bukkit.inventory.Recipe toBukkitRecipe();
}
