package me.lukas81298.flexmc.entity;

import lombok.Getter;
import me.lukas81298.flexmc.entity.metadata.EntityFlag;
import me.lukas81298.flexmc.entity.metadata.EntityMetaData;
import me.lukas81298.flexmc.entity.metadata.MetaDataType;
import me.lukas81298.flexmc.io.message.play.server.*;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author lukas
 * @since 04.08.2017
 */
public abstract class FlexEntity implements Entity {

    @Getter private int entityId;
    @Getter protected volatile Location location;
    @Getter private FlexWorld world;
    @Getter protected EntityMetaData metaData = new EntityMetaData();
    protected final UUID uuid = UUID.randomUUID();
    protected int ticksAlive = 0;

    @Getter protected volatile boolean alive = true;

    private boolean onGround = false;

    public FlexEntity( int entityId, Location location, FlexWorld world ) {
        this.entityId = entityId;
        this.location = location;
        this.world = world;
        this.metaData.set( (byte) 0, MetaDataType.BYTE, (byte) 0 );
    }

    public synchronized void changeWorld( FlexWorld world, int i ) {
        this.world = world;
        this.entityId = i;
    }

    public void teleport( Location l, boolean onGround ) {

        this.onGround = onGround;

        boolean lookChanged = l.getYaw() != location.getYaw() || l.getPitch() != location.getPitch();
        Location position = this.location; // prevent rc
        boolean positionChanged = l.getX() != this.location.getX() || l.getY() != position.getY() || l.getZ() != this.location.getZ();
        if( this.location.distanceSquared( l ) < 64 ) {
            double dx = ( l.getX() * 32 - getLocation().getX() * 32 ) * 128;
            double dy = ( l.getY() * 32 - getLocation().getY() * 32 ) * 128;
            double dz = ( l.getZ() * 32 - getLocation().getZ() * 32 ) * 128;
            for ( FlexPlayer player : getWorld().getPlayerSet() ) {
                if( !player.equals( this ) ) {
                    if( positionChanged && lookChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS27EntityRelMoveLook( getEntityId(), (short) dx, (short) dy, (short) dz, l.getYaw(), l.getPitch(), onGround ) );
                    } else if( positionChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS26EntityRelMove( getEntityId(), (short) dx, (short) dy, (short) dz, onGround ) );
                    } else if( lookChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS28EntityLook( getEntityId(), l.getYaw(), l.getPitch(), onGround ) );
                    }
                    if( lookChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS36EntityHeadLook( getEntityId(), l.getYaw() ) );
                    }
                }
            }
        } else {
            for ( FlexPlayer player : getWorld().getPlayerSet() ) {
                if( !player.equals( this ) ) {
                    player.getConnectionHandler().sendMessage( new MessageS4CEntityTeleport( getEntityId(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), onGround ) );
                    player.getConnectionHandler().sendMessage( new MessageS36EntityHeadLook( getEntityId(), l.getYaw() ) );
                }
            }
        }

        this.location = l;
    }

    public String getCustomName() {
        return this.metaData.get( (byte) 2 );
    }

    public boolean isCustomNameVisible() {
        return this.metaData.get( (byte) 3) ;
    }

    @Override
    public void setGlowing( boolean b ) {

    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public void setInvulnerable( boolean b ) {

    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void setSilent( boolean b ) {

    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public void setGravity( boolean b ) {

    }

    @Override
    public int getPortalCooldown() {
        return 0;
    }

    @Override
    public void setPortalCooldown( int i ) {

    }

    @Override
    public Set<String> getScoreboardTags() {
        return Collections.emptySet();
    }

    @Override
    public boolean addScoreboardTag( String s ) {
        return false;
    }

    @Override
    public boolean removeScoreboardTag( String s ) {
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.IGNORE;
    }

    public void setCustomName( String name ) {
        this.metaData.set( (byte) 2, MetaDataType.STRING, name );
        this.updateMetaData();
    }

    public void setCustomNameVisible( boolean b ) {
        this.metaData.set( (byte) 3, MetaDataType.BOOLEAN, b );
        this.updateMetaData();
    }

    private void updateMetaData() {
        for ( FlexPlayer player : this.world.getPlayerSet() ) {
            if( !player.equals( this ) ) {
                player.getConnectionHandler().sendMessage( new MessageS3CEntityMetaData( entityId, metaData ) );
            }
        }
    }

    protected boolean getFlag( EntityFlag flag ) {
        Byte b = this.metaData.get( (byte) 0 );
        return b != null && ( b & 1 << flag.ordinal() ) != 0;
    }

    protected void setFlag( EntityFlag flag, boolean value ) {
        Byte b = this.metaData.get( (byte) 0 );
        byte k = b == null ? 0 : b;
        this.metaData.set( (byte) 0, MetaDataType.BYTE, (byte) ( value ? ( k | 1 << flag.ordinal() ) : ( k & ( ~1 << flag.ordinal() ) ) ) );
        this.updateMetaData();
    }

    public void tick() {
        ticksAlive++;
    }

    @Override
    public Location getLocation( Location location ) {
        location.setX( this.location.getX() );
        location.setY( this.location.getY() );
        location.setZ( this.location.getZ() );
        location.setYaw( this.location.getYaw() );
        location.setPitch( this.location.getPitch() );
        return location;
    }

    @Override
    public void setVelocity( Vector vector ) {

    }

    @Override
    public Vector getVelocity() {
        return new Vector( 0, 0, 0 ); // todo change
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public boolean teleport( Location location ) {
        this.location = location;
        return true;
    }

    @Override
    public boolean teleport( Location location, PlayerTeleportEvent.TeleportCause teleportCause ) {
        return this.teleport( location );
    }

    @Override
    public boolean teleport( Entity entity ) {
        return this.teleport( entity.getLocation() );
    }

    @Override
    public boolean teleport( Entity entity, PlayerTeleportEvent.TeleportCause teleportCause ) {
        return false;
    }

    @Override
    public List<Entity> getNearbyEntities( double v, double v1, double v2 ) {
        Location l = this.getLocation();
        List<Entity> entities = new ArrayList<>();
        for ( FlexEntity flexEntity : this.world.getEntitySet() ) {
            Location t = flexEntity.getLocation();
            if( Math.abs( t.getX() - l.getX() ) < v && Math.abs( t.getY() - l.getY() ) < v1 && Math.abs( t.getZ() - l.getZ() ) < v2 ) {
                entities.add( flexEntity );
            }
        }
        return entities;
    }

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks( int i ) {

    }

    public void remove() {
        alive = false;
    }

    @Override
    public boolean isDead() {
        return !this.alive;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void sendMessage( String s ) {
        throw new UnsupportedOperationException( "Cannot send message to entity" ); // wtf bukkit
    }

    @Override
    public void sendMessage( String[] strings ) {
        for ( String string : strings ) {
            this.sendMessage( string );
        }
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public String getName() {
        return this.getClass().getName().replace( "Flex", "" ); // todo change
    }

    @Override
    public Entity getPassenger() {
        return null;
    }

    @Override
    public boolean setPassenger( Entity entity ) {
        return false;
    }

    @Override
    public List<Entity> getPassengers() {
        return Collections.emptyList();
    }

    @Override
    public boolean addPassenger( Entity entity ) {
        return false;
    }

    @Override
    public boolean removePassenger( Entity entity ) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean eject() {
        return false;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance( float v ) {

    }

    @Override
    public void setLastDamageCause( EntityDamageEvent entityDamageEvent ) {

    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public int getTicksLived() {
        return 0;
    }

    @Override
    public void setTicksLived( int i ) {

    }

    @Override
    public void playEffect( EntityEffect entityEffect ) {

    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        return false;
    }

    @Override
    public Entity getVehicle() {
        return null;
    }


    @Override
    public void setMetadata( String s, MetadataValue metadataValue ) {

    }

    @Override
    public List<MetadataValue> getMetadata( String s ) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasMetadata( String s ) {
        return false;
    }

    @Override
    public void removeMetadata( String s, Plugin plugin ) {

    }

    @Override
    public boolean isPermissionSet( String s ) {
        return false;
    }

    @Override
    public boolean isPermissionSet( Permission permission ) {
        return false;
    }

    @Override
    public boolean hasPermission( String s ) {
        return false;
    }

    @Override
    public boolean hasPermission( Permission permission ) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment( Plugin plugin, String s, boolean b ) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment( Plugin plugin ) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment( Plugin plugin, String s, boolean b, int i ) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment( Plugin plugin, int i ) {
        return null;
    }

    @Override
    public void removeAttachment( PermissionAttachment permissionAttachment ) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp( boolean b ) {
    }
}
