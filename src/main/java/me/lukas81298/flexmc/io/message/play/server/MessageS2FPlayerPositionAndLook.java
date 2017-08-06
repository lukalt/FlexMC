package me.lukas81298.flexmc.io.message.play.server;

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
public class MessageS2FPlayerPositionAndLook extends Message {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private byte flags;
    private int teleportId;

    @Override
    public void read( ByteBuf buf ) throws IOException {
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        buf.writeDouble( x );
        buf.writeDouble( y );
        buf.writeDouble( z );
        buf.writeFloat( yaw );
        buf.writeFloat( pitch );
        buf.writeByte( flags );
        writeVarInt( teleportId, buf );
    }
}
