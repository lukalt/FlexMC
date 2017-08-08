package me.lukas81298.flexmc.world.generator;

import me.lukas81298.flexmc.util.BiTuple;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.ChunkColumn;

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
        this.addLayer( new BlockState( 7, 0 ) );
        this.addLayer( new BlockState( 1, 0 ), 55 );
        this.addLayer( new BlockState( 3, 0 ), 3 );
        this.addLayer( new BlockState( 2, 0 ) );
    }

    @Override
    public void generate( ChunkColumn column ) {
        super.generate( column );
        int counter = 3 + random.nextInt( 3 );
        List<BiTuple<Integer, Integer>> list = new ArrayList<>();
        outer: for( int i = 0; i < counter; i++ ) {
            int x = random.nextInt( 11 ) + 3;
            int z = random.nextInt( 11 ) + 3;
            for( BiTuple<Integer, Integer> a : list ) {
                if( Math.abs( a.getA() - x ) < 2 || Math.abs( a.getB() - z ) < 2 ) {
                    continue outer;
                }
            }
            generateTree( column, x, z );
            list.add( new BiTuple<>( x, z ) );
        }
    }

    private void generateTree( ChunkColumn column, int x, int z ) {
        double rand = Math.random();
        BlockState logType, leavesType;
        if( rand < .25 ) {
            logType = new BlockState( 17, 0 );
            leavesType = new BlockState( 18, 0 );
        } else if( rand < .5 ) {
            logType = new BlockState( 17, 1 );
            leavesType = new BlockState( 18, 1 );
        } else if ( rand < .75 ) {
            logType = new BlockState( 17, 2 );
            leavesType = new BlockState( 18, 2 );
        } else {
            logType = new BlockState( 17, 3 );
            leavesType = new BlockState( 18, 3 );
        }
        int y = column.getHighestYAt( x, z ) + 1;
        int height = (int) (Math.random() * 4 + 3);
        for( int i = 0; i < height; i++ ) {
            column.setBlock( x, y + i, z, logType );
        }
        for( int i = -2; i <= 2; i++ ) {
            for( int j = -2; j <= 2; j++ ) {
                column.setBlock( x + i, y + height, z + j, leavesType );
            }
        }
        for( int i = -1; i <= 1; i++ ) {
            for( int j = -1; j <= 1; j++ ) {
                column.setBlock( x + i, y + height + 1, z + j, leavesType );
            }
        }
    }
}
