package me.lukas81298.flexmc.io.listener.login;

import com.google.common.base.Charsets;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.login.client.MessageC00LoginStart;
import me.lukas81298.flexmc.io.message.login.server.MessageS00Disconnect;
import me.lukas81298.flexmc.io.message.login.server.MessageS01EncryptionRequest;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import me.lukas81298.flexmc.util.VerifySession;
import me.lukas81298.flexmc.util.crypt.AuthHelper;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class MessageClientLoginListener implements MessageInboundListener<MessageC00LoginStart> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00LoginStart message ) {
        String m = this.tryLogin( connectionHandler, message );
        if ( m != null ) {
            connectionHandler.sendMessage( new MessageS00Disconnect( "{\"text\":\"" + m + "\"}" ) );
        }
    }

    private String tryLogin( ConnectionHandler connectionHandler, MessageC00LoginStart message ) {
        if ( connectionHandler.getVersion() > 338 ) {
            return "Outdated Server. Please use 1.12.1";
        }
        if ( connectionHandler.getVersion() < 338 ) {
            return "Outdated Client. Please use 1.12.1";
        }
        if ( connectionHandler.getVerifySession() != null ) {
            return "Already logging in";
        }
        if ( !Flex.getServer().isRunning() || Flex.getServer().getWorld() == null || !Flex.getServer().getWorld().isGenerated() ) {
            return "Server ist still starting up";
        }
        AsyncPlayerPreLoginEvent event = EventFactory.call( new AsyncPlayerPreLoginEvent( message.getName(), connectionHandler.getSocketAddress().getAddress(), UUID.randomUUID() ) );

        if( event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED ) {
            return event.getKickMessage() == null ? "null" : event.getKickMessage();
        }

        if ( Flex.getServer().getConfig().isVerifyUsers() ) {
            VerifySession verifySession = new VerifySession( AuthHelper.nextToken(), message.getName() );
            connectionHandler.setVerifySession( verifySession );
            MessageS01EncryptionRequest encryptionRequest = new MessageS01EncryptionRequest( "", Flex.getServer().getKeyPair().getPublic().getEncoded(), verifySession.getToken() );
            connectionHandler.sendMessage( encryptionRequest );
            verifySession.getState().incrementAndGet();
        } else {
            connectionHandler.loginSuccess( message.getName(), UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + message.getName() ).getBytes( Charsets.UTF_8 ) ) );
        }
        return null;
    }
}
