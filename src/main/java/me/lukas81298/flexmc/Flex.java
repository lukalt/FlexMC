package me.lukas81298.flexmc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.lukas81298.flexmc.config.MainConfig;
import net.cubespace.Yamler.Config.InvalidConfigurationException;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class Flex {

    @Getter private static FlexServer server;
    @Getter private static Gson gson = new GsonBuilder().create();

    public static void main( String[] args ) {

        System.out.println( "Loading flex... Please wait" );
        File configFolder = new File( "config" );
        if( !configFolder.exists() ) {
            if( !configFolder.mkdir() ) {
                System.out.println( "Cannot create config folder " + configFolder.getAbsolutePath() + ". Stopping!" );
                return;
            }
        }
        MainConfig config = new MainConfig( configFolder );
        try {
            config.init();
        } catch ( InvalidConfigurationException e ) {
            e.printStackTrace();
            return;
        }


        long start = System.nanoTime();
        server = new FlexServer( config, configFolder );
        ListenableFuture<Void> future = server.start();
        try {
            future.get();
        } catch ( InterruptedException | ExecutionException e ) {
            e.printStackTrace();
        }

        System.out.println( "Server has started! Took " + Math.round( ( System.nanoTime() - start ) / 1000 ) / 1000 + "ms" );
        while ( server.isRunning() ) {
            try {
                Thread.sleep( 1000L );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }

    }

}
