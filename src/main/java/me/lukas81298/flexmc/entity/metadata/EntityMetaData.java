package me.lukas81298.flexmc.entity.metadata;

import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.procedure.TByteObjectProcedure;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class EntityMetaData {

    @RequiredArgsConstructor
    private final class EntryNode {
        private final MetaDataType type;
        private final Object value;
    }

    private final TByteObjectMap<EntryNode> data = new TByteObjectHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void set( byte index, MetaDataType type, Object value ) {
        this.lock.writeLock().lock();
        try {
            if( value == null ) {
                this.data.remove( index );
            } else {
                this.data.put( index, new EntryNode( type, value ) );
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public <T> T get( byte index ) {
        EntryNode node = this.data.get( index );
        return node == null ? null : (T) node.value;
    }

    public void write( ByteBuf buf ) {
        this.lock.readLock().lock();
        try {
            this.data.forEachEntry( new TByteObjectProcedure<EntryNode>() {
                @Override
                public boolean execute( byte index, EntryNode entryNode ) {
                    buf.writeByte( index );
                    buf.writeByte( entryNode.type.ordinal() );
                    entryNode.type.getWriter().accept( buf, entryNode.value ); // serialize the value according to type
                    return true;
                }
            } );
        } finally {
            this.lock.readLock().unlock();
        }
        buf.writeByte( 0xFF );
    }
}
