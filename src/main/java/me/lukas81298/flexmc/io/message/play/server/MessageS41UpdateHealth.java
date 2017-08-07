package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

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
public class MessageS41UpdateHealth extends Message {

    private float health;
    private int food;
    private float foodSaturation;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeFloat( health );
        writeVarInt( food, buf );
        buf.writeFloat( foodSaturation );
    }
}
