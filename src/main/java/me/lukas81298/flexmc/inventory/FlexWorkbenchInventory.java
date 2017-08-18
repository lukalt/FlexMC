package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.inventory.crafting.CraftingInput;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lukas
 * @since 17.08.2017
 */
@Getter
public class FlexWorkbenchInventory extends FlexInventory implements CraftingInventory, CraftingInput {

    private final FlexPlayer player;

    public FlexWorkbenchInventory( byte windowId, FlexPlayer player ) {
        super( 10, windowId, "Crafting", "minecraft:crafting_table", 10 );
        this.viewers.add( player );
        this.player = player;
    }

    @Override
    protected ItemStack getItemFromRawSlot( int slot ) {
        return this.getItem( slot );
    }

    @Override
    protected void setRawSlot( short slot, ItemStack itemStack ) {
        this.setItem( slot, itemStack );
        if( slot > 0 ) {
            me.lukas81298.flexmc.inventory.crafting.Recipe recipe = Flex.getServer().getRecipeManager().getRecipe( this );
            if( recipe != null ) {
                setItem( 0, recipe.getResult() );
            }
        }
    }

    @Override
    protected void handleSlotClick( int slot ) {
        if( slot == 0 ) {
            for( int i = 1; i < getSize(); i++  ) {
                ItemStack s = getItem( i );
                if( s != null && s.getType() != Material.AIR ) {
                    s.setAmount( s.getAmount() - 1 );
                    setItem( slot, s.getAmount() <= 0 ? null : s );
                }
            }
        }
    }

    @Override
    public ItemStack getResult() {
        return this.getItem( 0 );
    }

    @Override
    public ItemStack[] getMatrix() {
        ItemStack[] itemStacks = new ItemStack[9];
        for( int i = 0; i < 9; i++ ) {
            itemStacks[ i ] = this.getItem( 1 +  i );
        }
        return itemStacks;
    }


    @Override
    public void setResult( ItemStack itemStack ) {
        this.setItem( 0, itemStack );
    }

    @Override
    public void setMatrix( ItemStack[] itemStacks ) {
        int i = 0;
        for ( ItemStack itemStack : itemStacks ) {
            setItem( 1 + i, itemStack );
            i++;
        }
    }

    @Override
    public Recipe getRecipe() {
        throw new UnsupportedOperationException( "getRecipe ist not implemented" );
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CRAFTING;
    }

    @Override
    public InventoryHolder getHolder() {
        return player;
    }

    @Override
    public boolean hasInputItems( ItemStack itemStack, int amount ) {
        int i = 0;
        for ( ItemStack stack : this.getInputs() ) {
            if ( stack != null && stack.isSimilar( itemStack ) ) {
                i++;
            }
        }
        return i == amount;
    }

    @Override
    public Collection<ItemStack> getInputs() {
        List<ItemStack> inputs = new ArrayList<>();
        for ( ItemStack itemStack : this.getMatrix() ) {
            if ( itemStack != null && itemStack.getType() != Material.AIR ) {
                inputs.add( itemStack );
            }
        }
        return inputs;
    }

    @Override
    public ItemStack[][] getInputArray() {
        ItemStack[] matrix = this.getMatrix();
        return new ItemStack[][]{
                new ItemStack[]{ matrix[0], matrix[1], matrix[2] },
                new ItemStack[]{ matrix[3], matrix[4], matrix[5] },
                new ItemStack[]{ matrix[6], matrix[7], matrix[8] }
        };
    }

    @Override
    public int getInputSize() {
        return this.getInputHeight() * this.getInputWidth();
    }

    @Override
    public int getInputHeight() {
        return 3;
    }

    @Override
    public int getInputWidth() {
        return 3;
    }

}
