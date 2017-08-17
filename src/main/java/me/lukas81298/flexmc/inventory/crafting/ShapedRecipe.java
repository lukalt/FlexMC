package me.lukas81298.flexmc.inventory.crafting;

import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.inventory.crafting.shape.CompiledShape;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 10.08.2017
 */
@RequiredArgsConstructor
public class ShapedRecipe implements Recipe {

    private final ItemStack result;
    private final CompiledShape compiledShape;

    public ShapedRecipe( Material result, CompiledShape compiledShape ) {
        this.result = new ItemStack( result );
        this.compiledShape = compiledShape;
    }

    @Override
    public boolean apply( CraftingInput input ) {
        return compiledShape.matches( input );
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public org.bukkit.inventory.ShapedRecipe toBukkitRecipe() {
        return new org.bukkit.inventory.ShapedRecipe( result ); // todo fill up
    }
}
