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
public class MessageC13PlayerAbilities extends Message {

    private byte flags;
    private float flyingSpeed;
    private float walkingSpeed;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        flags = buf.readByte();
        flyingSpeed = buf.readFloat();
        walkingSpeed = buf.readFloat();
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
