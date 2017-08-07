package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.world.BlockState;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class FlatGenerator extends LayeredChunkGenerator {

    public FlatGenerator() {
        this.addLayer( new BlockState( 7, 0 ) );
        this.addLayer( new BlockState( 2, 0 ), 3 );
        this.addLayer( new BlockState( 3, 0 ) );
    }
}
