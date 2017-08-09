package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.lukas81298.flexmc.util.crypt.EncryptionBufWrapper;

import javax.crypto.Cipher;
import java.util.List;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class MessageDecrypter extends MessageToMessageDecoder<ByteBuf> {

    private final EncryptionBufWrapper encryption;

    public MessageDecrypter( Cipher cipher ) {
        this.encryption = new EncryptionBufWrapper( cipher );
    }

    @Override
    protected void decode( ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list ) throws Exception {
        list.add( this.encryption.decrypt( channelHandlerContext, buf ) );
    }
}
