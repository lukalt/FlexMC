package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.io.message.Message;

/**
 * @author lukas
 * @since 04.08.2017
 */
@RequiredArgsConstructor
public class MessageEncoder extends MessageToByteEncoder<Message> {

    private final ConnectionManager connectionManager;

    @Override
    protected void encode( ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf ) throws Exception {
        ConnectionHandler connectionHandler = (ConnectionHandler) channelHandlerContext.pipeline().get( "connectionHandler" );
        int messageId = this.connectionManager.getRegistry( connectionHandler ).getMessageIdFromClass( message.getClass() );
        if( messageId == -1 ) {
            System.out.println(  this.connectionManager.getRegistry( connectionHandler ).getClass().getName() + " message id  not found for " + message.getClass().getName() );
            return;
        }
        Message.writeVarInt( messageId, byteBuf );
        message.write( byteBuf );
    }

}
