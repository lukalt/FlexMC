package me.lukas81298.flexmc.inventory;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 02.09.2017
 */
public class FlexFurnaceInventory extends FlexInventory implements FurnaceInventory {

    private final Furnace furnace;

    public FlexFurnaceInventory( byte windowId, FlexPlayer player, Furnace furnace ) {
        super( 3, windowId, "Furnace", "minecraft:furnace", 3 );
        viewers.add( player );
        this.furnace = furnace;
    }

    @Override
    protected ItemStack getItemFromRawSlot( int slot, int specialSlots ) {
        return getItem( slot );
    }

    @Override
    protected void setRawSlot( short slot, ItemStack itemStack ) {
        setItem( slot, itemStack );
    }

    @Override
    public ItemStack getResult() {
        return getItem( 2 );
    }

    @Override
    public ItemStack getFuel() {
        return getItem( 1 );
    }

    @Override
    public ItemStack getSmelting() {
        return getItem( 0 );
    }

    @Override
    public void setFuel( ItemStack itemStack ) {
        setItem( 1, itemStack );
    }

    @Override
    public void setResult( ItemStack itemStack ) {
        setItem( 2, itemStack );
    }

    @Override
    public void setSmelting( ItemStack itemStack ) {
        setItem( 0, itemStack );
    }

    @Override
    public InventoryType getType() {
        return InventoryType.FURNACE;
    }

    @Override
    public Furnace getHolder() {
        return this.furnace;
    }
}
