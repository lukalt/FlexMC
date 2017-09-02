package me.lukas81298.flexmc.config;

import lombok.Getter;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;

/**
 * @author lukas
 * @since 04.08.2017
 */
@Getter
public class MainConfig extends YamlConfig {

    private String serverAddress = "212.224.77.73";
    private int serverPort = 25565;

    private int acceptorThreads = 2;
    private int handlerThreads = 10;

    private int maxPlayers = 20;

    private String serverName = "HELLO!";

    private boolean verifyUsers = false;

    public MainConfig( File file ) {
        CONFIG_FILE = new File( file, "config.yml" );
    }

}
