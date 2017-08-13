package me.lukas81298.flexmc.util.lib;

import lombok.*;
import net.cubespace.Yamler.Config.YamlConfig;

/**
 * @author lukas
 * @since 13.08.2017
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class Library extends YamlConfig {

    private String url;
    private String groupId;
    private String artifactId;
    private String version;
    @Setter private String hash;

    public String getKey() {
        return ( groupId + "_" + artifactId + "_" + version ).replace( ".", "_" );
    }
}
