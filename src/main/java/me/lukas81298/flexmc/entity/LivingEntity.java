package me.lukas81298.flexmc.entity;

import me.lukas81298.flexmc.util.BetterAtomicDouble;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.world.World;

import java.util.function.DoubleUnaryOperator;

/**
 * @author lukas
 * @since 05.08.2017
 */
public abstract class LivingEntity extends Entity {

    private final BetterAtomicDouble health = new BetterAtomicDouble( 20D );
    private final BetterAtomicDouble maxHealth = new BetterAtomicDouble( 20D );

    public LivingEntity( int entityId, Location position, World world ) {
        super( entityId, position, world );
    }

    public void damage( double damage ) {
        if( damage < 0 ) {
            return;
        }
        double health = this.health.updateAndGet( new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble( double operand ) {
                operand -= damage;
                return operand < 0 ? 0 : operand;
            }
        } );
        updateHealth( health );
    }

    public double getHealth() {
        return this.health.get();
    }

    public double getMaxHealth() {
        return this.maxHealth.get();
    }

    public void setMaxHealth( double health ) {
        this.maxHealth.set( health );
        updateHealth( health );
    }

    public void setHealth( double health ) {
        double min = Math.min( health, maxHealth.get() );
        this.health.set( min );
        updateHealth( min );
    }

    protected void updateHealth( double health ) {
        if( health <= 0 ) {
            remove();
        }
    }

    @Override
    public void teleport( Location l, boolean onGround ) {
        super.teleport( l, onGround );
        if( l.x() < 0 ) {
            damage( 4D );
        }
    }

}
