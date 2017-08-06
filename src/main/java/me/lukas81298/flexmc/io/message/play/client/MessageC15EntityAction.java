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
public class MessageC15EntityAction extends Message {

    private int entityId;
    private int actionId;
    private int jumpBoost;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        entityId = readVarInt( buf );
        actionId = readVarInt( buf );
        jumpBoost = readVarInt( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
