package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.message.Message;
import net.md_5.bungee.api.chat.BaseComponent;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lukas
 * @since 08.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS0CBossBar extends Message {

    private UUID uuid;
    private Action action;

    private BaseComponent title;
    private float health;
    private Color color;
    private StupidNotchesUnit division;
    private byte flags;

    public enum Color {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE
    }

    public enum StupidNotchesUnit {
        DIV_NO,
        DIV_6,
        DIV_10,
        DIV_12,
        DIV_20
    }

    public enum Action {
        ADD,
        REMOVE,
        UPDATE_HEALTH,
        UPDATE_TITLE,
        UPDATE_STYLE,
        UPDATE_FLAG
    }

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeUUID( uuid, buf );
        writeVarInt( action.ordinal(), buf );
        switch ( action ) {
            case ADD:
                writeString( Flex.getGson().toJson( title ), buf );
                buf.writeFloat( health );
                writeVarInt( color.ordinal(), buf );
                writeVarInt( division.ordinal(), buf );
                buf.writeByte( flags );
                break;
            case REMOVE:
                break;
            case UPDATE_HEALTH:
                buf.writeFloat( health );
                break;
            case UPDATE_TITLE:
                writeString( Flex.getGson().toJson( title ), buf );
                break;
            case UPDATE_STYLE:
                writeVarInt( color.ordinal(), buf );
                writeVarInt( division.ordinal(), buf );
                break;
            case UPDATE_FLAG:
                buf.writeByte( flags );
                break;
        }
    }
}
