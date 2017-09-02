package me.lukas81298.flexmc.inventory.crafting.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lukas81298.flexmc.config.ItemStackConfig;
import net.cubespace.Yamler.Config.YamlConfig;

import java.util.Arrays;
import java.util.List;

/**
 * @author lukas
 * @since 10.08.2017
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShapelessRecipeConfig extends YamlConfig {

    private ItemStackConfig result;
    private List<ItemStackConfig> input;

    public ShapelessRecipeConfig( ItemStackConfig result, ItemStackConfig... inputs ) {
        this.result = result;
        this.input = Arrays.asList( inputs );
    }

}
