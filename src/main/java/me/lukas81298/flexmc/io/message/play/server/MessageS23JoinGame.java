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
 * @since 05.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageS23JoinGame extends Message {

    private int entityId;
    private GameMode gameMode;
    private Dimension dimension;
    private Difficulty difficulty;
    private String levelType;
    private boolean reducedDebugInfo;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeInt( entityId );
        buf.writeByte( gameMode.getValue() );
        buf.writeInt( dimension.getId() );
        buf.writeByte( difficulty.ordinal() );
        buf.writeByte( 0 ); // not used anymore
        writeString( levelType, buf );
        buf.writeBoolean( reducedDebugInfo );
    }
}
