package me.lukas81298.flexmc.inventory.crafting.shape;

import gnu.trove.map.TCharObjectMap;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.inventory.ItemStackConstants;
import me.lukas81298.flexmc.inventory.crafting.CraftingInput;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 17.08.2017
 */
@RequiredArgsConstructor
public class CompiledShape implements RecipeShape {

    private final ItemStack[][] shape;

    public CompiledShape( TCharObjectMap<ItemStack> match, String... lines ) {
        match.putIfAbsent( '0', null );
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
        this.shape = new ItemStack[ array.length ][];
        for( int i = 0; i < array.length; i++ ) {
            char[] b = array[ i ];
            ItemStack[] r = new ItemStack[ b.length ];
            for( int k = 0; k < r.length; k++ ) {
                r[ k ] = match.get( b[ k ] );
            }
            shape[ i ] = r;
        }
    }

    @Override
    public boolean matches( CraftingInput input ) {
        if( this.getWidth() > input.getInputWidth() || this.getHeight() > input.getInputHeight() ) {
            return false;
        }

        ItemStack[][] inputArray = input.getInputArray();
    //    System.out.println( "Matching " + ItemStackConstants.toString( inputArray ) );
    //    System.out.println( "Expected " + ItemStackConstants.toString( array ) );
        for( int i = 0; i <= input.getInputWidth() - this.getWidth(); i++ ) {
            for( int j = 0; j <= input.getInputHeight() - this.getHeight(); j++ ) {
             //   System.out.println( "Matching for " + i + ", " + j );
                if( matchSubArray( inputArray, i, j ) ) {
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

    private boolean matchSubArray( ItemStack[][] inputArray, int vi, int vj ) {
        for( int i = 0; i < this.shape.length; i++ )  {
            ItemStack[] l = this.shape[ i ];
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
    @Override
    public int getWidth() {
        return this.shape.length;
    }

    @Override
    public int getHeight() {
        return this.shape[0].length;
    }

}
