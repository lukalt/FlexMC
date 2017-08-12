package me.lukas81298.flexmc.world.generator;

import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;
import me.lukas81298.flexmc.inventory.Material;
import me.lukas81298.flexmc.util.BiTuple;
import me.lukas81298.flexmc.world.Biome;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lukas
 * @since 12.08.2017
 */
@SuppressWarnings( "Duplicates" )
public class OverWorldChunkGenerator extends LayeredChunkGenerator {

    private final int size = 64 * 16;
    private final Random random = new Random();
    private final Grid grid = new Grid( size );
    private final NoiseGenerator noiseGenerator = new NoiseGenerator();
    private final BufferedImage biomeGrid;

    public OverWorldChunkGenerator() {

        System.out.println( "Generating biome map" );
        biomeGrid = new BufferedImage( size, size, BufferedImage.TYPE_3BYTE_BGR );
        Graphics graphics = biomeGrid.getGraphics();
        graphics.setColor( new Color( 1 ) );
        graphics.fillRect( 0, 0, size, size );
        int specialBiomes = 10 + random.nextInt( 6 );
        List<Biome> biomes = new ArrayList<>();
        for ( Biome biome : Biome.values() ) {
            if ( biome.isGenerated() ) {
                biomes.add( biome );
            }
        }
        for ( int i = 0; i < specialBiomes; i++ ) {
            graphics.setColor( new Color( biomes.get( random.nextInt( biomes.size() ) ).getId() ) );
            int x = random.nextInt( size );
            int z = random.nextInt( size );
            int tx = 8 * 16 + random.nextInt( 3 * 16 );
            int tz = 8 * 16 + random.nextInt( 3 * 16 );
          //  System.out.println( x + "," + z + "," + tx + "," + tz );
            graphics.fillOval( x, z, tx, tz );
        }
        /*try {
            ImageIO.write( biomeGrid, "png", new File( "biomeGrid.png" ) );
        } catch ( IOException e ) {
            e.printStackTrace();
        }*/
        this.addLayer( new BlockState( Material.BEDROCK ), 1 );
        this.addLayer( new BlockState( Material.STONE ), 51 );

        System.out.println( "Generating height map" );
        setupStage( 32, .7F );
        setupStage( 16, 0.4f );
        setupStage( 8, 0.22f );
        setupStage( 4, 0.1f );
        setupStage( 1, 0.05f );
    }

    private void setupStage( final int radius, final float modifier ) {
        noiseGenerator.setRadius( radius );
        noiseGenerator.setModifier( modifier );
        noiseGenerator.setSeed( Generators.rollSeed() );
        noiseGenerator.generate( grid );
    }


    @Override
    public void generate( ChunkColumn column ) {
        super.generate( column );
        int cx = column.getX() * 16 + size / 2;
        int cz = column.getZ() * 16 + size / 2;
        for ( int x = 0; x < 16; x++ ) {
            for ( int z = 0; z < 16; z++ ) {
                byte biome = (byte) biomeGrid.getRGB( x + cx, z + cz );
                column.setBiome( x, z, biome );
                float height = Math.max( 0F, grid.get( x + cx, z + cz ) - .5F );
                int v = (int) ( 50 * height );
                for ( int i = 0; i < v; i++ ) {

                    BlockState type;
                    switch ( biome ) {
                        case 2:
                            type = new BlockState( i > v - 6 ? Material.SAND : Material.SANDSTONE );
                            break;
                        default:
                            type = new BlockState( v - 1 == i ? Material.GRASS : Material.DIRT );
                            break;
                    }

                    column.setBlock( x, i + 52, z, type );
                }
            }
        }
        int counter = 3 + random.nextInt( 3 );
        List<BiTuple<Integer, Integer>> list = new ArrayList<>();
        outer:
        for ( int i = 0; i < counter; i++ ) {
            int x = random.nextInt( 11 ) + 3;
            int z = random.nextInt( 11 ) + 3;
            for ( BiTuple<Integer, Integer> a : list ) {
                if ( Math.abs( a.getA() - x ) < 2 || Math.abs( a.getB() - z ) < 2 ) {
                    continue outer;
                }
            }
            generateTree( column, x, z );
            list.add( new BiTuple<>( x, z ) );
        }
        generateOres( column, Material.COAL_ORE, 17, 54, 20, 24 );
        generateOres( column, Material.IRON_ORE, 1, 54, 9, 11 );
        generateOres( column, Material.GOLD_ORE, 1, 30, 8, 10 );
        generateOres( column, Material.DIAMOND_ORE, 1, 16, 7, 9 ); // todo lower
    }

    private void generateOres( ChunkColumn column, Material material, int fromLayer, int toLayer, int minCount, int maxCount ) {
        int count = minCount + random.nextInt( maxCount - minCount );
        for ( int i = 0; i < count; i++ ) {
            int x = random.nextInt( 11 ) + 3;
            int z = random.nextInt( 11 ) + 3;
            int y = fromLayer + random.nextInt( toLayer - fromLayer );
            for ( int h = -1; h < 3; h++ ) {
                for ( int j = -1; j < 2; j++ ) {
                    for ( int k = -1; k < 2; k++ ) {
                        column.setBlock( x + i, z + h, y + k, new BlockState( material ) );
                    }
                }
            }
        }
    }

    private void generateTree( ChunkColumn column, int x, int z ) {
        byte biome = column.getBiome( x, z );
        switch ( biome ) {
            case 1:
                column.setBlock( x, column.getHighestYAt( x, z ) + 1, z, new BlockState( 31, 1 ) );
                break;
            case 2:
                int y = column.getHighestYAt( x, z ) + 1;
                int height = 1 + random.nextInt( 3 );
                for ( int i = 0; i < height; i++ ) {
                    column.setBlock( x, y + i, z, new BlockState( Material.CACTUS ) );
                }
                break;
            case 3:
                generateTree0( column, x, z, 0, 3, 4 );
                break;
            case 5:
                generateTree0( column, x, z, 1, 4, 5 );
                break;

        }

    }

    private void generateTree0( ChunkColumn column, int x, int z, int id, int minHeight, int maxHeight ) {
        int y = column.getHighestYAt( x, z ) + 1;
        int height = minHeight + random.nextInt( maxHeight - minHeight );
        for ( int i = 0; i < height; i++ ) {
            column.setBlock( x, y + i, z, new BlockState( Material.LOG, id ) );
        }
        for ( int i = -2; i <= 2; i++ ) {
            for ( int j = -2; j <= 2; j++ ) {
                column.setBlock( x + i, y + height, z + j, new BlockState( Material.LEAVES, id ) );
            }
        }
        for ( int i = -1; i <= 1; i++ ) {
            for ( int j = -1; j <= 1; j++ ) {
                column.setBlock( x + i, y + height + 1, z + j, new BlockState( Material.LEAVES, id ) );
            }
        }
    }

}
