package me.lukas81298.flexmc.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.yaml.snakeyaml.tokens.BlockEndToken;

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

    public void setBlock( int x, int y, int z, BlockState type ) {
        if( y > 255 ) {
            System.out.println( "y to high " + y );
            return;
        }
        ChunkSection section = this.sections[ y / 16 ];
        y = y % 16;
        section.setBlock( x, y, z, type.getId(), type.getData() );
    }

}