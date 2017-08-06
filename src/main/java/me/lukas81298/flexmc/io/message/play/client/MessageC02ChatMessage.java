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
public class MessageC02ChatMessage extends Message {

    private String message;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        message = readString( buf );
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeString( message, buf );
    }

}
