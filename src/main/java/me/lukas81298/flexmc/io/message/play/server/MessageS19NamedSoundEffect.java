package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import org.bukkit.Location;

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
public class MessageS19NamedSoundEffect extends Message {

    private String name;
    private String category;
    private Location position;
    private float volume, pitch;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeString( name, buf );
        writeString( category, buf );
        buf.writeInt( (int) (position.getX() * 8 ) );
        buf.writeInt( (int) (position.getY() * 8 ) );
        buf.writeInt( (int) (position.getZ() * 8 ) );
        writeFloats( buf, volume, pitch );
    }
}
