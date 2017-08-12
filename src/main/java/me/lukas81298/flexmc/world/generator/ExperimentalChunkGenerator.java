package me.lukas81298.flexmc.world.generator;

import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;
import me.lukas81298.flexmc.inventory.Material;
import me.lukas81298.flexmc.util.BiTuple;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lukas
 * @since 12.08.2017
 */
@SuppressWarnings( "Duplicates" )
public class ExperimentalChunkGenerator extends LayeredChunkGenerator {

    private final int size = 64 * 16;
    private final Random random = new Random();
    private final Grid grid = new Grid( size );
    private final NoiseGenerator noiseGenerator = new NoiseGenerator();

    public ExperimentalChunkGenerator() {
        this.addLayer( new BlockState( Material.BEDROCK ), 1 );
        this.addLayer( new BlockState( Material.STONE ), 50 );
        this.addLayer( new BlockState( Material.DIRT ), 1 );

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
                float height = Math.max( 0F, grid.get( x + cx, z + cz ) - .5F );
                int v = (int) ( 50 * height );
                for ( int i = 0; i < v; i++ ) {
                    boolean last = v - 1 == i;
                    column.setBlock( x, i + 52, z, new BlockState( last ? Material.GRASS : Material.DIRT ) );
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
        generateOres( column, Material.IRON_ORE, 1, 54, 9,11 );
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
        int l = random.nextInt( 4 );
        BlockState logType = new BlockState( Material.LOG, l ), leavesType = new BlockState( Material.LEAVES, l );
        int y = column.getHighestYAt( x, z ) + 1;
        int height = (int) ( Math.random() * 4 + 3 );
        for ( int i = 0; i < height; i++ ) {
            column.setBlock( x, y + i, z, logType );
        }
        for ( int i = -2; i <= 2; i++ ) {
            for ( int j = -2; j <= 2; j++ ) {
                column.setBlock( x + i, y + height, z + j, leavesType );
            }
        }
        for ( int i = -1; i <= 1; i++ ) {
            for ( int j = -1; j <= 1; j++ ) {
                column.setBlock( x + i, y + height + 1, z + j, leavesType );
            }
        }
    }

}
