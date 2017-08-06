package me.lukas81298.flexmc.world;

/**
 * @author lukas
 * @since 06.08.2017
 */
public interface WorldGenerator {

    int getHeight();

    BlockState getState( int x, int y, int z );
}
