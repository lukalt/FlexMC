package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.entity.metadata.EntityMetaData;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;
import java.util.UUID;

/**
 * @author lukas
 * @since 08.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS03SpawnMob extends Message {

    private int entityId;
    private UUID uuid;
    private int type;
    private double x,y,z;
    private float yaw, pitch, headPitch;
    private short velX, velY, velZ;
    private EntityMetaData entityMetaData;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( entityId, buf );
        writeUUID( uuid, buf );
        writeVarInt( type, buf );
        writeDoubles( buf, x, y, z );
        writeBytes( buf, toAngle( yaw ), toAngle( pitch ), toAngle( headPitch ) );
        writeShorts( buf, velX, velY, velZ );
        entityMetaData.write( buf );
    }
}
