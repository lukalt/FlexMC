package me.lukas81298.flexmc.entity;

import lombok.Getter;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.world.World;

/**
 * @author lukas
 * @since 05.08.2017
 */
public abstract class LivingEntity extends Entity {

    @Getter private volatile float health = 20F;
    @Getter private volatile float maxHealth = 20F;

    public LivingEntity( int entityId, Location position, World world ) {
        super( entityId, position, world );
    }

}
