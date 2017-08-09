package me.lukas81298.flexmc.io.listener.login;

import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.login.client.MessageC00LoginStart;
import me.lukas81298.flexmc.io.message.login.server.MessageS01EncryptionRequest;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.VerifySession;
import me.lukas81298.flexmc.util.crypt.AuthHelper;

import java.util.UUID;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class MessageClientLoginListener implements MessageInboundListener<MessageC00LoginStart> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00LoginStart message ) {
        if( connectionHandler.getVerifySession() != null ) {
            System.out.println( "Already authenticating" );
            return;
        }

        if( Flex.getServer().getConfig().isVerifyUsers() ) {
            VerifySession verifySession = new VerifySession( AuthHelper.nextToken(), message.getName() );
            connectionHandler.setVerifySession( verifySession );
            MessageS01EncryptionRequest encryptionRequest = new MessageS01EncryptionRequest( "", Flex.getServer().getKeyPair().getPublic().getEncoded(), verifySession.getToken() );
            connectionHandler.sendMessage( encryptionRequest );
            verifySession.getState().incrementAndGet();
        } else {
            connectionHandler.loginSuccess( message.getName(), UUID.randomUUID() );
        }
    }

}
