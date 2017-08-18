package me.lukas81298.flexmc.inventory;

import com.google.common.collect.Iterables;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.inventory.crafting.CraftingInput;
import me.lukas81298.flexmc.inventory.crafting.Recipe;
import me.lukas81298.flexmc.io.message.play.server.MessageS16SetSlot;
import me.lukas81298.flexmc.io.message.play.server.MessageS3FEntityEquipment;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class FlexPlayerInventory extends FlexInventory implements CraftingInput, org.bukkit.inventory.PlayerInventory {

    private final ItemStack[] armor = new ItemStack[4];

    @Setter
    @Getter
    private volatile ItemStack itemOnCursor = null;
    @Setter
    @Getter
    private volatile ItemStack itemOffHand = null;
    private final ItemStack[] craftingSlots = new ItemStack[5];

    private final TIntSet draggingSlots = new TIntHashSet();
    @NonNull
    private DragType dragType = DragType.NONE;

    public FlexPlayerInventory( FlexPlayer player ) {
        super( 36, (byte) 0, "Inventory", "minecraft:crafting_table" );
        this.viewers.add( player );
    }

    @Override
    protected int getRawSlow( int virtualSlot ) {
        if ( virtualSlot < 9 ) {
            return 36 + virtualSlot;
        }
        return virtualSlot;
    }

    @Override
    public synchronized boolean click( FlexPlayer player, short slot, byte button, int mode, ItemStack itemStack ) {
        ItemStack currentlyInSlot = slot < 0 ? null : this.getItemFromRawSlot( slot );
        if ( mode != 2 && ( mode != 4 && !( button == 1 || button == 2 ) ) ) {
            if ( !ItemStackConstants.equals( currentlyInSlot, itemStack ) ) {
                System.out.println( "Slots did not match: " + ( currentlyInSlot == null ? ItemStackConstants.AIR : currentlyInSlot ) + " " + itemStack );
                return false;
            }
        }

        switch ( mode ) {
            case 0:
                if ( button == 0 || ( button == 1 && currentlyInSlot != null && currentlyInSlot.getAmount() == 1 ) ) {
                    if ( currentlyInSlot == null && itemOnCursor != null && itemOnCursor.getType() != Material.AIR ) {
                        setRawSlot( slot, itemOnCursor );
                        System.out.println( "dropped item " + itemOnCursor );
                        itemOnCursor = null;
                        return true;
                    } else if ( currentlyInSlot != null && currentlyInSlot.getType() != Material.AIR && itemOnCursor == null ) {
                        itemOnCursor = currentlyInSlot;
                        setRawSlot( slot, null );
                        if ( slot == 0 ) {
                            for ( int i = 1; i < craftingSlots.length; i++ ) {
                                ItemStack stack = craftingSlots[i];
                                if ( stack != null && stack.getType() != Material.AIR ) {
                                    stack.setAmount( stack.getAmount() - 1 );
                                    if ( stack.getAmount() <= 0 ) {
                                        stack = null;
                                    }
                                    setRawSlot( (short) i, stack );
                                }
                            }
                        }
                        System.out.println( "Picked up " + itemOnCursor );
                        return true;
                    } else if ( currentlyInSlot != null ) {
                        int remaining = getMaxStackSize() - currentlyInSlot.getAmount();
                        int add = Math.min( remaining, itemOnCursor.getAmount() );
                        currentlyInSlot.setAmount( currentlyInSlot.getAmount() + add );
                        itemOnCursor.setAmount( itemOnCursor.getAmount() - add );
                        if ( itemOnCursor.getAmount() <= 0 ) {
                            itemOnCursor = null;
                        }
                        setRawSlot( slot, currentlyInSlot );
                        System.out.println( "Added " + add + " to " + currentlyInSlot.toString() );
                        return true;
                    } else {
                        System.out.println( "dewrtz78" );
                        // empty slot clicked
                        return true;
                    }
                    // left click
                }
                return false;
            case 1:
                if( currentlyInSlot != null ) {

                }
                break;
            case 2:
                if ( button > 8 ) {
                    return false;
                }
                ItemStack target = getItem( button );
                setRawSlot( slot, target );
                setItem( button, currentlyInSlot );
                return true;
            case 3:
                // ignored for now
                break;
            case 4:
                switch ( button ) {
                    case 0:
                        if ( currentlyInSlot != null && currentlyInSlot.getType() != Material.AIR ) {
                            currentlyInSlot.setAmount( currentlyInSlot.getAmount() - 1 );
                            player.dropItem( new ItemStack( currentlyInSlot.getType(), 1, currentlyInSlot.getDurability() ) );
                            if ( currentlyInSlot.getAmount() <= 0 ) {
                                currentlyInSlot = null;
                            }
                            setRawSlot( slot, currentlyInSlot );
                            return true;
                        }
                        break;
                    case 1:
                        if ( currentlyInSlot != null && currentlyInSlot.getType() != Material.AIR ) {
                            player.dropItem( new ItemStack( currentlyInSlot.getType(), currentlyInSlot.getAmount(), currentlyInSlot.getDurability() ) );
                            setRawSlot( slot, null );
                        }
                        break;
                }
                return true;
            case 5:
                switch ( button ) {
                    case 0:
                        dragType = DragType.LEFT;
                        break;
                    case 4:
                        dragType = DragType.RIGHT;
                        break;
                    case 8:
                        dragType = DragType.MIDDLE;
                        break;
                    case 1:
                        if( dragType != DragType.LEFT ) {
                            return false;
                        }
                        draggingSlots.add( slot );
                        break;
                    case 5:
                        if( dragType != DragType.RIGHT ) {
                            return false;
                        }
                        draggingSlots.add( slot );
                        break;
                    case 9:
                        if( dragType != DragType.MIDDLE ) {
                            return false;
                        }
                        draggingSlots.add( slot );
                        break;
                    case 2:
                        if( dragType != DragType.LEFT ) {
                            return false;
                        }
                        dragType = DragType.NONE;

                        if( !draggingSlots.isEmpty() ) {
                            int perSlot = itemOnCursor.getAmount() / draggingSlots.size();
                            int remaining = itemOnCursor.getAmount() % draggingSlots.size();

                            draggingSlots.forEach( (s) -> {
                                ItemStack clone = itemOnCursor.clone();
                                clone.setAmount( perSlot );
                                setRawSlot( (short) s, clone );
                                return true;
                            } );

                            itemOnCursor.setAmount( remaining );
                        }

                        draggingSlots.clear();
                        break;
                    case 6:
                        if( dragType != DragType.RIGHT ) {
                            return false;
                        }
                        dragType = DragType.NONE;

                        if( !draggingSlots.isEmpty() ) {
                            int perSlot = draggingSlots.size();
                            int remaining = itemOnCursor.getAmount() - perSlot;

                            draggingSlots.forEach( (s) -> {
                                ItemStack clone = itemOnCursor.clone();
                                clone.setAmount( 1 );
                                setRawSlot( (short) s, clone );
                                return true;
                            } );

                            itemOnCursor.setAmount( remaining );
                            if( itemOnCursor.getAmount() == 0 ) {
                                itemOnCursor = null;
                            }
                        }
                        draggingSlots.clear();
                        break;
                    case 10:
                        if( dragType != DragType.MIDDLE ) {
                            return false;
                        }
                        return false;
                    default:
                        return false;
                }
                return true;
            case 6:
                if( currentlyInSlot != null && currentlyInSlot.getType() != Material.AIR && currentlyInSlot.getAmount() < 64 ) {
                    int s = -1;
                    if( slot >= 36 && slot <= 44 ) {
                        s = slot - 36;
                    } else if( slot >= 9 && slot <= 35 ) {
                        s = slot;
                    }
                    int i = 0;
                    for( ItemStack stack : getContents() ) {
                        if( currentlyInSlot.getAmount() >= 64 ) {
                            break;
                        }
                        if( s != i && stack != null && stack.isSimilar( currentlyInSlot ) ) {
                            int amount = Math.min( stack.getAmount(), 64 - currentlyInSlot.getAmount() );
                            stack.setAmount( stack.getAmount() - amount );
                            currentlyInSlot.setAmount( currentlyInSlot.getAmount() + amount );
                            setItem( i, stack );
                        }
                        i++;
                    }
                    setRawSlot( slot, currentlyInSlot );
                    System.out.println( "Collected to slot " + ItemStackConstants.toString( currentlyInSlot ) );
                    return true;
                }
        }
        return false;
    }

    private ItemStack getItemFromRawSlot( int slot ) {
        if ( slot < 5 ) {
            return craftingSlots[slot];
        } else if ( slot < 9 ) {
            return armor[slot - 5];
        } else if ( slot == 45 ) {
            return itemOffHand;
        } else if ( slot > 35 ) {
            return getItem( slot - 36 );
        } else {
            return getItem( slot );
        }
    }

    private int convertArmorToEquipmentIndex( int i ) {
        switch ( i ) {
            case 5:
                return 5;
            case 6:
                return 4;
            case 7:
                return 3;
            case 8:
                return 2;
        }
        return 0;
    }

    private void setRawSlot( short slot, ItemStack itemStack ) {
        if( slot == -999 ) {
            return;
        }
        if ( slot < 5 ) {
            // crafting
            craftingSlots[slot] = itemStack;
            if ( slot > 0 ) {
                Recipe recipe = Flex.getServer().getRecipeManager().getRecipe( this );
                if ( recipe != null ) {
                    setRawSlot( (short) 0, recipe.getResult() );
                } else {
                    setRawSlot( (short) 0, null );
                }
            }
        } else if ( slot < 9 ) {
            armor[slot - 5] = itemStack;
            for ( FlexPlayer viewer : viewers ) {
                for ( FlexPlayer player : viewer.getWorld().getPlayerSet() ) {
                    if ( !player.equals( viewer ) ) {
                        player.getConnectionHandler().sendMessage( new MessageS3FEntityEquipment( viewer.getEntityId(), convertArmorToEquipmentIndex( slot ), itemStack ) );
                    }
                }
            }
        } else if ( slot == 45 ) {
            itemOffHand = itemStack;
        } else {
            int virtualSlot;
            if ( slot >= 36 && slot <= 44 ) {
                virtualSlot = slot - 36;
            } else {
                virtualSlot = slot;
            }
            System.out.println( "setting " + itemStack + " to " + virtualSlot );
            setItem( virtualSlot, itemStack );
            return;
        }
        for ( FlexPlayer viewer : viewers ) {
            viewer.getConnectionHandler().sendMessage( new MessageS16SetSlot( (byte) 0, slot, itemStack == null ? ItemStackConstants.AIR : itemStack ) );
        }
    }

    public synchronized ItemStack[] getRawSlotsArray() {
        ItemStack[] r = new ItemStack[ 46 ];
        System.arraycopy( craftingSlots, 0, r, 0 , 5 );
        System.arraycopy( armor, 0, r, 5, 4 );
        ItemStack[] contents = this.getContents();
        System.arraycopy( contents, 9, r, 9, 9 * 3 );
        System.arraycopy( contents, 0, r, 36, 9 );
        r[ 45 ] = itemOffHand;
        return r;
    }

    public ItemStack[] getArmorContents() {
        ItemStack[] copy = new ItemStack[4];
        System.arraycopy( armor, 0, copy, 0, 4 );
        return copy;
    }

    @Override
    public ItemStack[] getExtraContents() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getHelmet() {
        return armor[0];
    }

    @Override
    public ItemStack getChestplate() {
        return armor[1];
    }

    @Override
    public ItemStack getLeggings() {
        return armor[2];
    }

    @Override
    public ItemStack getBoots() {
        return armor[3];
    }

    @Override
    public void setArmorContents( ItemStack[] itemStacks ) {

    }

    @Override
    public void setExtraContents( ItemStack[] itemStacks ) {

    }

    @Override
    public void setHelmet( ItemStack itemStack ) {
        setRawSlot( (short) 5, itemStack );
    }

    @Override
    public void setChestplate( ItemStack itemStack ) {
        setRawSlot( (short) 6, itemStack );
    }

    @Override
    public void setLeggings( ItemStack itemStack ) {
        setRawSlot( (short) 7, itemStack );
    }

    @Override
    public void setBoots( ItemStack itemStack ) {
        setRawSlot( (short) 8, itemStack );
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getItemInHand();
    }

    @Override
    public void setItemInMainHand( ItemStack itemStack ) {
        this.setItemInHand( itemStack );
    }

    @Override
    public ItemStack getItemInOffHand() {
        return itemOffHand;
    }

    @Override
    public void setItemInOffHand( ItemStack itemStack ) {
        itemOffHand = itemStack;
        setRawSlot( (short) 45, itemStack );
    }

    @Override
    public ItemStack getItemInHand() {
        return getItem( getOwner().getHeldItemSlot() );
    }

    @Override
    public void setItemInHand( ItemStack itemStack ) {
        setItem( getOwner().getHeldItemSlot(), itemStack );
    }

    private FlexPlayer getOwner() {
        FlexPlayer t = Iterables.getFirst( viewers, null );
        if ( t == null ) {
            throw new NullPointerException();
        }
        return t;
    }

    @Override
    public int getHeldItemSlot() {
        return getOwner().getHeldItemSlot();
    }

    @Override
    public void setHeldItemSlot( int i ) {
        getOwner().setHeldItemSlot( i );
    }

    @Override
    public int clear( int i, int i1 ) {
        return 0;
    }

    @Override
    public HumanEntity getHolder() {
        return getOwner();
    }

    @Override
    public boolean hasInputItems( ItemStack itemStack, int amount ) {
        int count = 0;
        int i = 0;
        for ( ItemStack craftingSlot : craftingSlots ) {
            if ( i != 0 ) {
                if ( craftingSlot != null && craftingSlot.getType() != Material.AIR ) {
                    if ( craftingSlot.isSimilar( itemStack ) ) {
                        count++;
                    }
                }
            }
            i++;
        }
        return count == amount;
    }

    @Override
    public Collection<ItemStack> getInputs() {
        List<ItemStack> items = new ArrayList<>();
        int i = 0;
        for ( ItemStack craftingSlot : craftingSlots ) {
            if ( i != 0 ) {
                if ( craftingSlot != null && craftingSlot.getType() != Material.AIR ) {
                    items.add( craftingSlot );
                }
            }
            i++;
        }
        return items;
    }

    @Override
    public ItemStack[][] getInputArray() {
        synchronized ( craftingSlots ) {
            return new ItemStack[][]{ new ItemStack[]{ craftingSlots[1], craftingSlots[2] }, new ItemStack[]{ craftingSlots[3], craftingSlots[4] } };
        }
    }

    @Override
    public int getInputSize() {
        return getInputHeight() * getInputWidth();
    }

    @Override
    public int getInputHeight() {
        return 2;
    }

    @Override
    public int getInputWidth() {
        return 2;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYER;
    }

    public void resetCrafting() {
        synchronized ( this.craftingSlots ) {
            for ( int i = 0; i < craftingSlots.length; i++ ) {
                ItemStack item = craftingSlots[ i ];
                if( item != null && i > 0 ) {
                    if( item.getType() != Material.AIR ) {
                        getOwner().getWorld().dropItem( getOwner().getLocation(), item );
                    }
                    this.setRawSlot( (short) i, null );
                }
            }
        }

    }
}
