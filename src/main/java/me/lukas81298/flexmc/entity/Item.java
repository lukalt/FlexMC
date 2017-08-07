package me.lukas81298.flexmc.entity;

import me.lukas81298.flexmc.entity.metadata.MetaDataType;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.io.message.play.server.MessageS4BCollectItem;
import me.lukas81298.flexmc.util.Location;
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
                if( l.distanceSquared( player.getLocation() ) <= .1 ) {
                    ItemStack itemStack = getItemStack();
                    if( itemStack != null ) {
                        for( Player t : this.getWorld().getPlayers() ) {
                            t.getConnectionHandler().sendMessage( new MessageS4BCollectItem( getEntityId(), player.getEntityId(), itemStack.getAmount() ) );
                        }
                        player.getInventory().addItem( itemStack );
                    }
                    remove();
                    break;
                }
            }
        }
    }

    @Override
    public byte getObjectType() {
        return (byte) 2;
    }
}
