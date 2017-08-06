package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.message.Message;
import net.md_5.bungee.api.chat.BaseComponent;

import java.io.IOException;

/**
 * @author lukas
 * @since 05.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageS0FChatMessage extends Message {

    private String message;
    private byte position;

    public MessageS0FChatMessage( BaseComponent[] message, byte position ) {
        this.message = Flex.getGson().toJson( message );
        this.position = position;
    }

    @Override
    public void read( ByteBuf buf ) throws IOException {
        message = readString( buf );
        position = buf.readByte();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeString( message, buf );
        buf.writeByte( position );
    }
}
