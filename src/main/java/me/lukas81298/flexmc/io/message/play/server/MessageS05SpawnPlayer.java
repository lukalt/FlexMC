package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.entity.metadata.EntityMetaData;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lukas
 * @since 06.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
@ToString
public class MessageS05SpawnPlayer extends Message {

    private int entityId;
    private UUID uuid;
    private double x,y,z;
    private float yaw, pitch;
    private EntityMetaData metaData;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( entityId, buf );
        writeUUID( uuid, buf );
        buf.writeDouble( x );
        buf.writeDouble( y );
        buf.writeDouble( z );
        buf.writeFloat( toAngle( yaw ) );
        buf.writeFloat( toAngle( pitch ) ) ;
        metaData.write( buf );
    }
}
