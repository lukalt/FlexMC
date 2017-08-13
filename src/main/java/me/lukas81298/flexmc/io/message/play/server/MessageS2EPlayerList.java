package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import lombok.experimental.Accessors;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.message.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.GameMode;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author lukas
 * @since 05.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS2EPlayerList extends Message {

    private Action action;
    private List<PlayerItem> players;

    public enum Action {
        ADD_PLAYER,
        UPDATE_GAMEMODE,
        UPDATE_PING,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( action.ordinal(), buf );
        writeVarInt( players.size(), buf );
        for ( PlayerItem player : players ) {
            writeUUID( player.uuid, buf );
            switch ( action ) {
                case ADD_PLAYER:
                    writeString( player.name, buf );
                    writeVarInt( 0, buf );
                    writeVarInt( player.gameMode.ordinal(), buf );
                    writeVarInt( player.ping, buf );
                    buf.writeBoolean( player.displayName != null );
                    if ( player.displayName != null ) {
                        writeString( Flex.getGson().toJson( player.displayName ), buf );
                    }
                    break;
                case UPDATE_GAMEMODE:
                    writeVarInt( player.gameMode.ordinal(), buf );
                    break;
                case UPDATE_PING:
                    writeVarInt( player.ping, buf );
                    break;
                case UPDATE_DISPLAY_NAME:
                    buf.writeBoolean( player.displayName != null );
                    if ( player.displayName != null ) {
                        writeString( Flex.getGson().toJson( player.displayName ), buf );
                    }
                    break;
                case REMOVE_PLAYER:
                    // nothing here, but intellij fucks me off when I dont handle this case so here is a pretty useful comment
                    break;
            }
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Accessors( chain = true )
    public static class PlayerItem {

        private String name;
        private UUID uuid;
        private GameMode gameMode;
        private int ping;
        private BaseComponent displayName;
    }
}
