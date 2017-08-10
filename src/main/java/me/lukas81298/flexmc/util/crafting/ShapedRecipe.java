package me.lukas81298.flexmc.util.crafting;

import me.lukas81298.flexmc.inventory.ItemStack;

/**
 * @author lukas
 * @since 10.08.2017
 */
public class ShapedRecipe implements Recipe {

    private final ItemStack result;

    public ShapedRecipe( ItemStack result ) {
        this.result = result;
    }

    @Override
    public boolean apply( CraftingInput input ) {
        return false;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }
}
