package me.lukas81298.flexmc.inventory;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 08.08.2017
 */
@RequiredArgsConstructor
@Getter
public enum Material {

    AIR( 0, false, false, true, false),
    STONE( 1 ),
    GRASS( 2 ),
    DIRT( 3 ),
    COBBLESTONE( 4 ),
    WOOD_PLANK( 5 ),
    SAPLING( 6, false, true, true, false ),
    BEDROCK( 7 ),
    FLOWING_WATER( 8, false, false, true, false ),
    STILL_WATER( 9, false, false, true, false ),
    FLOWING_LAVA( 10, false, false, true, false ),
    STILL_LAVA( 11, false, false, true, false ),
    SAND( 12 ),
    GRAVEL( 13 ),
    GOLD_ORE( 14 ),
    IRON_ORE( 15 ),
    COAL_ORE( 16 ),
    LOG( 17 ),
    LEAVES( 18 ),
    SPONGE( 19 ),
    GLASS( 20, true, false, true, true ),
    LAPIS_ORE( 21 ),
    LAPIS_BLOCK( 22 ),
    DISPENSER( 23 ),
    SANDSTONE( 24 ),
    NOTE_BLOCK( 25 );

    private final int id;
    private final boolean block;
    private final boolean item;
    private final boolean translucent;
    private final boolean solid;

    Material( int id ) {
        this( id, true, false, false, true );
    }

    private final static TIntObjectMap<Material> byId = new TIntObjectHashMap<>();

    static {
        for ( Material material : values() ) {
            byId.put( material.id, material );
        }
    }

    public static Material getById( int id ) {
        return byId.get( id );
    }

    public static Material getByName( String input ) {
        for ( Material material : values() ) {
            if( material.name().equalsIgnoreCase( input ) || material.name().replace( "_", " " ).equalsIgnoreCase( input ) || Integer.toString( material.id ).equals( input ) ) {
                return material;
            }
        }
        return null;
    }
}
