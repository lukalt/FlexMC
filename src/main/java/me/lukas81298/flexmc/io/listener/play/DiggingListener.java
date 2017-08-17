package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.inventory.item.ItemSpec;
import me.lukas81298.flexmc.inventory.item.Items;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC14PlayerDigging;
import me.lukas81298.flexmc.io.message.play.server.MessageS08BlockBreakAnimation;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.FlexWorld;
import me.lukas81298.flexmc.world.block.BlockSpec;
import me.lukas81298.flexmc.world.block.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class DiggingListener implements MessageInboundListener<MessageC14PlayerDigging> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC14PlayerDigging message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        if( player != null ) {
            FlexWorld world = player.getWorld();
            if( message.getStatus() == 2 ) {
                BlockState previous = world.getBlockAt( message.getPosition() );
                BlockSpec blockSpec = Blocks.getBlockSpec( previous.getTypeId() );
                if( blockSpec != null ) {
                    blockSpec.breakBlock( player, player.getWorld(), message.getPosition() );
                }
                world.setBlock( message.getPosition(), new BlockState( 0, 0 ) );
                spawnItems( player, message.getPosition().toMidLocation( player.getWorld() ), previous );
                ItemStack itemStack = player.getItemInHand();
                if( itemStack != null && itemStack.getType() != Material.AIR ) {
                    ItemSpec spec = Items.getItemSpec( itemStack.getTypeId() );
                    if( spec != null ) {
                        short d = itemStack.getDurability();
                        ItemStack changed = spec.breakBlock( player, itemStack );
                        if( changed != itemStack || changed.getDurability() != d ) {
                            player.getInventory().setItem( player.getHeldItemSlot(), changed );
                        }
                    }
                }

            } else if( message.getStatus() ==  0 ) {
                for( FlexPlayer t : world.getPlayerSet() ) {
                    t.getConnectionHandler().sendMessage( new MessageS08BlockBreakAnimation( player.getEntityId(), message.getPosition(), (byte) 1 ) );
                }
            } else if ( message.getStatus() == 0 ) {
                for( FlexPlayer t : world.getPlayerSet() ) {
                    t.getConnectionHandler().sendMessage( new MessageS08BlockBreakAnimation( player.getEntityId(), message.getPosition(), (byte) -1 ) );
                }
            } else if( message.getStatus() == 4 ) {
                ItemStack itemStack = player.getItemInHand();
                if ( itemStack != null && itemStack.getType() != Material.AIR ) {
                    synchronized ( connectionHandler.getPlayer() ) {
                        itemStack.setAmount( itemStack.getAmount() - 1 );
                        player.dropItem( itemStack );
                        if( itemStack.getAmount() <= 1 ) {
                            itemStack = null;
                        }
                        player.getInventory().setItem( player.getHeldItemSlot(), itemStack );
                    }
                }
            } else if( message.getStatus() == 4 ) {
                ItemStack itemStack = player.getItemInHand();
                if ( itemStack != null && itemStack.getType() != Material.AIR ) {
                    synchronized ( connectionHandler.getPlayer() ) {
                        player.dropItem( itemStack );
                        player.getInventory().setItem( player.getHeldItemSlot(), null );
                    }
                }
            }
        }
    }

    private void spawnItems( FlexPlayer player, Location location, BlockState state ) {
        Iterable<ItemStack> s;
        BlockSpec spec = Blocks.getBlockSpec( state.getTypeId() );
        if( spec == null ) { // fallback normal behavior
            s = Collections.singletonList( new ItemStack( state.getType(), 1, (short) state.getData() ) );
        } else {
            s = spec.getDrops( player, state.getData() );
        }
        if( s != null ) {
            for ( ItemStack itemStack : s ) {
                if( itemStack != null && itemStack.getType() != Material.AIR ) {
                    player.getWorld().spawnItem( location, itemStack );
                }
            }
        }

    }

}
