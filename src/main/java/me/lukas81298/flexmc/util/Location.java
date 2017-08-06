package me.lukas81298.flexmc.util;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author lukas
 * @since 05.08.2017
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Accessors( fluent = true )
@ToString
public class Location {

    private double x,y,z;
    private float yaw,pitch;

    public Location( double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceSquared( Location to ) {
        double dx = to.x - x;
        double dy = to.y - y;
        double dz = to.z - z;
        return dx * dx + dy * dy + dz * dz;
    }
}
