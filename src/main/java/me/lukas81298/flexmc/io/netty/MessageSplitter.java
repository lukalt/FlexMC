package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import me.lukas81298.flexmc.io.message.Message;

import java.util.List;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class MessageSplitter extends ByteToMessageDecoder {

    @Override
    protected void decode( ChannelHandlerContext channelHandlerContext, ByteBuf input, List<Object> output ) throws Exception {
        input.markReaderIndex();

        byte[] bytes = new byte[3];
        for ( int i = 0; i < bytes.length; i++ ) {
            if ( !input.isReadable() ) {
                input.resetReaderIndex();
                return;
            }
            bytes[i] = input.readByte();
            if ( bytes[i] >= 0 ) {
                ByteBuf buf = Unpooled.wrappedBuffer( bytes );
                try {
                    int j = Message.readVarInt( buf );
                    if ( input.readableBytes() < j ) {
                        input.resetReaderIndex();
                        return;
                    }
                    output.add( input.readBytes( j ) );
                    return;
                } finally {
                    buf.release();
                }
            }
        }
        throw new CorruptedFrameException( "length wider than 21-bit" );
    }
}
