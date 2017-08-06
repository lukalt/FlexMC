package me.lukas81298.flexmc.io.message.play.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.*;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.world.ChunkColumn;
import me.lukas81298.flexmc.world.ChunkSection;

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
public class MessageS20ChunkData extends Message {

    private static final int COLUMN_WIDTH = 16, COLUMN_HEIGHT = 256;
    private static final int SECTION_SIZE = 16;
    private ChunkColumn chunkColumnColumn;

    @Override
    public void read( ByteBuf buf ) throws IOException {
        // nothing here
    }

    @Override
    public void write( ByteBuf buf ) throws IOException {

        buf.writeInt( chunkColumnColumn.getX() );
        buf.writeInt( chunkColumnColumn.getZ() );
        buf.writeBoolean( true ); // full chunk

        int mask = 0;
        ByteBuf data = Unpooled.buffer();
        for ( int sectionY = 0; sectionY < COLUMN_HEIGHT / SECTION_SIZE; sectionY++ ) {
            mask |= 1 << sectionY;
            ChunkSection chunkSection = chunkColumnColumn.getSections()[ sectionY ];
            try {
                chunkSection.write( data );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        writeVarInt( mask, buf );
        writeVarInt( data.readableBytes(), buf );
        buf.writeBytes( data );
        writeVarInt( 0, buf );

    }

}
