package me.lukas81298.flexmc.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cubespace.Yamler.Config.YamlConfig;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 13.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemStackConfig extends YamlConfig {

    private int type;
    private int amount;
    private int durability;

    public ItemStack getItemStack() {
        return new ItemStack( type, amount, (short) durability );
    }
}
