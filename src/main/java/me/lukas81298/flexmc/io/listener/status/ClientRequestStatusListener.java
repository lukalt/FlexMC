package me.lukas81298.flexmc.io.listener.status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.FlexServer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.status.client.MessageC00RequestStatus;
import me.lukas81298.flexmc.io.message.status.server.MessageS00ResponseStatus;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class ClientRequestStatusListener implements MessageInboundListener<MessageC00RequestStatus> {

    private final Gson gson = new Gson();

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00RequestStatus message ) {

        JsonObject version = new JsonObject();
        version.addProperty( "name", "1.12.1" );
        version.addProperty( "protocol", 338 );
        JsonObject players = new JsonObject();
        FlexServer server = Flex.getServer();
        players.addProperty( "max", server.getConfig().getMaxPlayers() );
        players.addProperty( "online", 0 );
        JsonObject description = new JsonObject();
        description.addProperty( "text", server.getConfig().getServerName() );
        JsonObject o = new JsonObject();
        o.add( "version", version );
        o.add( "players", players );
        o.add( "description", description );
        String s = gson.toJson( o );
        System.out.println( s );
        connectionHandler.sendMessage( new MessageS00ResponseStatus( s ) );
        System.out.println( "sending response" );
    }

}
