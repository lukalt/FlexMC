package me.lukas81298.flexmc.io.listener;

import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;

/**
 * @author lukas
 * @since 04.08.2017
 */
public interface MessageInboundListener<K extends Message>  {

    void handle( ConnectionHandler connectionHandler, K message );
}
