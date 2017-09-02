package me.lukas81298.flexmc.inventory.crafting.config;

import lombok.Getter;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lukas
 * @since 10.08.2017
 */
@Getter
public class RecipeConfig extends YamlConfig {

    private List<ShapedRecipeConfig> shapedRecipes = new ArrayList<>();
    private List<ShapelessRecipeConfig> shapelessRecipes = new ArrayList<>();

    public RecipeConfig( File folder ) {
        CONFIG_FILE = new File( folder, "recipes.yml" );
    }
}
