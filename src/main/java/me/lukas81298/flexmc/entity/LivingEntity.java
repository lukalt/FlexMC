package me.lukas81298.flexmc.entity;

import me.lukas81298.flexmc.util.BetterAtomicDouble;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.World;
import org.bukkit.Location;

import java.util.function.DoubleUnaryOperator;

/**
 * @author lukas
 * @since 05.08.2017
 */
public abstract class LivingEntity extends FlexEntity {

    private final BetterAtomicDouble health = new BetterAtomicDouble( 20D );
    private final BetterAtomicDouble maxHealth = new BetterAtomicDouble( 20D );
    protected final BetterAtomicDouble fallDistance = new BetterAtomicDouble( 0D );

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
        this.updateHealth( health );
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
        Location previous = this.location;
        if( l.getY() < 0 ) {
            damage( 2D );

            return;
        }
        if( previous.getY() > l.getY() ) {
            double tempFallDistance = Math.abs( l.getY() - previous.getY() );
            BlockState state = getWorld().getBlockAt( new Vector3i( l.getBlockX(), ((int) l.getY()) - 1, l.getBlockZ() ) );
            if( state.getTypeId() != 0 || getWorld().getBlockAt( new Vector3i( l.getBlockX(), l.getBlockY(), l.getBlockZ() ) ).getTypeId() != 0 ) {
                fallDistance.updateAndGet( new DoubleUnaryOperator() {
                    @Override
                    public double applyAsDouble( double operand ) {
                        double finalFallDistance = operand + tempFallDistance;
                        if( finalFallDistance > 2.5 ) {
                            damage( finalFallDistance );
                        }
                        return 0;
                    }
                } );
            } else {
                fallDistance.addAndGet( tempFallDistance );
            }
        } else {
            fallDistance.set( 0D );
        }
        super.teleport( l, onGround );
    }

}
