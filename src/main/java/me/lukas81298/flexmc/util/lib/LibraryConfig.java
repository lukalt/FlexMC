package me.lukas81298.flexmc.util.lib;

import lombok.Getter;
import lombok.ToString;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lukas
 * @since 13.08.2017
 */
@Getter
@ToString
public class LibraryConfig extends YamlConfig {

    private List<Library> libraryList = new ArrayList<>();

    public LibraryConfig( File file ) {
        CONFIG_FILE = file;
        libraryList.add( new Library( "http://repo1.maven.org/maven2/com/google/guava/guava/21.0/guava-21.0.jar", "com.google.guava", "guava", "21.0", "" ) );
        libraryList.add( new Library( "http://repo1.maven.org/maven2/org/apache/commons/commons-io/1.3.2/commons-io-1.3.2.jar", "org.apache.commons", "commons-io", "1.3.2", "" ) );
        libraryList.add( new Library( "http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.1/gson-2.8.1.jar", "com.google.code.gson", "gson", "2.8.1", "" ) );
        libraryList.add( new Library( "http://central.maven.org/maven2/io/netty/netty-all/4.1.5.Final/netty-all-4.1.5.Final.jar", "io.netty", "netty-all", "4.1.5.Final", "" ) );
        libraryList.add( new Library( "http://central.maven.org/maven2/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar", "net.sf.trove4j", "trove4j", "3.0.3", "" ) );
        libraryList.add( new Library( "https://oss.sonatype.org/content/repositories/snapshots/net/md-5/bungeecord-chat/1.12-SNAPSHOT/bungeecord-chat-1.12-20170813.024237-27.jar", "net.md-5", "bungeecird-chat", "1.12-SNAPSHOT", "" ) );
        libraryList.add( new Library( "https://oss.sonatype.org/service/local/repositories/releases/content/com/github/czyzby/noise4j/0.1.0/noise4j-0.1.0.jar", "com.github.czyzby", "noise4j", "0.1.0", "" ) );
        libraryList.add( new Library( "https://hub.spigotmc.org/nexus/content/repositories/snapshots/org/bukkit/bukkit/1.12.1-R0.1-SNAPSHOT/bukkit-1.12.1-R0.1-20170812.230806-16.jar", "org.bukkit", "bukkit", "1.12.1-R.01.-SNAPSHOT", "" ) );
        libraryList.add( new Library( "http://central.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar", "com.googlecode.json-simple", "json-simple", "1.1.1", "" ) );
        libraryList.add( new Library( "http://central.maven.org/maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.jar", "org.apache.commons", "commons-lang", "2.6", "" ) );
    }

}
