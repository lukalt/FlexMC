package me.lukas81298.flexmc.util.crafting;

import me.lukas81298.flexmc.inventory.ItemStack;

/**
 * @author lukas
 * @since 10.08.2017
 */
public class ShapelessRecipe implements Recipe {

    private final ItemStack result;
    private final ItemStack[] ingredients;

    public ShapelessRecipe( ItemStack result, ItemStack... ingredients ) {
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean apply( CraftingInput input ) {
        int s = input.getInputs().size();
        int expected = 0;
        for( ItemStack t : ingredients ) {
            expected += t.getAmount();
            if( !input.hasInputItems( t, t.getAmount() ) ) {
                return false;
            }
        }
        if( s != expected ) {
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getResult() {
        return this.result;
    }
}