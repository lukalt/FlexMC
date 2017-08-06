package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 06.08.2017
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageS32DestroyEntities extends Message {

    private int[] entityIds;

    public MessageS32DestroyEntities( int... entityIds ) {
        this.entityIds = entityIds;
    }

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( entityIds.length, buf );
        for ( int entityId : entityIds ) {
            writeVarInt( entityId, buf );
        }
    }
}
