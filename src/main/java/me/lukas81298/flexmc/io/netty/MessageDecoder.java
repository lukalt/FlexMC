package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.io.message.Message;

import java.util.List;

/**
 * @author lukas
 * @since 04.08.2017
 */
@RequiredArgsConstructor
public class MessageDecoder extends ByteToMessageDecoder {

    private final ConnectionManager connectionManager;

    @Override
    protected void decode( ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list ) throws Exception {
        if( byteBuf instanceof EmptyByteBuf ) {
            System.out.println( "Got empty byte buf for " + channelHandlerContext.name() );
            return;
        }

        ConnectionHandler connectionHandler = (ConnectionHandler) channelHandlerContext.pipeline().get( "connectionHandler" );
        int messageId = Message.readVarInt( byteBuf );
        Message message = connectionManager.getRegistry( connectionHandler ).createMessageFromId( messageId );
        if( message == null ) {

            System.out.println( "Cannot find message id " + messageId );
            while ( byteBuf.readableBytes() > 0 ){
                byteBuf.readByte();
            }
            return;
        }
        message.read( byteBuf );

        list.add( message );
    }
}
