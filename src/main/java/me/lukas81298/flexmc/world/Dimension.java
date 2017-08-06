package me.lukas81298.flexmc.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 05.08.2017
 */
@RequiredArgsConstructor
@Getter
public enum Dimension {

    NETHER( -1 ),
    OVER_WORLD( 0 ),
    END( 1 );

    private final int id;

}
