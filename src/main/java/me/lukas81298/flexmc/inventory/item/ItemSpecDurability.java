package me.lukas81298.flexmc.inventory.item;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.inventory.ItemStack;

/**
 * @author lukas
 * @since 12.08.2017
 */
public class ItemSpecDurability extends ItemSpec {

    private final short maxDurability;

    public ItemSpecDurability( int type, final short maxDurability ) {
        super( type );
        this.maxDurability = maxDurability;
    }

    public ItemSpecDurability( int type, int maxDurability ) {
        this( type, (short) maxDurability );
    }

    @Override
    public ItemStack breakBlock( Player player, ItemStack itemStack ) {
        if( itemStack == null ) {
            return null;
        }
        itemStack.setDamage( (short) (itemStack.getDamage() + 1) );
        if( itemStack.getDamage() >= maxDurability ) {
            return null;
        }
        return itemStack;
    }
}
