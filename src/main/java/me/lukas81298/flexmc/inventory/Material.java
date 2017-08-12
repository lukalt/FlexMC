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
    LEAVES( 18, true, false, false, false ),
    SPONGE( 19 ),
    GLASS( 20, true, false, true, true ),
    LAPIS_ORE( 21 ),
    LAPIS_BLOCK( 22 ),
    DISPENSER( 23 ),
    SANDSTONE( 24 ),
    NOTE_BLOCK( 25 ),

    DEAD_BUSH( 31, false, false, true, false ),

    DIAMOND_ORE( 56 ),
    DIAMOND_BLOCK( 57 ),

    CACTUS( 81 ),

    IRON_SHOVEL( 256, false, true, true, false ),
    IRON_PICK_AXE( 257, false, true, true, false ),
    IRON_AXE( 258, false, true, true, false ),

    COAL( 263, false, true, true, false ),
    DIAMOND( 264, false, true, true, false ),
    IRON_INGOT( 265, false, true, true, false ),
    GOLD_INGOT( 266, false, true, true, false ),
    IRON_SWORD( 267, false, true, true, false ),
    WOOD_SWORD( 268, false, true, true, false ),
    WOOD_SHOVEL( 269, false, true, true, false ),
    WOOD_PICK_AXE( 270, false, true, true, false ),
    WOOD_AXE( 271, false, true, true, false ),
    STONE_SWORD( 272, false, true, true, false ),
    STONE_SHOVEL( 273, false, true, true, false ),
    STONE_PICK_AXE( 274, false, true, true, false ),
    STONE_AXE( 275, false, true, true, false ),
    DIAMOND_SWORD( 276, false, true, true, false ),
    DIAMOND_SHOVEL( 277, false, true, true, false ),
    DIAMOND_PICK_AXE( 278, false, true, true, false ),
    DIAMOND_AXE( 279, false, true, true, false )
    ;

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
