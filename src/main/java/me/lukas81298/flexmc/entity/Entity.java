package me.lukas81298.flexmc.entity;

import lombok.Getter;
import me.lukas81298.flexmc.entity.metadata.EntityFlag;
import me.lukas81298.flexmc.entity.metadata.EntityMetaData;
import me.lukas81298.flexmc.entity.metadata.MetaDataType;
import me.lukas81298.flexmc.io.message.play.server.*;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.world.World;

/**
 * @author lukas
 * @since 04.08.2017
 */
public abstract class Entity {

    @Getter private int entityId;
    @Getter protected volatile Location location;
    @Getter private World world;
    @Getter protected EntityMetaData metaData = new EntityMetaData();

    protected int ticksAlive = 0;

    @Getter private volatile boolean alive = true;

    public Entity( int entityId, Location location, World world ) {
        this.entityId = entityId;
        this.location = location;
        this.world = world;
        this.metaData.set( (byte) 0, MetaDataType.BYTE, (byte) 0 );
    }

    public synchronized void changeWorld( World world, int i ) {
        this.world = world;
        this.entityId = i;
    }

    public void teleport( Location l, boolean onGround ) {
        boolean lookChanged = l.yaw() != location.yaw() || l.pitch() != location.pitch();
        Location position = this.location; // prevent rc
        boolean positionChanged = l.x() != this.location.x() || l.y() != position.y() || l.z() != this.location.z();
        if( this.location.distanceSquared( l ) < 64 ) {
            double dx = ( l.x() * 32 - getLocation().x() * 32 ) * 128;
            double dy = ( l.y() * 32 - getLocation().y() * 32 ) * 128;
            double dz = ( l.z() * 32 - getLocation().z() * 32 ) * 128;
            for ( Player player : getWorld().getPlayers() ) {
                if( !player.equals( this ) ) {
                    if( positionChanged && lookChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS27EntityRelMoveLook( getEntityId(), (short) dx, (short) dy, (short) dz, l.yaw(), l.pitch(), onGround ) );
                    } else if( positionChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS26EntityRelMove( getEntityId(), (short) dx, (short) dy, (short) dz, onGround ) );
                    } else if( lookChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS28EntityLook( getEntityId(), l.yaw(), l.pitch(), onGround ) );
                    }
                    if( lookChanged ) {
                        player.getConnectionHandler().sendMessage( new MessageS36EntityHeadLook( getEntityId(), l.yaw() ) );
                    }
                }
            }
        } else {
            for ( Player player : getWorld().getPlayers() ) {
                if( !player.equals( this ) ) {
                    player.getConnectionHandler().sendMessage( new MessageS4CEntityTeleport( getEntityId(), l.x(), l.y(), l.z(), l.yaw(), l.pitch(), onGround ) );
                    player.getConnectionHandler().sendMessage( new MessageS36EntityHeadLook( getEntityId(), l.yaw() ) );
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

    public void setCustomName( String name ) {
        this.metaData.set( (byte) 2, MetaDataType.STRING, name );
        this.updateMetaData();
    }

    public void setCustomNameVisible( boolean b ) {
        this.metaData.set( (byte) 3, MetaDataType.BOOLEAN, b );
        this.updateMetaData();
    }

    private void updateMetaData() {
        for ( Player player : this.world.getPlayers() ) {
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

    public void remove() {
        // todo remove
    }
}
