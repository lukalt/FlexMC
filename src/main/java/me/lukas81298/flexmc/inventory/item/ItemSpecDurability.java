package me.lukas81298.flexmc.inventory.item;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.inventory.ItemStack;

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
    public ItemStack breakBlock( FlexPlayer player, ItemStack itemStack ) {
        if( itemStack == null ) {
            return null;
        }
        itemStack.setDurability( (short) (itemStack.getDurability() + 1) );
        if( itemStack.getDurability() >= maxDurability ) {
            return null;
        }
        return itemStack;
    }
}
