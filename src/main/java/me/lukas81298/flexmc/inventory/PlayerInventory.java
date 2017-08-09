package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.play.server.MessageS16SetSlot;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class PlayerInventory extends Inventory {

    private final ItemStack[] armor = new ItemStack[4];

    @Setter @Getter private volatile ItemStack itemOnCursor = null;
    @Setter @Getter private volatile ItemStack itemOffHand = null;
    private ItemStack[] craftingSlots = new ItemStack[5];

    public PlayerInventory( Player player ) {
        super( 36, (byte) 0, "Inventory" );
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
    public synchronized void click( short slot, byte button, short action, int mode, ItemStack itemStack ) {
        if( slot < 0 ) { // todo handle -999
            return;
        }
        ItemStack currentlyInSlot = this.getItemFromRawSlot( slot );
        if( !ItemStack.isEqual( currentlyInSlot, itemStack ) ) {
            System.out.println( "Slots did not match: " + currentlyInSlot + " " + itemStack );
            return;
        }
        switch ( mode ) {
            case 0:
                if( button == 0 || ( button == 1 && currentlyInSlot != null && currentlyInSlot.getAmount() == 1 ) ) {
                    if( currentlyInSlot == null && itemOnCursor != null && !itemStack.isEmpty() ) {
                        setRawSlot( slot, itemOnCursor );
                        System.out.println( "dropped item " + itemOnCursor );
                        itemOnCursor = null;
                    } else if( currentlyInSlot != null && !currentlyInSlot.isEmpty() && itemOnCursor == null ) {
                        itemOnCursor = currentlyInSlot;
                        setRawSlot( slot, null );
                        System.out.println( "Picked up " + itemOnCursor );
                    } else {
                        // empty slot clicked
                    }
                    // left click
                } else if( button == 2 ) {
                    if( itemOnCursor != null && !itemOnCursor.isEmpty() ) {
                       // todo i gave up here
                    }
                }
                break;
            case 1:
                switch ( button ) {
                    case 0:
                        // left shift click
                        break;
                    case 1:
                        // right shift click
                        break;
                }
                break;
            case 2:
                if( button > 8 ) {
                    return;
                }
                break;

            case 3:

                break;

            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
        }
    }

    private ItemStack getItemFromRawSlot( int slot ) {
        if( slot < 5 ) {
            return craftingSlots[ slot ];
        } else if( slot < 9 ) {
            return armor[ slot - 5 ];
        } else if( slot == 45 ) {
            return itemOffHand;
        } else if( slot > 35 ) {
            return getItem( slot - 36 );
        } else {
            return getItem( slot );
        }
    }

    private void setRawSlot( short slot, ItemStack itemStack ) {
        if( slot < 5  ) {
            // crafting output
            craftingSlots[ slot ] = itemStack;
        } else if( slot < 9 ) {
            armor[ slot - 5 ] = itemStack;
        } else if( slot == 45 ) {
            itemOffHand = itemStack;
        } else {
            int virtualSlot;
            if( slot >= 36 && slot <= 44 ) {
                virtualSlot = slot - 36;
            } else {
                virtualSlot = slot;
            }
            setItem( virtualSlot, itemStack );
            return;
        }
        for ( Player viewer : viewers ) {
            viewer.getConnectionHandler().sendMessage( new MessageS16SetSlot( (byte) 0, slot, itemStack == null ? ItemStack.AIR : itemStack ) );
        }
    }

    public ItemStack[] getArmorContents() {
        ItemStack[] copy = new ItemStack[4];
        System.arraycopy( armor, 0, copy, 0 , 4 );
        return copy;
    }
}
