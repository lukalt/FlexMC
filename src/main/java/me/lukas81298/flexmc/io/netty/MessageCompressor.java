package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Setter;
import me.lukas81298.flexmc.io.message.Message;

import java.util.zip.Deflater;

/**
 * @author lukas
 * @since 12.08.2017
 */
public class MessageCompressor extends MessageToByteEncoder<ByteBuf> {

    private final byte[] a = new byte['?'];
    private final Deflater deflater;
    @Setter
    private int threshold;

    public MessageCompressor( int threshold ) {
        this.threshold = threshold;
        this.deflater = new Deflater();
    }

    @Override
    protected void encode( ChannelHandlerContext channelHandlerContext, ByteBuf buf, ByteBuf byteBuf ) throws Exception {
        int i = byteBuf.readableBytes();
        if ( i < this.threshold ) {
            Message.writeVarInt( 0, buf );
            byteBuf.writeBytes( buf );
        } else {
            byte[] byteArray = new byte[i];
            buf.readBytes( byteArray );

            Message.writeVarInt( byteArray.length, byteBuf );

            this.deflater.setInput( byteArray, 0, i );
            this.deflater.finish();
            while ( !this.deflater.finished() ) {
                int j = this.deflater.deflate( this.a );
                byteBuf.writeBytes( this.a, 0, j );
            }
            this.deflater.reset();
        }
    }
}
