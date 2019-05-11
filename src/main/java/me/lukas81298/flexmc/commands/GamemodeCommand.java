package me.lukas81298.flexmc.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author lukas
 * @since 11.05.2019
 */
public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super( "gamemode" );
    }

    @Override
    public boolean execute( CommandSender commandSender, String s, String[] strings ) {
        if( strings.length == 0 ) {
            commandSender.sendMessage( "§c/gamemode <0,1,2,3>" );
            return true;
        }
        ((Player) commandSender).setGameMode( GameMode.values()[Integer.parseInt( strings[0] )] );
        return true;
    }
}
