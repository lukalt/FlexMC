package me.lukas81298.flexmc.world.chunk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.lukas81298.flexmc.entity.FlexEntity;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.FlexWorld;
import me.lukas81298.flexmc.world.block.FlexBlock;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author lukas
 * @since 06.08.2017
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode( of = { "x", "z" } )
public class ChunkColumn implements Chunk, ChunkSnapshot {

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

    @Override
    public String getWorldName() {
        return this.world.getName();
    }

    @Override
    public int getBlockTypeId( int i, int i1, int i2 ) {
        return this.getBlockAt( i, i1, i2 ).getTypeId();
    }

    @Override
    public int getBlockData( int i, int i1, int i2 ) {
        return this.getBlockAt( i, i1, i2 ).getData();
    }

    @Override
    public int getBlockSkyLight( int i, int i1, int i2 ) {
        return 15;
    }

    @Override
    public int getBlockEmittedLight( int i, int i1, int i2 ) {
        return 15;
    }

    @Override
    public int getHighestBlockYAt( int i, int i1 ) {
        return 0;
    }

    @Override
    public Biome getBiome( int i, int i1 ) {
        int biome = this.getRawBiome( i, i1 );
        if( biome == 127 ) {
            return Biome.VOID;
        }
        return Biome.values()[ biome ];
    }

    public byte getRawBiome( int x, int z ) {
        return biome[ z * 16 | x ];
    }

    @Override
    public double getRawBiomeTemperature( int i, int i1 ) {
        return 0;
    }

    @Override
    public double getRawBiomeRainfall( int i, int i1 ) {
        return 0;
    }

    @Override
    public long getCaptureFullTime() {
        return 0;
    }

    @Override
    public boolean isSectionEmpty( int i ) {
        return this.sections[i].isEmpty();
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
        int j = section.getBlock( x, y & 0xF, z );
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
        return this.world;
    }

    @Override
    public Block getBlock( int i, int i1, int i2 ) {
        Vector3i position = new Vector3i( i, i1, i2 ); // todo change to absolute ccordinates
        return new FlexBlock(  world, position, this.getBlockAt( i1, i1, i2 ) );
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(){
        // we actually do not need chunk snapshots, thats just a dummy to prevent plugin breaking
        return this;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot( boolean b, boolean b1, boolean b2 ) {
        return this;
    }

    @Override
    public Entity[] getEntities() {
        int minX = this.x * 16;
        int minZ = this.z * 16;
        int maxX = this.x + ( this.x < 0 ? -1 : 1 ) * 16;
        int maxZ = this.x + ( this.x < 0 ? -1 : 1 ) * 16;

        List<Entity> list = new ArrayList<>();
        for ( FlexEntity flexEntity : this.world.getEntitySet() ) {
            Location l = flexEntity.getLocation();
            if( l.getX() > minX && l.getX() < maxX && l.getZ() > minZ && l.getZ() < maxZ ) {
                list.add( flexEntity );
            }
        }
        return list.toArray( new Entity[list.size()] );
    }

    @Override
    public org.bukkit.block.BlockState[] getTileEntities() {
        return new org.bukkit.block.BlockState[0];
    }

    @Override
    public boolean isLoaded() {
        return true;
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