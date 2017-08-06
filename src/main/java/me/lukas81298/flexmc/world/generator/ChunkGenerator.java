package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.world.ChunkSection;

/**
 * @author lukas
 * @since 06.08.2017
 */
public interface ChunkGenerator {

    void generate( ChunkSection[] sections );
}
