package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.inventory.Material;
import me.lukas81298.flexmc.world.BlockState;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class FlatGenerator extends LayeredChunkGenerator {

    public FlatGenerator() {
        this.addLayer( new BlockState( Material.BEDROCK, 0 ) );
        this.addLayer( new BlockState( Material.DIRT, 0 ), 3 );
        this.addLayer( new BlockState( Material.GRASS, 0 ) );
    }
}
