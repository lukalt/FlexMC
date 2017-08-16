package me.lukas81298.flexmc.io.message.play.client;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 17.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageC20UseItem extends Message {

    private int hand;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        hand = readVarInt( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( hand, buf );
    }
}
