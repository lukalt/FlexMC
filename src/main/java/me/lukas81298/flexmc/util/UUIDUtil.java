package me.lukas81298.flexmc.util;

import java.util.UUID;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class UUIDUtil {

    public static UUID convertUUID( String uuid ) {
        if ( uuid.length() == 36 ) {
            return UUID.fromString( uuid );
        }

        return UUID.fromString( uuid.substring( 0, 8 ) + "-" + uuid.substring( 8, 12 ) + "-" + uuid.substring( 12, 16 ) + "-" + uuid.substring( 16, 20 ) + "-" + uuid.substring( 20, 32 ) );
    }

}
