package me.lukas81298.flexmc.inventory.crafting.shape;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;
import me.lukas81298.flexmc.inventory.ItemStackConstants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class ShapeBuilder {

    private final TCharObjectMap<ItemStack> palette = new TCharObjectHashMap<>();
    private final String[] pattern;

    public ShapeBuilder( String... pattern ) {
        this.pattern = pattern;
    }

    public ShapeBuilder add( char c, ItemStack itemStack ) {
        this.palette.put( c, itemStack );
        return this;
    }

    public ShapeBuilder add( char c, Material material, short durability ) {
        return this.add( c, new ItemStack( material, 1, durability ) );
    }

    public ShapeBuilder add( char c, Material material ) {
        return this.add( c, new ItemStack( material ) );
    }

    public ShapeBuilder addIgnoreData( char c, Material material ) {
        return this.add( c, new ItemStack( material, ItemStackConstants.IGNORE_DATA_VALUE ) );
    }

    public CompiledShape build() {
        return new CompiledShape( palette, pattern );
    }

}
