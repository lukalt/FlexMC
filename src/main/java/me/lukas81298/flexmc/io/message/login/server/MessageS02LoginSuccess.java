package me.lukas81298.flexmc.io.message.login.server;

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
public class MessageS02LoginSuccess extends Message {

    private String name;
    private String uuid;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        uuid = readString( buf );
        name = readString( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeString( uuid, buf );
        writeString( name, buf );
    }
}
