package me.lukas81298.flexmc.world.chunk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.lukas81298.flexmc.world.BlockState;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author lukas
 * @since 06.08.2017
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode( of = { "x", "z" } )
public class ChunkColumn {

    private final int x, z;
    private final ChunkSection[] sections;
    private final byte[] biome;
    private final UUID uuid = UUID.randomUUID();

    public ChunkColumn( int x, int z ) {
        this( x, z, new ChunkSection[16], new byte[0x100] );
        Arrays.fill( biome, (byte) 127 ); // void biome
    }

    public void setBlock( int x, int y, int z, BlockState type ) {
        if( y > 255 ) {
            System.out.println( "y to high " + y );
            return;
        }
        ChunkSection section = this.sections[ y / 16 ];
        y = y % 16;
        section.setBlock( x, y, z, type.getTypeId(), type.getData() );
    }

    public BlockState getBlockAt( int x, int y, int z ) {
        ChunkSection section = this.sections[ y / 16 ];
        y = y % 16;
        int j = section.getBlock( x, y, z );
        int type = j >> 4;
        int data = j & 15;
        return new BlockState( type, data );
    }

    public int getHighestYAt( int x, int z ) {
        for( int i = 255; i > 0; i-- ) {
            if( this.getBlockAt( x, i, z ).getType().isSolid() ) {
                return i;
            }
        }
        return 0;
    }
}