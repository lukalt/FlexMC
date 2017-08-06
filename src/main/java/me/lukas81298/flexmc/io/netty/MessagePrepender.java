package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.lukas81298.flexmc.io.message.Message;

/**
 * @author lukas
 * @since 04.08.2017
 */

public class MessagePrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode( ChannelHandlerContext channelHandlerContext, ByteBuf a, ByteBuf byteBuf ) throws Exception {
        int i = a.readableBytes();
        int j = Message.getVarIntSize( i );
        if ( j > 3 ) {
            throw new IllegalArgumentException( "unable to fit " + i + " into " + 3 );
        }

        byteBuf.ensureWritable( j + i );
        Message.writeVarInt( i, byteBuf );
        byteBuf.writeBytes( a, a.readerIndex(), i );
    }

}
