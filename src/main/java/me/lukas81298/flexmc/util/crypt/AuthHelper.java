package me.lukas81298.flexmc.util.crypt;

import me.lukas81298.flexmc.Flex;
import sun.security.jca.JCAUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class AuthHelper {

    private final static String KEY_PAIR_ALGO = "RSA";
    private final static int KEY_PAIR_KEY_LENGTH = 1024;
    private final static SecureRandom RANDOM = JCAUtil.getSecureRandom();
    private final static int VERIFY_TOKEN_LENGTH = 4;
    private final static String CIPHER_TYPE = "AES/CFB8/NoPadding";

    public static KeyPair generateServerKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance( KEY_PAIR_ALGO );
        generator.initialize( KEY_PAIR_KEY_LENGTH, RANDOM );
        return generator.generateKeyPair();
    }

    public static byte[] nextToken() {
        return randomByteArray( VERIFY_TOKEN_LENGTH );
    }

    private static byte[] randomByteArray( int length ) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes( bytes );
        return bytes;
    }

    public static Cipher createCipher( boolean encrypt, SecretKey secretKey ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance( CIPHER_TYPE );
        cipher.init( encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec( secretKey.getEncoded() ) );
        return cipher;
    }

    public static Cipher createDecryptCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance( KEY_PAIR_ALGO );
        reInitCipher( cipher );
        return cipher;
    }

    public static void reInitCipher( Cipher cipher ) throws InvalidKeyException {
        cipher.init( Cipher.DECRYPT_MODE, Flex.getServer().getKeyPair().getPrivate() );
    }

}
