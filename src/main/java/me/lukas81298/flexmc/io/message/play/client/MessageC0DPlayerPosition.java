package me.lukas81298.flexmc.io.message.play.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

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
public class MessageC0DPlayerPosition extends Message {

    private double x;
    private double y;
    private double z;
    private boolean onGround;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        onGround = buf.readBoolean();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
