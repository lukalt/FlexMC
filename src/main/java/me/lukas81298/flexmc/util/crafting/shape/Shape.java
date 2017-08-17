package me.lukas81298.flexmc.util.crafting.shape;

import gnu.trove.map.TCharObjectMap;
import me.lukas81298.flexmc.inventory.ItemStackConstants;
import me.lukas81298.flexmc.util.crafting.CraftingInput;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class Shape implements RecipeShape {

    private final TCharObjectMap<ItemStack> match;
    private final char[][] shape;

    public Shape( TCharObjectMap<ItemStack> match, String... lines ) {
        this.match = match;
        this.match.putIfAbsent( '0', null );
        char[][] array = new char[ lines.length ][];
        int lastLength = -1;
        for( int i = 0; i < lines.length; i++ ) {
            array[ i ] = lines[ i ].toCharArray();
            if( lastLength != -1 && lastLength != lines[ i ].length() ) {
                   throw new IllegalArgumentException( "Not equal length of lines" );
            }
            lastLength = lines[ i ].length();
            for( char c : lines[ i ].toCharArray() ) {
                if( !match.containsKey( c ) ) {
                    throw new IllegalArgumentException( "Invalid char " + c );
                }
            }
        }
        this.shape = array;
    }

    @Override
    public boolean matches( CraftingInput input ) {
        if( this.getWidth() > input.getInputWidth() || this.getHeight() > input.getInputHeight() ) {
            return false;
        }
        ItemStack[][] array = this.toItemStackArray();

        ItemStack[][] inputArray = input.getInputArray();
    //    System.out.println( "Matching " + ItemStackConstants.toString( inputArray ) );
    //    System.out.println( "Expected " + ItemStackConstants.toString( array ) );
        for( int i = 0; i <= input.getInputWidth() - this.getWidth(); i++ ) {
            for( int j = 0; j <= input.getInputHeight() - this.getHeight(); j++ ) {
             //   System.out.println( "Matching for " + i + ", " + j );
                if( matchSubArray( array, inputArray, i, j ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchItemStack( ItemStack expect, ItemStack found ) {
    //    System.out.println( "Matching " + ItemStackConstants.toString( expect ) + ", " + ItemStackConstants.toString( found ) );
        return expect == found || expect != null && found != null && expect.getType() == found.getType() && ( expect.getDurability() == ItemStackConstants.IGNORE_DATA_VALUE || expect.getDurability() == found.getDurability() );
    }

    private boolean matchSubArray( ItemStack[][] shape, ItemStack[][] inputArray, int vi, int vj ) {
        for( int i = 0; i < shape.length; i++ )  {
            ItemStack[] l = shape[ i ];
            for( int j = 0; j < l.length; j++ ) {
                ItemStack found = l[ j ];
                ItemStack expected = inputArray[ i + vi ][ j + vj ];
                if( !matchItemStack( found, expected ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    private ItemStack[][] toItemStackArray() {
        ItemStack[][] array = new ItemStack[ this.shape.length ][];
        for( int i = 0; i < array.length; i++ ) {
            char[] b = shape[ i ];
            ItemStack[] r = new ItemStack[ b.length ];
            for( int k = 0; k < r.length; k++ ) {
                r[ k ] = match.get( b[ k ] );
            }
            array[ i ] = r;
        }
        return array;
    }

    @Override
    public int getWidth() {
        return this.shape.length;
    }

    @Override
    public int getHeight() {
        return this.shape[0].length;
    }
}
