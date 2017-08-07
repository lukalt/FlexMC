package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

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
public class MessageS00SpawnObject extends Message {

    private int entityId;
    private UUID uuid;
    private byte type;
    private double x,y,z;
    private float pitch,yaw;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( entityId, buf );
        writeUUID( uuid, buf );
        buf.writeByte( type );
        buf.writeDouble( x );
        buf.writeDouble( y );
        buf.writeDouble( z );
        buf.writeByte( toAngle( yaw ) );
        buf.writeByte( toAngle( pitch ) );
        buf.writeInt( 1 );
        buf.writeShort( 1 );
        buf.writeShort( 1 );
        buf.writeShort( 1 );
    }
}
