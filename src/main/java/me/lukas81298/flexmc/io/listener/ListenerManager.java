package me.lukas81298.flexmc.io.listener;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.io.protocol.ProtocolState;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class ListenerManager {

    private final TMap<Class<? extends Message>, List<MessageInboundListener>> registered = new THashMap<>();

    public synchronized void registerListener( MessageInboundListener<? extends Message> listener ) {
        Class<? extends MessageInboundListener> clazz = listener.getClass();
        for ( Type type : clazz.getGenericInterfaces() ) {
            if ( ParameterizedType.class.isAssignableFrom( type.getClass() ) ) {
                Type messageClass = ( (ParameterizedType) type ).getActualTypeArguments()[0];
                if ( Message.class.isAssignableFrom( (Class<?>) messageClass ) ) {
                    registered.computeIfAbsent( (Class<? extends Message>) messageClass, new Function<Class<? extends Message>, List<MessageInboundListener>>() {
                        @Override
                        public List<MessageInboundListener> apply( Class<? extends Message> aClass ) {
                            return new LinkedList<>();
                        }
                    } ).add( listener );
                }
            }
        }
    }

    public void invokeListeners( ConnectionHandler connectionHandler, Message message ) {
        List<MessageInboundListener> list = registered.get( message.getClass() );
        if ( list != null ) {
            if( connectionHandler.getProtocolState() == ProtocolState.PLAY ) {
                Flex.getServer().getPacketThreadExecutor().execute( new Runnable() {
                    @Override
                    public void run() {
                        list.forEach( ( listener ) -> {
                            try {
                                listener.handle( connectionHandler, message );
                            } catch ( Exception e ) {
                                e.printStackTrace();
                            }
                        } );

                    }
                } );
            } else {
                list.forEach( ( listener ) -> {
                    try {
                        listener.handle( connectionHandler, message );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                } );
            }
        }
    }


}
