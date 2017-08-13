package me.lukas81298.flexmc.world.chunk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

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
public class ChunkColumn implements Chunk {

    private final int x, z;
    private final ChunkSection[] sections;
    private final byte[] biome;
    private final UUID uuid = UUID.randomUUID();
    private final FlexWorld world;

    public ChunkColumn( int x, int z, FlexWorld world ) {
        this( x, z, new ChunkSection[16], new byte[0x100], world );
        Arrays.fill( biome, (byte) 1 ); // void biome
    }

    public void changeBiomeBulk( byte biomeId ) {
        Arrays.fill( biome, (byte) biomeId );
    }

    public byte getBiome( int x, int z ) {
        return biome[ z * 16 | x ];
    }

    public void setBiome( int x, int z, byte biome ) {
        this.biome[ z * 16 | x ] = biome;
    }

    public void setBlock( int x, int y, int z, BlockState type ) {
        if( y > 255 ) {
            System.out.println( "y to high " + y );
            throw new IllegalArgumentException( "y too high " + y );
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
            Material type = this.getBlockAt( x, i, z ).getType();
            if( type != Material.AIR && type != Material.LEAVES ) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Block getBlock( int i, int i1, int i2 ) {
        return null;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return null;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot( boolean b, boolean b1, boolean b2 ) {
        return null;
    }

    @Override
    public Entity[] getEntities() {
        return new Entity[0];
    }

    @Override
    public org.bukkit.block.BlockState[] getTileEntities() {
        return new org.bukkit.block.BlockState[0];
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean load( boolean b ) {
        return false;
    }

    @Override
    public boolean load() {
        return false;
    }

    @Override
    public boolean unload( boolean b, boolean b1 ) {
        return false;
    }

    @Override
    public boolean unload( boolean b ) {
        return false;
    }

    @Override
    public boolean unload() {
        return false;
    }

    @Override
    public boolean isSlimeChunk() {
        return false;
    }
}