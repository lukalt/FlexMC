package me.lukas81298.flexmc.util;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author lukas
 * @since 10.08.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors( fluent = true )
public class Vector3d {

    private double vx,vy,vz;

    public synchronized Vector3d multiply( double factor ) {
        vx *= factor;
        vy *= factor;
        vz *= factor;
        return this;
    }
}
