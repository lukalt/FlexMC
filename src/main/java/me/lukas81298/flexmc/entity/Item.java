package me.lukas81298.flexmc.entity;

import me.lukas81298.flexmc.entity.metadata.MetaDataType;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.io.message.play.server.MessageS4BCollectItem;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.World;

import javax.annotation.Nullable;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class Item extends Entity implements EntityObject {

    public Item( int entityId, Location location, World world ) {
        super( entityId, location, world );
    }

    public void setItemStack( ItemStack itemStack ) {
        this.metaData.set( (byte) 6, MetaDataType.SLOT, itemStack );
    }

    @Nullable
    public ItemStack getItemStack() {
        return this.metaData.get( (byte) 6 );
    }

    @Override
    public void tick() {
        super.tick();
        if( ticksAlive > 20 * 60 ) {
            remove();
        } else if( isAlive() ) {
            Location l = this.getLocation();
            for( Player player : this.getWorld().getPlayers() ) {
                if( l.distanceSquared( player.getLocation() ) <= 2 ) {
                    ItemStack itemStack = getItemStack();
                    if( itemStack != null ) {
                        for( Player t : this.getWorld().getPlayers() ) {
                            t.getConnectionHandler().sendMessage( new MessageS4BCollectItem( getEntityId(), player.getEntityId(), itemStack.getAmount() ) );
                        }
                        player.getInventory().addItem( itemStack );
                    }
                    remove();
                    return;
                }
            }
            boolean k = Math.ceil( l.y() ) != l.y();
            boolean m = getWorld().getBlockAt( new Vector3i( (int) l.x(), ( (int) l.y() ) - 1, (int) l.z() ) ).getId() == 0;
            if( m || k ){
                if( m ) {
                    location = new Location( l.x(), l.y() - 0.03999999910593033D, l.z() );
                } else {
                    location = new Location( l.x(), Math.ceil( l.y() ), l.z() );
                }
            }
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.ITEM;
    }

    @Override
    public byte getObjectType() {
        return (byte) 2;
    }
}
