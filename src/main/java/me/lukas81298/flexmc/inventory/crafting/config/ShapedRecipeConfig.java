package me.lukas81298.flexmc.inventory.crafting.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lukas81298.flexmc.config.ItemStackConfig;
import net.cubespace.Yamler.Config.YamlConfig;

/**
 * @author lukas
 * @since 10.08.2017
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShapedRecipeConfig extends YamlConfig {

    private ItemStackConfig result;

}
