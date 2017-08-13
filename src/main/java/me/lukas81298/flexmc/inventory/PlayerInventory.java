package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.message.play.server.MessageS16SetSlot;
import me.lukas81298.flexmc.io.message.play.server.MessageS3FEntityEquipment;
import me.lukas81298.flexmc.util.crafting.CraftingInput;
import me.lukas81298.flexmc.util.crafting.Recipe;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
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
public class PlayerInventory extends FlexInventory implements CraftingInput, org.bukkit.inventory.PlayerInventory {

    private final ItemStack[] armor = new ItemStack[4];

    @Setter
    @Getter
    private volatile ItemStack itemOnCursor = null;
    @Setter
    @Getter
    private volatile ItemStack itemOffHand = null;
    private ItemStack[] craftingSlots = new ItemStack[5];

    public PlayerInventory( FlexPlayer player ) {
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
    public synchronized boolean click( FlexPlayer player, short slot, byte button, int mode, ItemStack itemStack ) {
        if ( slot < 0 ) { // todo handle -999
            return false;
        }
        ItemStack currentlyInSlot = this.getItemFromRawSlot( slot );
        if ( mode != 2 && ( mode != 4 && !( button == 1 || button == 2 ) ) ) {
            /*if ( !ItemStack.( currentlyInSlot, itemStack ) ) {
                System.out.println( "Slots did not match: " + currentlyInSlot + " " + itemStack );
                return false;
            }*/
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
                        if( slot == 0 ) {
                            for ( int i = 1; i < craftingSlots.length; i++ ) {
                                ItemStack stack = craftingSlots[ i ];
                                if( stack != null && stack.getType() != Material.AIR ) {
                                    stack.setAmount( stack.getAmount() - 1 );
                                    if( stack.getAmount() <= 0 ) {
                                        stack = null;
                                    }
                                    setRawSlot( (short) i, stack );
                                }
                            }
                        }
                        System.out.println( "Picked up " + itemOnCursor );
                        return true;
                    } else {
                        // empty slot clicked
                    }
                    // left click
                } else if ( button == 2 ) {
                    if ( itemOnCursor != null && itemOnCursor.getType() != Material.AIR ) {
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
                    setRawSlot( (short) 0, null );
                    System.out.println( "no recipe found" );
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
        return null;
    }

    @Override
    public ItemStack getChestplate() {
        return null;
    }

    @Override
    public ItemStack getLeggings() {
        return null;
    }

    @Override
    public ItemStack getBoots() {
        return null;
    }

    @Override
    public void setArmorContents( ItemStack[] itemStacks ) {

    }

    @Override
    public void setExtraContents( ItemStack[] itemStacks ) {

    }

    @Override
    public void setHelmet( ItemStack itemStack ) {

    }

    @Override
    public void setChestplate( ItemStack itemStack ) {

    }

    @Override
    public void setLeggings( ItemStack itemStack ) {

    }

    @Override
    public void setBoots( ItemStack itemStack ) {

    }

    @Override
    public ItemStack getItemInMainHand() {
        return null;
    }

    @Override
    public void setItemInMainHand( ItemStack itemStack ) {

    }

    @Override
    public ItemStack getItemInOffHand() {
        return null;
    }

    @Override
    public void setItemInOffHand( ItemStack itemStack ) {

    }

    @Override
    public ItemStack getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand( ItemStack itemStack ) {

    }

    @Override
    public int getHeldItemSlot() {
        return 0;
    }

    @Override
    public void setHeldItemSlot( int i ) {

    }

    @Override
    public int clear( int i, int i1 ) {
        return 0;
    }

    @Override
    public HumanEntity getHolder() {
        return null;
    }

    @Override
    public boolean hasInputItems( ItemStack itemStack, int amount ) {
        int count = 0;
        int i = 0;
        for ( ItemStack craftingSlot : craftingSlots ) {
            if( i != 0 ) {
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
            if( i != 0 ) {
                if ( craftingSlot != null && craftingSlot.getType() != Material.AIR ) {
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

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYER;
    }

}
