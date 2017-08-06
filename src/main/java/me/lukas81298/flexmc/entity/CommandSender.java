package me.lukas81298.flexmc.entity;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

/**
 * @author lukas
 * @since 06.08.2017
 */
public interface CommandSender {

    void sendMessage( String... messages );

    void sendMessage( BaseComponent... components );

    UUID getUniqueId();

    String getName();

    boolean hasPermission( String permission );

}
