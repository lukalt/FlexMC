package me.lukas81298.flexmc.io.listener.login;

import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.login.client.MessageC00LoginStart;
import me.lukas81298.flexmc.io.message.login.server.MessageS02LoginSuccess;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.io.protocol.ProtocolState;
import me.lukas81298.flexmc.util.*;

import java.util.UUID;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class MessageClientLoginListener implements MessageInboundListener<MessageC00LoginStart> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00LoginStart message ) {
        System.out.println( message.getName() );

        //connectionHandler.sendMessage( new MessageS00Disconnect( "{\"text\": \"test\"}" ) );
        connectionHandler.sendMessage( new MessageS02LoginSuccess( message.getName(), UUID.randomUUID().toString() ) );
        connectionHandler.setProtocolState( ProtocolState.PLAY );

        Player player = new Player( -1, new Location( 0, 75, 0 ), message.getName(), UUID.randomUUID(), connectionHandler, Flex.getServer().getWorld() );
        connectionHandler.setPlayer( player );
        Flex.getServer().getPlayerManager().handlePlayerJoin( player );

    }

}
