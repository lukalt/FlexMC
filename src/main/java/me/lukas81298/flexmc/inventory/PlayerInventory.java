package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.play.server.MessageS16SetSlot;
import me.lukas81298.flexmc.io.message.play.server.MessageS3FEntityEquipment;
import me.lukas81298.flexmc.util.crafting.CraftingInput;
import me.lukas81298.flexmc.util.crafting.Recipe;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class PlayerInventory extends Inventory implements CraftingInput {

    private final ItemStack[] armor = new ItemStack[4];

    @Setter
    @Getter
    private volatile ItemStack itemOnCursor = null;
    @Setter
    @Getter
    private volatile ItemStack itemOffHand = null;
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
    public synchronized boolean click( Player player, short slot, byte button, int mode, ItemStack itemStack ) {
        if ( slot < 0 ) { // todo handle -999
            return false;
        }
        ItemStack currentlyInSlot = this.getItemFromRawSlot( slot );
        if ( mode != 2 && ( mode != 4 && !( button == 1 || button == 2 ) ) ) {
            if ( !ItemStack.isEqual( currentlyInSlot, itemStack ) ) {
                System.out.println( "Slots did not match: " + currentlyInSlot + " " + itemStack );
                return false;
            }
        }

        switch ( mode ) {
            case 0:
                if ( button == 0 || ( button == 1 && currentlyInSlot != null && currentlyInSlot.getAmount() == 1 ) ) {
                    if ( currentlyInSlot == null && itemOnCursor != null && !itemStack.isEmpty() ) {
                        setRawSlot( slot, itemOnCursor );
                        System.out.println( "dropped item " + itemOnCursor );
                        itemOnCursor = null;
                        return true;
                    } else if ( currentlyInSlot != null && !currentlyInSlot.isEmpty() && itemOnCursor == null ) {
                        itemOnCursor = currentlyInSlot;
                        setRawSlot( slot, null );
                        System.out.println( "Picked up " + itemOnCursor );
                        return true;
                    } else {
                        // empty slot clicked
                    }
                    // left click
                } else if ( button == 2 ) {
                    if ( itemOnCursor != null && !itemOnCursor.isEmpty() ) {
                        // todo i gave up here
                    }
                }
                return false;
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
                if ( button > 8 ) {
                    return false;
                }
                ItemStack target = getItem( button );
                setRawSlot( slot, target );
                setItem( button, currentlyInSlot );
                break;

            case 3:
                // ignored for now
                break;
            case 4:
                switch ( button ) {
                    case 0:
                        if ( currentlyInSlot != null && !currentlyInSlot.isEmpty() ) {
                            currentlyInSlot.setAmount( currentlyInSlot.getAmount() - 1 );
                            player.dropItem( new ItemStack( currentlyInSlot.getType(), 1, currentlyInSlot.getDamage() ) );
                            if ( currentlyInSlot.getAmount() <= 0 ) {
                                currentlyInSlot = null;
                            }
                            setRawSlot( slot, currentlyInSlot );
                            return true;
                        }
                        break;
                    case 1:
                        if ( currentlyInSlot != null && !currentlyInSlot.isEmpty() ) {
                            player.dropItem( new ItemStack( currentlyInSlot.getType(), currentlyInSlot.getAmount(), currentlyInSlot.getDamage() ) );
                            setRawSlot( slot, null );
                            return true;
                        }
                        break;
                    case 2:
                    case 3:
                        // no action here
                        break;
                }
                break;
            case 5:
                TextComponent textComponent = new TextComponent( "Item dragging is not supported yet!" );
                textComponent.setColor( ChatColor.RED );
                player.sendMessage( textComponent );
                break;
            case 6:

                break;
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
        if ( slot < 5 ) {
            // crafting
            craftingSlots[slot] = itemStack;
            if( slot > 0 ) {
                Recipe recipe = Flex.getServer().getRecipeManager().getRecipe( this );
                if ( recipe != null ) {
                    setRawSlot( (short) 0, recipe.getResult() );
                    System.out.println( "Recipe found" );
                } else {
                    System.out.println( "no recipe found" );
                }
            }
        } else if ( slot < 9 ) {
            armor[slot - 5] = itemStack;
            for ( Player viewer : viewers ) {
                for ( Player player : viewer.getWorld().getPlayers() ) {
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
        for ( Player viewer : viewers ) {
            viewer.getConnectionHandler().sendMessage( new MessageS16SetSlot( (byte) 0, slot, itemStack == null ? ItemStack.AIR : itemStack ) );
        }
    }

    public ItemStack[] getArmorContents() {
        ItemStack[] copy = new ItemStack[4];
        System.arraycopy( armor, 0, copy, 0, 4 );
        return copy;
    }

    @Override
    public boolean hasInputItems( ItemStack itemStack, int amount ) {
        int count = 0;
        int i = 0;
        for ( ItemStack craftingSlot : craftingSlots ) {
            if( i != 0 ) {
                if ( craftingSlot != null && !craftingSlot.isEmpty() ) {
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
            if( i != 0 ) {
                if ( craftingSlot != null && !craftingSlot.isEmpty() ) {
                    items.add( craftingSlot );
                }
            }
            i++;
        }
        return items;
    }

    @Override
    public int getInputSize() {
        return 4;
    }
}
