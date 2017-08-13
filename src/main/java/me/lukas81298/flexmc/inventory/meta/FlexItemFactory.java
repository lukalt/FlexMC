package me.lukas81298.flexmc.inventory.meta;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class FlexItemFactory implements ItemFactory {

    private interface ItemTypeProvider extends Supplier<FlexItemMeta> {

        boolean isApplicable( ItemMeta meta );
    }

    private final EnumMap<Material, ItemTypeProvider> itemMeta = new EnumMap<>( Material.class );
    private final ItemTypeProvider defaultMeta = new ItemTypeProvider() {
        @Override
        public boolean isApplicable( ItemMeta meta ) {
            return true;
        }

        @Override
        public FlexItemMeta get() {
            return new FlexItemMeta();
        }
    };

    public FlexItemFactory() {
        this.registerBulk( new ItemTypeProvider() {

            @Override
            public boolean isApplicable( ItemMeta meta ) {
                return meta instanceof FlexLeatherArmorMeta;
            }

            @Override
            public FlexItemMeta get() {
                return new FlexLeatherArmorMeta();
            }
        }, Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS );
    }

    private void registerBulk( ItemTypeProvider supplier, Material... materials ) {
        for ( Material material : materials ) {
            this.itemMeta.put( material, supplier );
        }
    }

    @Override
    public ItemMeta getItemMeta( Material material ) {
        ItemTypeProvider callable = this.itemMeta.getOrDefault( material, this.defaultMeta );
        return callable.get();
    }

    @Override
    public boolean isApplicable( ItemMeta itemMeta, ItemStack itemStack ) throws IllegalArgumentException {
        return isApplicable( itemMeta, itemStack.getType() );
    }

    @Override
    public boolean isApplicable( ItemMeta itemMeta, Material material ) throws IllegalArgumentException {
        ItemTypeProvider callable = this.itemMeta.getOrDefault( material, this.defaultMeta );
        return callable.isApplicable( itemMeta );
    }

    @Override
    public boolean equals( ItemMeta a, ItemMeta b ) throws IllegalArgumentException {
        return Objects.equals( a, b );
    }

    @Override
    public ItemMeta asMetaFor( ItemMeta itemMeta, ItemStack itemStack ) throws IllegalArgumentException {
        return this.getItemMeta( itemStack.getType() );
    }

    @Override
    public ItemMeta asMetaFor( ItemMeta itemMeta, Material material ) throws IllegalArgumentException {
        return this.getItemMeta( material );
    }

    @Override
    public Color getDefaultLeatherColor() {
        return Color.fromRGB( 0xA06540 );
    }
}
