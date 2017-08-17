package me.lukas81298.flexmc.util.crafting;

import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.util.crafting.shape.Shape;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 10.08.2017
 */
@RequiredArgsConstructor
public class ShapedRecipe implements Recipe {

    private final ItemStack result;
    private final Shape shape;

    public ShapedRecipe( Material result, Shape shape ) {
        this.result = new ItemStack( result );
        this.shape = shape;
    }

    @Override
    public boolean apply( CraftingInput input ) {
        return shape.matches( input );
    }

    @Override
    public ItemStack getResult() {
        return result;
    }
}
