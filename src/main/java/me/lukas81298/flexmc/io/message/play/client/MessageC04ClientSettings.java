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
public class MessageC04ClientSettings extends Message {

    private String locale;
    private byte viewDistance;
    private int chatMode;
    private boolean chatColors;
    private byte skinParts;
    private int mainHand;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        locale = readString( buf );
        viewDistance = buf.readByte();
        chatMode = readVarInt( buf );
        chatColors = buf.readBoolean();
        skinParts = buf.readByte();
        mainHand = readVarInt( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

    }
}
