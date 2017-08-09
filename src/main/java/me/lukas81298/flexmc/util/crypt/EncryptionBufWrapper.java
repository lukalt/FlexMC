package me.lukas81298.flexmc.util.crypt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lukas
 * @since 09.08.2017
 */
@RequiredArgsConstructor
public class EncryptionBufWrapper {

    private final Cipher cipher;
    private byte[] buf = new byte[0];
    private byte[] c = new byte[0];
    private final Lock lock = new ReentrantLock();

    private byte[] toByteArray( ByteBuf buf ) {
        int i = buf.readableBytes();
        if ( this.buf.length < i ) {
            this.buf = new byte[i];
        }
        buf.readBytes( this.buf, 0, i );
        return this.buf;
    }

    public ByteBuf decrypt( ChannelHandlerContext paramChannelHandlerContext, ByteBuf input ) throws ShortBufferException {
        int readableBytes = input.readableBytes();
        this.lock.lock();
        try {
            byte[] array = toByteArray( input );
            ByteBuf output = paramChannelHandlerContext.alloc().heapBuffer( this.cipher.getOutputSize( readableBytes ) );
            output.writerIndex( this.cipher.update( array, 0, readableBytes, output.array(), output.arrayOffset() ) );
            return output;
        } finally {
            this.lock.unlock();
        }
    }

    public void encrypt( ByteBuf input, ByteBuf output ) throws ShortBufferException {
        int readableBytes = input.readableBytes();
        this.lock.lock();
        try {
            byte[] array = toByteArray( input );
            int size = this.cipher.getOutputSize( readableBytes );
            if ( this.c.length < size ) {
                this.c = new byte[size];
            }
            output.writeBytes( this.c, 0, this.cipher.update( array, 0, readableBytes, this.c ) );
        } finally {
            this.lock.unlock();
        }
    }
}
