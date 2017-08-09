package me.lukas81298.flexmc.io.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.io.message.login.server.MessageS02LoginSuccess;
import me.lukas81298.flexmc.io.protocol.ProtocolState;
import me.lukas81298.flexmc.util.ConnectionInfo;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.util.VerifySession;
import me.lukas81298.flexmc.util.crypt.AuthHelper;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private VerifySession verifySession;

    @Getter
    private Player player;

    private final AtomicBoolean encrypted = new AtomicBoolean( false );

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
        if ( player != null ) {
            Flex.getServer().getPlayerManager().handlePlayerQuit( player );
        }
    }

    public String getIpAddress() {
        InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    public void sendMessage( Message message ) {
        if ( channelHandlerContext != null ) {
            if ( this.channelHandlerContext.channel().eventLoop().inEventLoop() ) {
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

    public void loginSuccess( String name, UUID uuid ) {
        sendMessage( new MessageS02LoginSuccess( name, uuid.toString() ) );
        System.out.println( name + " logged in with uuid " + uuid.toString() );
        setProtocolState( ProtocolState.PLAY );

        this.player = new Player( -1, new Location( .5, Flex.getServer().getWorld().getChunkAt( 0, 0 ).getHighestYAt( 0, 0 ) + 1, .5 ), name, uuid, this, Flex.getServer().getWorld() );
        Flex.getServer().getPlayerManager().handlePlayerJoin( player );
    }

    public void startProtocolEncryption( SecretKey key ) {
        if ( this.encrypted.compareAndSet( false, true ) ) {

            ChannelPipeline pipeline = this.channelHandlerContext.channel().pipeline();
            try {
                pipeline.addBefore( "splitter", "decrypter", new MessageDecrypter( AuthHelper.createCipher( false, key ) ) );
                pipeline.addBefore( "prepender", "encrypter", new MessageEncrypter( AuthHelper.createCipher( true, key ) ) );
            } catch ( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
                e.printStackTrace();
            }
        }
    }
}
