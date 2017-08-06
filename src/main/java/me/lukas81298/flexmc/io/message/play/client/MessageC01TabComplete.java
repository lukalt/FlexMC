package me.lukas81298.flexmc.io.message.play.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

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
public class MessageC01TabComplete extends Message {

    private String text;
    private boolean assumeCommand;
    private Vector3i position;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        text = readString( buf );
        assumeCommand = buf.readBoolean();
        if( buf.readBoolean() ) {
            position = Vector3i.fromLong( buf.readLong() );
        }
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        // ignored
    }
}
