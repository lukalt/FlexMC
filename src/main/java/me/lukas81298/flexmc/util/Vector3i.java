package me.lukas81298.flexmc.util;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.World;

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

    public Vector3i( Location location ) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public static Vector3i fromLong( long val ) {
        return new Vector3i( (int) ( val >> 38 ), (int) ( ( val >> 26 ) & 0xFFF ), (int) ( val << 38 >> 38 ) );
    }

    public Vector3i add( int i, int j, int k ) {
        return new Vector3i( x + i, y + j, z + k );
    }

    public long asLong() {
        return ( ( x & 0x3FFFFFFL ) << 38L | ( y & 0xFFFL ) << 26L | z & 0x3FFFFFFL );
    }

    public Vector3i clone() {
        return new Vector3i( x, y, z );
    }

    public Location toLocation() {
        return this.toLocation( null );
    }

    public Location toLocation( World world ) {
        return new Location( world, x, y, z );
    }

    public Location toMidLocation( World world ) {
        return new Location( world, x + .5, y, z + .5 );
    }

    public Location toMidLocation() {
        return this.toMidLocation( null );
    }


}
