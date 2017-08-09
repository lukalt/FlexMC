package me.lukas81298.flexmc.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.lukas81298.flexmc.util.crypt.EncryptionBufWrapper;

import javax.crypto.Cipher;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class MessageEncrypter extends MessageToByteEncoder<ByteBuf> {

    private final EncryptionBufWrapper encryption;

    public MessageEncrypter( Cipher cipher ) {
        this.encryption = new EncryptionBufWrapper( cipher );
    }

    @Override
    protected void encode( ChannelHandlerContext channelHandlerContext, ByteBuf buf, ByteBuf byteBuf ) throws Exception {
        this.encryption.encrypt( buf, byteBuf );
    }
}
