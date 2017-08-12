package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.inventory.Material;
import me.lukas81298.flexmc.util.BiTuple;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.chunk.ChunkColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class OverWorldGenerator extends LayeredChunkGenerator {

    private final Random random = new Random();

    public OverWorldGenerator() {
        this.addLayer( new BlockState( Material.BEDROCK ) );
        this.addLayer( new BlockState( Material.STONE ), 55 );
        this.addLayer( new BlockState( Material.DIRT ), 3 );
        this.addLayer( new BlockState( Material.GRASS ) );
    }

    @Override
    public void generate( ChunkColumn column ) {
        super.generate( column );
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
        double rand = Math.random();
        BlockState logType, leavesType;
        if ( rand < .25 ) {
            logType = new BlockState( Material.LOG );
            leavesType = new BlockState( Material.LEAVES );
        } else if ( rand < .5 ) {
            logType = new BlockState( Material.LOG, 1 );
            leavesType = new BlockState( Material.LEAVES, 1 );
        } else if ( rand < .75 ) {
            logType = new BlockState( Material.LOG, 2 );
            leavesType = new BlockState( Material.LEAVES, 2 );
        } else {
            logType = new BlockState( Material.LOG, 3 );
            leavesType = new BlockState( Material.LEAVES, 3 );
        }
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
