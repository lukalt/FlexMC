package me.lukas81298.flexmc.util.bukkit;

import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author lukas
 * @since 02.09.2017
 */
public class Events {

    public static PlayerInteractEvent callPlayerInteractEvent( final FlexPlayer player, Action action, Block clickedBlock, BlockFace clickedFace ) {
        PlayerInteractEvent event = new PlayerInteractEvent( player, action, player.getItemInHand(), clickedBlock, clickedFace );
        Bukkit.getPluginManager().callEvent( event );
        return event;
    }
}
