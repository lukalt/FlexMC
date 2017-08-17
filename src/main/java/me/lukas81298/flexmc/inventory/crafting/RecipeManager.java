package me.lukas81298.flexmc.inventory.crafting;

import me.lukas81298.flexmc.inventory.crafting.shape.ShapeBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lukas
 * @since 10.08.2017
 */
public class RecipeManager {

    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeManager() {
        /*RecipeConfig recipeConfig = new RecipeConfig( new File( "config" ) );

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
        }*/

        for ( int i = 0; i < 4; i++ ) {
            this.recipes.add( new ShapelessRecipe( new ItemStack( Material.WOOD, 4, (short) i ), new ItemStack( Material.LOG, 1, (short) i ) ) );
        }
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.WORKBENCH ),
                new ShapeBuilder( "MM", "MM" ).addIgnoreData( 'M', Material.WOOD ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.STICK, 4 ),
                new ShapeBuilder( "M", "M" ).add( 'M', Material.WOOD ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.WOOD_PLATE ),
                new ShapeBuilder( "MM" ).addIgnoreData( 'M', Material.WOOD ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.STONE_PLATE ),
                new ShapeBuilder( "MM" ).addIgnoreData( 'M', Material.STONE ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.TORCH, 4 ),
                new ShapeBuilder( "M", "N" ).addIgnoreData( 'M', Material.COAL ).add( 'N', Material.STICK ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.FLINT_AND_STEEL ),
                new ShapeBuilder( "M0","0N" ).add( 'M', Material.IRON_INGOT ).add( 'N', Material.FLINT ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.SHEARS ),
                new ShapeBuilder( "0M", "M0" ).add( 'M', Material.IRON_INGOT ).build() ) );
        this.recipes.add( new ShapedRecipe( Material.LEVER,
                new ShapeBuilder( "M", "N" ).add( 'M', Material.STICK ).add( 'N', Material.COBBLESTONE ).build() ) );
        this.recipes.add( new ShapedRecipe( Material.GLOWSTONE,
                new ShapeBuilder( "MM", "MM" ).add( 'M', Material.GLOWSTONE ).build() ) );
        this.recipes.add( new ShapedRecipe( Material.SNOW_BLOCK,
                new ShapeBuilder( "MM", "MM" ).add( 'M', Material.SNOW_BALL ).build() ) );
        this.recipes.add( new ShapedRecipe( new ItemStack( Material.SMOOTH_BRICK ),
                new ShapeBuilder( "MM", "MM" ).add( 'M', Material.STONE ).build() ) );
        this.recipes.add( new ShapedRecipe( Material.SANDSTONE ,
                new ShapeBuilder( "MM", "MM" ).add( 'M', Material.SAND ).build() ) );


    }

    public Recipe getRecipe( CraftingInput input ) {
        for ( Recipe recipe : recipes ) {
            if ( recipe.apply( input ) ) {
                return recipe;
            }
        }
        return null;
    }
}
