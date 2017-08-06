package me.lukas81298.flexmc.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

/**
 * @author lukas
 * @since 06.08.2017
 */
@RequiredArgsConstructor
@Getter
@ToString
public class ChunkColumn {

    private final int x, z;
    private final ChunkSection[] sections;
    private final byte[] biome;

    public ChunkColumn( int x, int z ) {
        this( x, z,new ChunkSection[16], new byte[0x100] );
        Arrays.fill( biome, (byte) 127 ); // void biome
    }

    public boolean hasBiome() {
        return biome != null;
    }

}