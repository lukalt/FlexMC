package me.lukas81298.flexmc.util.crafting;

import me.lukas81298.flexmc.config.ItemStackConfig;
import me.lukas81298.flexmc.util.crafting.config.RecipeConfig;
import me.lukas81298.flexmc.util.crafting.config.ShapelessRecipeConfig;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lukas
 * @since 10.08.2017
 */
public class RecipeManager {

    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeManager() {
        RecipeConfig recipeConfig = new RecipeConfig( new File( "config" ) );

        for( short i = 0; i < 4; i++ ) {
            recipeConfig.getShapelessRecipes().add( new ShapelessRecipeConfig( new ItemStackConfig( Material.WOOD.getId(), 4, i ), new ItemStackConfig( Material.LOG.getId(), 1, i ) ) );
        }

        try {
            recipeConfig.init();
        } catch ( InvalidConfigurationException e ) {
            e.printStackTrace();
        }

        for( ShapelessRecipeConfig c : recipeConfig.getShapelessRecipes() ) {
            ItemStack[] ingredients = new ItemStack[c.getInput().size()];
            int i = 0;
            for ( ItemStackConfig itemStackConfig : c.getInput() ) {
                ingredients[ i ] = itemStackConfig.getItemStack();
                i++;
            }
            this.recipes.add( new ShapelessRecipe( c.getResult().getItemStack(), ingredients ) );
        }

    }

    public Recipe getRecipe( CraftingInput input ) {
        for ( Recipe recipe : recipes ) {
            if( recipe.apply( input ) ) {
                return recipe;
            }
        }
        return null;
    }
}
