package me.lukas81298.flexmc;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.play.server.MessageS2EPlayerList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lukas
 * @since 05.08.2017
 */
public class PlayerManager {

    private final Map<UUID, Player> players = new ConcurrentHashMap<>();

    public Player getPlayer( UUID uuid ) {
        return this.players.get( uuid );
    }

    public Player getPlayer( String name ) {
        name = name.toLowerCase();
        for( Player player : this.players.values() ) {
            if ( player.getName().toLowerCase().equals( name ) ) {
                return player;
            }
        }
        return null;
    }

    public Collection<Player> getOnlinePlayers() {
        return this.players.values();
    }

    public void handlePlayerJoin( Player player ) {
        this.players.put( player.getUuid(), player );
        List<MessageS2EPlayerList.PlayerItem> items = new ArrayList<>();
        for ( Player target : this.getOnlinePlayers() ) {
            target.sendMessage( "§e" + player.getName() + " joined the game." );
            if( !target.equals( player ) ) {
                target.getConnectionHandler().sendMessage( new MessageS2EPlayerList( MessageS2EPlayerList.Action.ADD_PLAYER,
                        Collections.singletonList( new MessageS2EPlayerList.PlayerItem().setUuid( player.getUuid() ).setName( player.getName() ).setGameMode( player.getGameMode() ).setPing( player.getLatency() ) ) ) );
            }
            items.add( new MessageS2EPlayerList.PlayerItem().setName( target.getName() ).setUuid( target.getUuid() ).setGameMode( target.getGameMode() ).setPing( target.getLatency() ) );
        }
        player.getConnectionHandler().sendMessage( new MessageS2EPlayerList( MessageS2EPlayerList.Action.ADD_PLAYER, items ) );
        System.out.println( player.getName() + " (" + player.getUuid().toString() + ") logged in from " + player.getIpAddress() );
        player.spawnPlayer();
        player.getWorld().addEntity( player );
    }

    public void handlePlayerQuit( Player player ) {
        if( this.players.remove( player.getUuid() ) != null ) {
            this.getOnlinePlayers().forEach( (t) -> {
                t.sendMessage( "§e" + player.getName() + " left the game" );
                t.getConnectionHandler().sendMessage( new MessageS2EPlayerList( MessageS2EPlayerList.Action.REMOVE_PLAYER, Collections.singletonList( new MessageS2EPlayerList.PlayerItem().setUuid( player.getUuid() ) ) ) );
            } );
            System.out.println( player.getName() + " has disconnected" );
            player.getWorld().removeEntity( player );
        }
    }
}
