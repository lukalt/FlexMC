package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.Vector3i;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lukas
 * @since 07.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS04SpawnPainting extends Message {

    private int entityId;
    private UUID entityUUID;
    private String title;
    private Vector3i position;
    private byte direction;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( entityId, buf );
        writeUUID( entityUUID, buf );
        writeString( title, buf );
        buf.writeLong( position.asLong() );
        buf.writeByte( direction );
    }
}
