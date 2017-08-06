package me.lukas81298.flexmc.io.message;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lukas
 * @since 04.08.2017
 */
public abstract class Message {

    public abstract void read( ByteBuf buf ) throws IOException;

    public abstract void write( ByteBuf buf ) throws IOException;

    public static String readString( ByteBuf buf ) {
        int length = readVarInt( buf );
        byte[] bytes = new byte[length];
        for ( int i = 0; i < bytes.length; i++ ) {
            bytes[i] = buf.readByte();
        }
        return new String( bytes, Charsets.UTF_8 );
    }

    public static int getVarIntSize( int i ) {
        for ( int j = 1; j < 5; j++ ) {
            if ( ( i & -1 << j * 7 ) == 0 ) {
                return j;
            }
        }
        return 5;
    }

    public static void writeString( String string, ByteBuf buf ) {
        writeVarInt( string.length(), buf );
        buf.writeBytes( string.getBytes( Charsets.UTF_8 ) );
    }

    public static UUID readUUID( ByteBuf buf ) {
        return new UUID( buf.readLong(), buf.readLong() );
    }

    public static void writeUUID( UUID uuid, ByteBuf buf ) {
        buf.writeLong( uuid.getMostSignificantBits() );
        buf.writeLong( uuid.getLeastSignificantBits() );
    }

    public static int readVarInt( ByteBuf buf ) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buf.readByte();
            int value = ( read & 0b01111111 );
            result |= ( value << ( 7 * numRead ) );

            numRead++;
            if ( numRead > 5 ) {
                throw new RuntimeException( "VarInt is too big" );
            }
        } while ( ( read & 0b10000000 ) != 0 );

        return result;
    }

    public static long readVarLong( ByteBuf buf ) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buf.readByte();
            int value = ( read & 0b01111111 );
            result |= ( value << ( 7 * numRead ) );

            numRead++;
            if ( numRead > 10 ) {
                throw new RuntimeException( "VarLong is too big" );
            }
        } while ( ( read & 0b10000000 ) != 0 );

        return result;
    }

    @SuppressWarnings( "Duplicates" )
    public static void writeVarInt( int value, ByteBuf buf ) {
        do {
            byte temp = (byte) ( value & 0b01111111 );
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if ( value != 0 ) {
                temp |= 0b10000000;
            }
            buf.writeByte( temp );
        } while ( value != 0 );
    }

    @SuppressWarnings( "Duplicates" )
    public static void writeVarLong( long value, ByteBuf buf ) {
        do {
            byte temp = (byte) ( value & 0b01111111 );
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if ( value != 0 ) {
                temp |= 0b10000000;
            }
            buf.writeByte( temp );
        } while ( value != 0 );
    }

    public static byte toAngle( float radian ) {
        return ( (byte) (int) ( radian * 256.0F / 360.0F ) );
    }
}
