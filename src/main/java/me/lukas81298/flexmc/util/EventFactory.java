package me.lukas81298.flexmc.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class EventFactory {

    public static <K extends Event> K call( K event ) {
        Bukkit.getPluginManager().callEvent( event );
        return event;
    }
}
