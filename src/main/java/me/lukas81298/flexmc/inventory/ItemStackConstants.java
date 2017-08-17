package me.lukas81298.flexmc.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class ItemStackConstants {

    public static final ItemStack AIR = new ItemStack( Material.AIR );
    public static final short IGNORE_DATA_VALUE = Short.MIN_VALUE;

    public static String toString( ItemStack[][] f ) {
        List<String> s = new ArrayList<>();
        for ( ItemStack[] t : f ) {
            s.add( toString( t ) );
        }
        return "[" + String.join( ",", s ) + "]";
    }

    public static String toString( ItemStack[] t ) {
        StringBuilder builder = new StringBuilder();
        builder.append( "[" );
        for ( int i = 0; i < t.length; i++ ) {
            builder.append( toString( t[i] ) );
            if ( i < t.length - 1 ) {
                builder.append( "," );
            }
        }
        builder.append( "]" );
        return builder.toString();
    }

    public static String toString( ItemStack t ) {
        return t == null ? "null" : ( t.getType().name() + ":" + t.getAmount() + ":" + t.getDurability() );
    }

    public static boolean equals( ItemStack a, ItemStack b ) {
        if( a == null ) {
            a = ItemStackConstants.AIR;
        }
        if( b == null ) {
            b = ItemStackConstants.AIR;
        }
        return a == b || ( a.getType() == b.getType() && a.getAmount() == b.getAmount() && a.getDurability() == b.getDurability() );
    }

}
