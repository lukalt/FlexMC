package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.world.chunk.ChunkColumn;

/**
 * @author lukas
 * @since 06.08.2017
 */
public interface ChunkGenerator {

    void generate( ChunkColumn column );
}
