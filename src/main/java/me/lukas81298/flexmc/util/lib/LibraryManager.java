package me.lukas81298.flexmc.util.lib;

import net.cubespace.Yamler.Config.InvalidConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class LibraryManager {

    private final File folder;
    private final LibraryConfig config;

    public LibraryManager() {

        this.folder = new File( "libs" );
        if ( !this.folder.exists() ) {
            this.folder.mkdir();
        }

        this.config = new LibraryConfig( new File( this.folder, "libraries.yml" ) );
        try {
            this.config.init();
        } catch ( InvalidConfigurationException e ) {
            e.printStackTrace();
        }

    }

    public void init() {
        System.out.println( "Loading libraries, please wait..." );
        for ( Library library : this.config.getLibraryList() ) {
            loadLibrary( library );
        }
        try {
            config.save();
        } catch ( InvalidConfigurationException e ) {
            e.printStackTrace();
        }
    }

    private void loadLibrary( Library library ) {
        File targetFile = new File( folder, library.getKey() + ".jar" );
        boolean download = false;
        if ( !targetFile.exists() ) {
            download = true;
        } else {
            try {
                String hash = hash( targetFile );
                System.out.println( targetFile.getName() + " has been hashed to " + hash );
                if ( !hash.equals( library.getHash() ) ) {
                    System.out.println( "Hashes did not match, attempting to redownload from " + library.getUrl() );
                    download = true;
                }
            } catch ( IOException e ) {
                e.printStackTrace();
                download = true;
            }
        }
        if ( download ) {
            System.out.println( "Downloading library " + library.getKey() );
            try {
                URL url = new URL( library.getUrl() );
                ReadableByteChannel r = Channels.newChannel( url.openStream() );
                FileOutputStream outputStream = new FileOutputStream( targetFile );
                outputStream.getChannel().transferFrom( r, 0, Long.MAX_VALUE );
                String hash = hash( targetFile );
                System.out.println( targetFile.getName() + " hashed to " + hash );
                if ( library.getHash().isEmpty() ) {
                    library.setHash( hash );
                } else if ( !hash.equals( library.getHash() ) ) {
                    System.out.println( "Hashes did not match" );
                    targetFile.deleteOnExit();
                    System.exit( 0 );
                    return;
                }


            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        try {
            this.loadJarToClassPath( targetFile );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void loadJarToClassPath( File file ) throws IOException {
        URL url = file.toURI().toURL();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        try {
            Method method = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
            if( !method.isAccessible() ) {
                method.setAccessible( true );
            }
            method.invoke( systemClassLoader, url );
            System.out.println( "added " + url.toString() + " to classpath" );
        } catch ( NoSuchMethodException | InvocationTargetException | IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    private String hash( File file ) throws IOException {
        try ( FileInputStream inputStream = new FileInputStream( file ) ) {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance( "SHA-256" );
            int read;
            do {
                read = inputStream.read( buffer );
                if ( read > 0 ) {
                    complete.update( buffer, 0, read );
                }
            } while ( read != -1 );

            byte[] data = complete.digest();
            StringBuilder builder = new StringBuilder( data.length );
            for ( int i = 0; i < data.length; i++ ) {
                builder.append( Integer.toString( ( data[i] & 0xff ) + 0x100, 16 ).substring( 1 ) );
            }
            return builder.toString();
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
    }
}
