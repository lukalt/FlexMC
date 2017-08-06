package me.lukas81298.flexmc.world.generator;

import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.ChunkSection;

/**
 * @author lukas
 * @since 06.08.2017
 */
@RequiredArgsConstructor
public class FlatGenerator implements ChunkGenerator {

    private final int bottomLayerSize;
    private final int topLayerSize;

    private final BlockState bottomType;
    private final BlockState topType;

    @Override
    public void generate( ChunkSection[] sections ) {
        ChunkSection section = sections[0];
        for( int x = 0; x < 16; x++ ) {
            for( int z = 0; z < 16; z++ ) {
                for( int y = 0; y < bottomLayerSize + topLayerSize; y++ ) {
                    if( y < bottomLayerSize ) {
                        section.setBlock( x, y, z, bottomType.getId(), bottomType.getData() );
                    } else {
                        section.setBlock( x, y, z, topType.getId(), topType.getData() );
                    }
                    section.getBlockLight().set( x, y, z, 15 );
                }
            }
        }
    }
}
