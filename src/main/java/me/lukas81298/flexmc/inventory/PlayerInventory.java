package me.lukas81298.flexmc.inventory;

import me.lukas81298.flexmc.entity.Player;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class PlayerInventory extends Inventory {

    private final ItemStack[] armor = new ItemStack[4];

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

    public ItemStack[] getArmorContents() {
        ItemStack[] copy = new ItemStack[4];
        System.arraycopy( armor, 0, copy, 0 , 4 );
        return copy;
    }
}
