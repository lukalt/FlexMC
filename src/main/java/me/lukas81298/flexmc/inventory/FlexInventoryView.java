package me.lukas81298.flexmc.inventory;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 17.08.2017
 */
@RequiredArgsConstructor
public class FlexInventoryView extends InventoryView {

    @Getter
    private final FlexPlayer player;

    @Getter
    @Setter
    private volatile FlexInventory topInventory;

    private final TIntSet draggingSlots = new TIntHashSet();
    @NonNull
    private volatile DragType dragType = DragType.NONE;

    @Override
    public FlexInventory getBottomInventory() {
        return this.player.getInventory();
    }

    @Override
    public synchronized InventoryType getType() {
        return this.topInventory == null ? InventoryType.PLAYER : this.topInventory.getType();
    }

    public synchronized boolean click( int windowId, short slot, byte button, int mode, ItemStack itemStack ) {
        System.out.println( windowId + " " + slot + " " + button + " " + mode + " " + itemStack );
        FlexInventory clickedInventory;
        if( windowId == 0 ) {
            clickedInventory = this.player.getInventory();
        } else if( topInventory != null && topInventory.getWindowId() == windowId ){
            if( slot != -999 && slot < topInventory.getSpecialSlots() ) {
                clickedInventory = this.topInventory;
            } else {
                clickedInventory = this.player.getInventory();
            }
        } else {
            throw new IllegalArgumentException( "Clicked unknown window " + windowId );
        }

        System.out.println( "Clicked inventory: " + clickedInventory.getName() );
        ItemStack currentlyInSlot = slot < 0 ? null : clickedInventory.getItemFromRawSlot( slot, topInventory == null ? getBottomInventory().getSpecialSlots() : topInventory.getSpecialSlots() );

        if ( mode != 2 && ( mode != 4 && !( button == 1 || button == 2 ) ) ) {
            if ( !ItemStackConstants.equals( currentlyInSlot, itemStack ) ) {
                System.out.println( "Slots did not match: " + ( currentlyInSlot == null ? ItemStackConstants.AIR : currentlyInSlot ) + " " + itemStack );
                return false;
            }
        }
        FlexPlayerInventory playerInventory = this.player.getInventory();
        switch ( mode ) {
            case 0:
                if ( button == 0 || ( button == 1 && currentlyInSlot != null && currentlyInSlot.getAmount() == 1 ) ) {
                    ItemStack itemOnCursor = playerInventory.getItemOnCursor();
                    if ( currentlyInSlot == null && itemOnCursor != null && itemOnCursor.getType() != Material.AIR ) {
                        clickedInventory.setRawSlot( slot, itemOnCursor );
                        System.out.println( "dropped item " + itemOnCursor );
                        playerInventory.setItemOnCursor( null );
                        return true;
                    } else if ( currentlyInSlot != null && currentlyInSlot.getType() != Material.AIR && itemOnCursor == null ) {
                        playerInventory.setItemOnCursor( currentlyInSlot );
                        clickedInventory.setRawSlot( slot, null );
                        clickedInventory.handleSlotClick( slot );
                        System.out.println( "Picked up " + currentlyInSlot);
                        return true;
                    } else if ( currentlyInSlot != null ) {
                        int remaining = playerInventory.getMaxStackSize() - currentlyInSlot.getAmount();
                        int add = Math.min( remaining, itemOnCursor.getAmount() );
                        currentlyInSlot.setAmount( currentlyInSlot.getAmount() + add );
                        itemOnCursor.setAmount( itemOnCursor.getAmount() - add );
                        if ( itemOnCursor.getAmount() <= 0 ) {
                            playerInventory.setItemOnCursor( null );
                        }
                        playerInventory.setItemOnCursor( itemOnCursor );
                                clickedInventory.setRawSlot( slot, currentlyInSlot );
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
                clickedInventory.setRawSlot( slot, target );
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
                            clickedInventory.setRawSlot( slot, currentlyInSlot );
                            return true;
                        }
                        break;
                    case 1:
                        if ( currentlyInSlot != null && currentlyInSlot.getType() != Material.AIR ) {
                            player.dropItem( new ItemStack( currentlyInSlot.getType(), currentlyInSlot.getAmount(), currentlyInSlot.getDurability() ) );
                            clickedInventory.setRawSlot( slot, null );
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
                            ItemStack itemOnCursor = playerInventory.getItemOnCursor();
                            int perSlot = itemOnCursor.getAmount() / draggingSlots.size();
                            int remaining = itemOnCursor.getAmount() % draggingSlots.size();

                            draggingSlots.forEach( (s) -> {
                                ItemStack clone = itemOnCursor.clone();
                                clone.setAmount( perSlot );
                                clickedInventory.setRawSlot( (short) s, clone );
                                return true;
                            } );

                            itemOnCursor.setAmount( remaining );
                            playerInventory.setItemOnCursor( itemOnCursor );
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
                            ItemStack itemOnCursor = playerInventory.getItemOnCursor();
                            int remaining = itemOnCursor.getAmount() - perSlot;

                            draggingSlots.forEach( (s) -> {
                                ItemStack clone = itemOnCursor.clone();
                                clone.setAmount( 1 );
                                clickedInventory.setRawSlot( (short) s, clone );
                                return true;
                            } );

                            itemOnCursor.setAmount( remaining );
                            if( itemOnCursor.getAmount() == 0 ) {
                                playerInventory.setItemOnCursor( null );
                            } else {
                                playerInventory.setItemOnCursor( itemOnCursor );
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
                    for( ItemStack stack : clickedInventory.getContents() ) {
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
                    clickedInventory.setRawSlot( slot, currentlyInSlot );
                    System.out.println( "Collected to slot " + ItemStackConstants.toString( currentlyInSlot ) );
                    return true;
                }
        }
        return false;
    }
}
