package me.lukas81298.flexmc.io.message.status.server;

import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 04.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS00ResponseStatus extends Message {

    private String response;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        response = readString( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeString( response, buf );
    }
}
