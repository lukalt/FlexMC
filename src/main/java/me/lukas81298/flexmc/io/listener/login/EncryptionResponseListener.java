package me.lukas81298.flexmc.io.listener.login;

import com.google.gson.JsonObject;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.login.client.MessageC01EncryptionResponse;
import me.lukas81298.flexmc.io.message.login.server.MessageS00Disconnect;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.UUIDUtil;
import me.lukas81298.flexmc.util.VerifySession;
import me.lukas81298.flexmc.util.crypt.AuthHelper;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class EncryptionResponseListener implements MessageInboundListener<MessageC01EncryptionResponse> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC01EncryptionResponse message ) throws Exception {
        VerifySession verifySession = connectionHandler.getVerifySession();
        if ( verifySession != null ) {
            if ( verifySession.getState().compareAndSet( 1, 2 ) ) {
                Cipher cipher = AuthHelper.createDecryptCipher();
                SecretKey secretKey = new SecretKeySpec( cipher.doFinal( message.getSharedSecret() ), "AES" );
                AuthHelper.reInitCipher( cipher );
                byte[] decryptedToken = cipher.doFinal( message.getVerifyToken() );

                if ( !Arrays.equals( decryptedToken, verifySession.getToken() ) ) {
                    connectionHandler.sendMessage( new MessageS00Disconnect( "{\"text\":\"Failed to verify username\"}" ) );
                    return;
                }

                connectionHandler.startProtocolEncryption( secretKey ); // finally enable encryption
                JsonObject jsonElement = verifyToken( connectionHandler, createVerifyHash( secretKey ) );
                if( jsonElement == null ) {
                    connectionHandler.sendMessage( new MessageS00Disconnect( "{\"text\":\"Failed to verify username\"}" ) );
                    return;
                }
                connectionHandler.loginSuccess( jsonElement.get( "name" ).getAsString(), UUIDUtil.convertUUID( jsonElement.get( "id" ).getAsString() ) );
            }
        }
    }

    private static JsonObject verifyToken( ConnectionHandler connectionHandler, String hash ) {
        try {
            String spec = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + connectionHandler.getVerifySession().getUsername() + "&serverId=" + hash + "&ip=" + connectionHandler.getIpAddress();
            URL url = new URL( spec );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String response = IOUtils.toString( connection.getInputStream() );
            if( response == null || response.isEmpty() ) {
                return null;
            }
            return Flex.getGson().fromJson( response, JsonObject.class );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private String createVerifyHash( SecretKey key ) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return new BigInteger( hashBytes(  "".getBytes(), key.getEncoded(), Flex.getServer().getKeyPair().getPublic().getEncoded() ) ).toString( 16 );
    }

    /**
     * stolen from vanilla server, ik shame on me
     */
    private static byte[] hashBytes( byte[]... byteArrays ) {
        try {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            for ( byte[] array : byteArrays ) {
                digest.update( array );
            }
            return digest.digest();
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
    }

}
