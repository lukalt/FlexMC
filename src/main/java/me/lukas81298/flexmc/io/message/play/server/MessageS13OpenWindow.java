package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.message.Message;
import net.md_5.bungee.api.chat.BaseComponent;

import java.io.IOException;

/**
 * @author lukas
 * @since 08.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS13OpenWindow extends Message {

    private byte windowId;
    private String type;
    private BaseComponent title;
    private byte slots;
    private int entityId;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeByte( windowId );
        writeString( type, buf );
        writeString( Flex.getGson().toJson( title ), buf );
        buf.writeByte( slots );
        if( type.equals( "EntityHorse" ) ) {
            buf.writeInt( entityId );
        }
    }
}
