package me.lukas81298.flexmc.world;

import lombok.Getter;
import me.lukas81298.flexmc.config.WorldConfig;
import net.cubespace.Yamler.Config.InvalidConfigurationException;

import java.io.File;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class WorldManager {

    @Getter private final WorldConfig config;

    public WorldManager() {
        this.config = new WorldConfig( new File( "config" ) );
        try {
            this.config.init();
        } catch ( InvalidConfigurationException e ) {
            e.printStackTrace();
        }
    }
}
