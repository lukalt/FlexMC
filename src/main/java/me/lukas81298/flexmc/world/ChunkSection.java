package me.lukas81298.flexmc.world;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.procedure.TIntProcedure;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class ChunkSection {

    private final TIntList palette = new TIntArrayList();
    private final int[] blocks;
    @Getter private final NibbleArray blockLight;
    @Setter private NibbleArray skyLight;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ChunkSection() {
        this.palette.add( 0 );
        this.blocks = new int[ 16 * 16 * 16 ];
        this.blockLight = new NibbleArray( 16 * 16 * 16 );
    }

    public void setBlock( Vector3i position, BlockState blockState ) {
        this.setBlock( position.getX(), position.getY(), position.getZ(), blockState.getId(), blockState.getData() );
    }

    public void setBlock( int x, int y, int z, int type, int data ) {
        setBlock( calArrayIndex( x, y, z ), type, data );
    }

    public int getBlockType( int x, int y, int z ) {
        return getBlock( x, y, z ) >> 4;
    }

    public int getBlock( int x, int y, int z ) {
        int index = blocks[calArrayIndex( x, y, z )];
        return palette.get( index );
    }

    public void setBlock( int position, int type, int data ) {
        this.lock.writeLock().lock();
        try {
            final int typeAndData = type << 4 | ( data & 0xF );
            int index = this.palette.indexOf( typeAndData );
            if ( index == -1 ) { // not in palette
                index = palette.size();
                palette.add( typeAndData );
            }

            blocks[position] = index;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private void writePalette( ByteBuf buf ) {
        Message.writeVarInt( this.palette.size(), buf );
        this.palette.forEach( new TIntProcedure() {
            @Override
            public boolean execute( int i ) {
                Message.writeVarInt( i, buf );
                return true;
            }
        } );
    }

    private int getExpectedLength( int i ) {
        double t = 16 * 16 * 16 * i / 64.0;
        return (int) Math.ceil( t );
    }

    private void writeBlockData( ByteBuf buf ) throws Exception {
        int bpb = 4;
        while ( palette.size() > 1 << bpb ) {
            bpb += 1;
        }
        long max = ( 1L << bpb ) - 1;
        buf.writeByte( bpb );
        this.writePalette( buf );
        int lengthExpected = this.getExpectedLength( bpb );
        Message.writeVarInt( lengthExpected, buf );
        long[] data = new long[lengthExpected];
        for ( int i = 0; i < this.blocks.length; i++ ) {
            int value = this.blocks[i];
            int bitIndex = i * bpb;
            int si = bitIndex / 64;
            int ei = ( ( i + 1 ) * bpb - 1 ) / 64;
            int startBitSubIndex = bitIndex % 64;
            data[si] = data[si] & ~( max << startBitSubIndex ) | ( (long) value & max ) << startBitSubIndex;
            if ( si != ei ) {
                int endBitSubIndex = 64 - startBitSubIndex;
                data[ei] = data[ei] >>> endBitSubIndex << endBitSubIndex | ( (long) value & max ) >> endBitSubIndex;
            }
        }
        for ( long l : data ) {
            buf.writeLong( l );
        }
    }

    public void write( ByteBuf buf ) throws Exception {
        this.lock.readLock().lock();
        try {
            this.writeBlockData( buf );
            buf.writeBytes( blockLight.getHandle() );
            if( this.skyLight != null ) {
                buf.writeBytes( skyLight.getHandle() );
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private int calArrayIndex( int x, int y, int z ) {
        return y << 8 | z << 4 | x;
    }

}