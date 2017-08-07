package me.lukas81298.flexmc.io.message;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.ToString;

/**
 * @author lukas
 * @since 04.08.2017
 */
public abstract class MessageRegistry {

    private final TIntObjectMap<Class<? extends Message>> idToMessage = new TIntObjectHashMap<>();
    private final TObjectIntMap<Class<? extends Message>> messageToId = new TObjectIntHashMap<>( 10, .75F, -1 );

    public abstract void register();

    protected void registerServerMessage( int id, Class<? extends Message> messageClass ) {
        try {
             messageClass.newInstance();
        } catch ( Throwable t ) {
            System.out.println( "Cannot register " + messageClass.getSimpleName() + ", probably @NoArgsConstructor is missing" );
            t.printStackTrace();
        }

        this.messageToId.put( messageClass, id );
    }

    protected void registerClientMessage( int id, Class<? extends Message> messageClass ) {
        try {
            messageClass.newInstance();
        } catch ( Throwable t ) {
            System.out.println( "Cannot register " + messageClass.getSimpleName() + ", probably @NoArgsConstructor is missing" );
            t.printStackTrace();
        }

        this.idToMessage.put( id, messageClass );
    }



    public Message createMessageFromId( int id ) throws IllegalAccessException, InstantiationException {
        Class<? extends Message> clazz = this.idToMessage.get( id );
        if( clazz == null ) {
            return null;
        }
        return clazz.newInstance();
    }

    public int getMessageIdFromClass( Class<? extends Message> clazz ) {
        return this.messageToId.get( clazz );
    }
}
