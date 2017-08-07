package me.lukas81298.flexmc.inventory;

import me.lukas81298.flexmc.entity.Player;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class PlayerInventory extends Inventory {

    public PlayerInventory( Player player ) {
        super( 36, (byte) 0, "Inventory" );
        this.viewers.add( player );
    }

    @Override
    protected int getRawSlow( int virtualSlot ) {
        if( virtualSlot < 9 ) {
            return 36 + virtualSlot;
        }
        return virtualSlot;
    }
}
