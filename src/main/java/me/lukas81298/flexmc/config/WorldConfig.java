package me.lukas81298.flexmc.config;

import lombok.Getter;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author lukas
 * @since 04.08.2017
 */
@Getter
public class WorldConfig extends YamlConfig {

    private int viewDistance = 8;
    private String worldContainer = "worlds";
    private List<WorldConfigEntry> worlds = Arrays.asList(
            new WorldConfigEntry( "world", "OVERWORLD" ),
            new WorldConfigEntry( "world_the_nether", "NETHER" ),
            new WorldConfigEntry( "world_the_end", "END" )
    );

    public WorldConfig( File folder ) {
        CONFIG_FILE = new File( folder, "worlds.yml" );
    }
}
