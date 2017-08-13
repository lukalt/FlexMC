package me.lukas81298.flexmc.entity;

import lombok.Getter;
import lombok.Setter;
import me.lukas81298.flexmc.entity.metadata.MetaDataType;
import me.lukas81298.flexmc.io.message.play.server.MessageS4BCollectItem;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;


/**
 * @author lukas
 * @since 07.08.2017
 */
public class FlexItem extends FlexEntity implements EntityObject, Item {

    private double fallSpeed = 0D; // no concurrency needed here, entity is ticked by the same thread every time
    @Getter @Setter private int pickupDelay = 10;
    public FlexItem( int entityId, Location location, FlexWorld world ) {
        super( entityId, location, world );
    }

    public void setItemStack( ItemStack itemStack ) {
        this.metaData.set( (byte) 6, MetaDataType.SLOT, itemStack );
    }

    public ItemStack getItemStack() {
        return this.metaData.get( (byte) 6 );
    }

    @Override
    public void tick() {
        super.tick();
        if( ticksAlive > 20 * 60 * 5 ) {
            remove();
        } else if( isAlive() ) {
            Location l = this.getLocation();
            if( ticksAlive > 10 ) {
                for( FlexPlayer player : this.getWorld().getPlayerSet() ) {
                    if( l.distanceSquared( player.getLocation() ) <= 2 ) {
                        ItemStack itemStack = getItemStack();
                        if( itemStack != null ) {
                            for( FlexPlayer t : this.getWorld().getPlayerSet() ) {
                                t.getConnectionHandler().sendMessage( new MessageS4BCollectItem( getEntityId(), player.getEntityId(), itemStack.getAmount() ) );
                            }
                            player.getInventory().addItem( itemStack );
                        }
                        remove();
                        return;
                    }
                }
            }
            boolean k = Math.ceil( l.getY() ) != l.getY();
            boolean m = getWorld().getBlockAt( new Vector3i( l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() ) ).getTypeId() == 0;
            if( m || k ){
                if( m ) {
                    fallSpeed += 0.03999999910593033D;
                    location = new Location( null, l.getBlockX(), l.getY() - fallSpeed, l.getZ() );
                } else {
                    location = new Location( null, l.getX(), Math.ceil( l.getY() ), l.getZ() );
                }
            } else {
                fallSpeed = 0D;
            }
        }
    }

    @Override
    public byte getObjectType() {
        return (byte) 2;
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
