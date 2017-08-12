package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.util.BiTuple;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lukas
 * @since 07.08.2017
 */
public abstract class LayeredChunkGenerator implements ChunkGenerator {

    private final List<BiTuple<BlockState, Integer>> layers = new ArrayList<>();

    @Override
    public void generate( ChunkColumn column ) {
        int y = 0;
        for ( BiTuple<BlockState, Integer> layer : this.layers ) {
            for ( int i = 0; i < layer.getB(); i++ ) {
                for ( int x = 0; x < 16; x++ ) {
                    for ( int z = 0; z < 16; z++ ) {
                        column.setBlock( x, y, z, layer.getA() );
                    }
                }
                y++;
            }
        }
    }

    protected void addLayer( BlockState blockState ) {
        this.addLayer( blockState, 1 );
    }

    protected void addLayer( BlockState blockState, int amount ) {
        this.layers.add( new BiTuple<>( blockState, amount ) );
    }
}
