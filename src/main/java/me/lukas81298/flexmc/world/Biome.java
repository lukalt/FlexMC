package me.lukas81298.flexmc.world;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 12.08.2017
 */
@RequiredArgsConstructor
@Getter
public enum Biome {
    OCEAN( 0, false ),
    PLAINS( 1, false ),
    DESERT( 2, true ),
    FOREST( 3, true ),
    TAIGA( 5, true );

    private final int id;
    private final boolean generated;

    private final static TIntObjectMap<Biome> byId = new TIntObjectHashMap<>();

    static {
        for ( Biome biome : values() ) {
            byId.put( biome.getId(), biome );
        }
    }

    public static Biome getById( int id ) {
        return byId.get( id );
    }

}
