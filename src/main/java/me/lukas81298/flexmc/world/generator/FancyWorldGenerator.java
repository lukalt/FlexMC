package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.world.BlockState;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class FancyWorldGenerator extends LayeredChunkGenerator {

    public FancyWorldGenerator() {
        this.addLayer( new BlockState( 7, 0 ) );
        this.addLayer( new BlockState( 1, 0 ), 55 );
        this.addLayer( new BlockState( 3, 0 ), 3 );
        this.addLayer( new BlockState( 2, 0 ) );
    }

}
