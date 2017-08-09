package me.lukas81298.flexmc.io.message.play.server;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.procedure.TObjectIntProcedure;
import io.netty.buffer.ByteBuf;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;

import java.io.IOException;

/**
 * @author lukas
 * @since 09.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = false )
public class MessageS07Statistics extends Message {

    private TObjectIntMap<String> values;

    @Override
    public void read( ByteBuf buf ) throws IOException {

    }

    @Override
    public void write( ByteBuf buf ) throws IOException {
        writeVarInt( values.size(), buf );
        values.forEachEntry( new TObjectIntProcedure<String>() {
            @Override
            public boolean execute( String s, int i ) {
                writeString( s, buf );
                writeVarInt( i, buf );
                return true;
            }
        } );
    }
}
