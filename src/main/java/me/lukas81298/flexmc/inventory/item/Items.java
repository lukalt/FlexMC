package me.lukas81298.flexmc.inventory.item;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author lukas
 * @since 12.08.2017
 */
public class Items {

    private final static TIntObjectMap<ItemSpec> registeredItems = new TIntObjectHashMap<>();

    public synchronized static void initItems() {
        register( new ItemSpecDurability( 270, 60 ) );
        register( new ItemSpecDurability( 274, 132 ) );
        register( new ItemSpecDurability( 257, 251 ) );
        register( new ItemSpecDurability( 285, 33 ) );
        register( new ItemSpecDurability( 278, 1562 ) );

        register( new ItemSpecDurability( 269, 60 ) );
        register( new ItemSpecDurability( 273, 132 ) );
        register( new ItemSpecDurability( 256, 251 ) );
        register( new ItemSpecDurability( 284, 33 ) );
        register( new ItemSpecDurability( 277, 1562 ) );

        register( new ItemSpecDurability( 271, 60 ) );
        register( new ItemSpecDurability( 275, 132 ) );
        register( new ItemSpecDurability( 258, 251 ) );
        register( new ItemSpecDurability( 279, 33 ) );
        register( new ItemSpecDurability( 286, 1562 ) );
    }

    private static void register( ItemSpec spec ) {
        registeredItems.put( spec.getType(), spec );
    }

    public static ItemSpec getItemSpec( int type ) {
        return registeredItems.get( type );
    }
}
