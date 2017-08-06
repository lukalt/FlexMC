package me.lukas81298.flexmc.util;

import lombok.*;

/**
 * @author lukas
 * @since 05.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode( callSuper = false )
public class Vector3i {

    private int x, y, z;

    public static Vector3i fromLong( long val ) {
        return new Vector3i( (int) ( val >> 38 ), (int) ( ( val >> 26 ) & 0xFFF ), (int) ( val << 38 >> 38 ) );
    }

    public long asLong() {
        return ( ( (long) x & 0x3FFFFFF ) << 38 | ( y & 0xFFF ) << 26 | z & 0x3FFFFFF );

    }

}
