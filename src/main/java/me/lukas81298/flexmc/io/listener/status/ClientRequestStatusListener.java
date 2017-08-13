package me.lukas81298.flexmc.io.listener.status;

import com.google.gson.JsonObject;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.FlexServer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.status.client.MessageC00RequestStatus;
import me.lukas81298.flexmc.io.message.status.server.MessageS00ResponseStatus;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class ClientRequestStatusListener implements MessageInboundListener<MessageC00RequestStatus> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC00RequestStatus message ) {
        JsonObject version = new JsonObject();
        version.addProperty( "name", "1.12.1" );
        version.addProperty( "protocol", 338 );
        JsonObject players = new JsonObject();
        FlexServer server = Flex.getServer();
        ServerListPingEvent event = EventFactory.call( new ServerListPingEvent( connectionHandler.getSocketAddress().getAddress(), server.getConfig().getServerName(), server.getPlayerManager().getOnlinePlayers().size(), server.getConfig().getMaxPlayers() ) );
        players.addProperty( "max", event.getMaxPlayers() );
        players.addProperty( "online", event.getNumPlayers() );
        JsonObject description = new JsonObject();
        description.addProperty( "text", event.getMotd() );
        JsonObject o = new JsonObject();
        o.add( "version", version );
        o.add( "players", players );
        o.add( "description", description );

        connectionHandler.sendMessage( new MessageS00ResponseStatus( Flex.getGson().toJson( o ) ) );

    }

}
