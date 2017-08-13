package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.world.Dimension;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.io.IOException;

/**
 * @author lukas
 * @since 07.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS35Respawn extends Message {

    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private String levelType;

    @Override
    public void read( ByteBuf buf ) throws IOException {
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeInt( dimension.getId() );
        buf.writeByte( difficulty.ordinal() );
        buf.writeByte( gameMode.ordinal() );
        writeString( levelType, buf );
    }
}
