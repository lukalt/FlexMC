package me.lukas81298.flexmc.entity;

import me.lukas81298.flexmc.util.BetterAtomicDouble;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.DoubleUnaryOperator;

/**
 * @author lukas
 * @since 05.08.2017
 */
public abstract class FlexLivingEntity extends FlexEntity implements LivingEntity {

    private final BetterAtomicDouble health = new BetterAtomicDouble( 20D );
    private final BetterAtomicDouble maxHealth = new BetterAtomicDouble( 20D );
    protected final BetterAtomicDouble fallDistance = new BetterAtomicDouble( 0D );

    public FlexLivingEntity( int entityId, Location position, FlexWorld world ) {
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

    @Override
    public void damage( double v, Entity entity ) {

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

    @Override
    public void resetMaxHealth() {

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

    @Override
    public double getEyeHeight() {
        return 0;
    }

    @Override
    public double getEyeHeight( boolean b ) {
        return 0;
    }

    @Override
    public Location getEyeLocation() {
        return null;
    }

    @Override
    public List<Block> getLineOfSight( Set<Material> set, int i ) {
        return null;
    }

    @Override
    public Block getTargetBlock( Set<Material> set, int i ) {
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks( Set<Material> set, int i ) {
        return null;
    }

    @Override
    public int getRemainingAir() {
        return 0;
    }

    @Override
    public void setRemainingAir( int i ) {

    }

    @Override
    public int getMaximumAir() {
        return 0;
    }

    @Override
    public void setMaximumAir( int i ) {

    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks( int i ) {

    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage( double v ) {

    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks( int i ) {

    }

    @Override
    public Player getKiller() {
        return null;
    }

    @Override
    public boolean addPotionEffect( PotionEffect potionEffect ) {
        return false;
    }

    @Override
    public boolean addPotionEffect( PotionEffect potionEffect, boolean b ) {
        return false;
    }

    @Override
    public boolean addPotionEffects( Collection<PotionEffect> collection ) {
        return false;
    }

    @Override
    public boolean hasPotionEffect( PotionEffectType potionEffectType ) {
        return false;
    }

    @Override
    public PotionEffect getPotionEffect( PotionEffectType potionEffectType ) {
        return null;
    }

    @Override
    public void removePotionEffect( PotionEffectType potionEffectType ) {

    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return null;
    }

    @Override
    public boolean hasLineOfSight( Entity entity ) {
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway( boolean b ) {

    }

    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    @Override
    public void setCanPickupItems( boolean b ) {

    }

    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    @Override
    public boolean setLeashHolder( Entity entity ) {
        return false;
    }

    @Override
    public boolean isGliding() {
        return false;
    }

    @Override
    public void setGliding( boolean b ) {

    }

    @Override
    public void setAI( boolean b ) {

    }

    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public void setCollidable( boolean b ) {

    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public AttributeInstance getAttribute( Attribute attribute ) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile( Class<? extends T> aClass ) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile( Class<? extends T> aClass, Vector vector ) {
        return null;
    }
}
