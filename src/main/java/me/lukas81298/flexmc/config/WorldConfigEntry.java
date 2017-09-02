package me.lukas81298.flexmc.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.cubespace.Yamler.Config.YamlConfig;

/**
 * @author lukas
 * @since 04.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorldConfigEntry extends YamlConfig {

    private String name;
    private String environment;

}
