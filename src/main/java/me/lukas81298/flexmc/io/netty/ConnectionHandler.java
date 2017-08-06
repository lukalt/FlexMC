package me.lukas81298.flexmc.io.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.io.protocol.ProtocolState;
import me.lukas81298.flexmc.util.ConnectionInfo;

import java.util.concurrent.ExecutionException;

/**
 * @author lukas
 * @since 04.08.2017
 */
@RequiredArgsConstructor
public class ConnectionHandler extends SimpleChannelInboundHandler<Message> {

    @Getter
    private volatile ProtocolState protocolState = ProtocolState.HANDSHAKE;

    @Getter
    @Setter
    private ConnectionInfo connectionInfo;
    private final ConnectionManager connectionManager;

    @Getter
    private volatile ChannelHandlerContext channelHandlerContext;

    @Getter
    @Setter
    private int version;

    @Getter
    @Setter
    private Player player;

    public void setProtocolState( ProtocolState protocolState ) {
        System.out.println( "Changed protocol state to " + protocolState.name() );
        this.protocolState = protocolState;
        this.channelHandlerContext.channel().config().setAutoRead( true );
    }

    @Override
    protected void channelRead0( ChannelHandlerContext channelHandlerContext, Message message ) throws Exception {
       // System.out.println( "Received message " + message.toString() );
        this.connectionManager.getListenerManager().invokeListeners( this, message );
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        super.exceptionCaught( ctx, cause );
       /*System.out.println( ctx.name() + " threw an exception" );
        cause.printStackTrace();*/
    }

    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception {
        channelHandlerContext = ctx;
    }

    @Override
    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
        channelHandlerContext = null;
        if( player != null ) {
            Flex.getServer().getPlayerManager().handlePlayerQuit( player );
        }
    }

    public void sendMessage( Message message ) {
        if ( channelHandlerContext != null ) {
            if( this.channelHandlerContext.channel().eventLoop().inEventLoop() ) {
                flush( message );
            } else {
                this.channelHandlerContext.channel().eventLoop().execute( new Runnable() {
                    @Override
                    public void run() {
                        flush( message );
                    }
                } );
            }
        }
    }

    private void flush( Message message ) {
        channelHandlerContext.writeAndFlush( message ).addListener( ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE );
    }
}
