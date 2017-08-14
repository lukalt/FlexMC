package me.lukas81298.flexmc.world;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

/**
 * @author lukas
 * @since 14.08.2017
 */
public class FlexWorldBorder implements WorldBorder {

    private final FlexWorld world;

    private double x, z;
    @Getter
    private double size;
    @Getter
    private double damageAmount, damageBuffer;
    @Getter
    private int warningTime, warningDistance;

    public FlexWorldBorder( FlexWorld world ) {
        this.world = world;
        this.reset();
    }

    @Override
    public synchronized void reset() {
        x = 0D;
        z = 0D;
        size = 30000 * 2;
    }

    @Override
    public synchronized void setSize( double v ) {
        setSize( v, 0L );
    }

    @Override
    public synchronized void setSize( double v, long l ) {
        size = v;
    }

    @Override
    public synchronized Location getCenter() {
        return new Location( world, x, 0, z );
    }

    @Override
    public synchronized void setCenter( double v, double v1 ) {
        this.x = v;
        this.z = v1;
    }

    @Override
    public synchronized void setCenter( Location location ) {
        setCenter( location.getX(), location.getZ() );
    }

    @Override
    public synchronized void setDamageBuffer( double v ) {
        this.damageBuffer = v;
    }

    @Override
    public void setDamageAmount( double v ) {
        damageAmount = v;
    }

    @Override
    public void setWarningTime( int i ) {
        warningTime = i;
    }


    @Override
    public synchronized void setWarningDistance( int i ) {
        warningDistance = i;
    }

    @Override
    public synchronized boolean isInside( Location location ) {
        return Math.abs( location.getX() ) < size / 2 && Math.abs( location.getZ() ) < size / 2;
    }
}
