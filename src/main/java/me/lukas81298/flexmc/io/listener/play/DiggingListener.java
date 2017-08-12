package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC14PlayerDigging;
import me.lukas81298.flexmc.io.message.play.server.MessageS08BlockBreakAnimation;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.Location;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.World;
import me.lukas81298.flexmc.world.block.BlockSpec;
import me.lukas81298.flexmc.world.block.Blocks;

import java.util.Collections;

/**
 * @author lukas
 * @since 06.08.2017
 */
public class DiggingListener implements MessageInboundListener<MessageC14PlayerDigging> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC14PlayerDigging message ) {
        Player player = connectionHandler.getPlayer();
        if( player != null ) {
            World world = player.getWorld();
            if( message.getStatus() == 2 ) {
                BlockState previous = world.getBlockAt( message.getPosition() );
                world.setBlock( message.getPosition(), new BlockState( 0, 0 ) );
                spawnItems( player, message.getPosition().toMidLocation(), previous );
            } else if( message.getStatus() ==  0 ) {
                for( Player t : world.getPlayers() ) {
                    t.getConnectionHandler().sendMessage( new MessageS08BlockBreakAnimation( player.getEntityId(), message.getPosition(), (byte) 1 ) );
                }
            } else if ( message.getStatus() == 0 ) {
                for( Player t : world.getPlayers() ) {
                    t.getConnectionHandler().sendMessage( new MessageS08BlockBreakAnimation( player.getEntityId(), message.getPosition(), (byte) -1 ) );
                }
            } else if( message.getStatus() == 4 ) {
                ItemStack itemStack = player.getItemInHand();
                if ( itemStack != null && !itemStack.isEmpty() ) {
                    synchronized ( connectionHandler.getPlayer() ) {
                        itemStack.setAmount( itemStack.getAmount() - 1 );
                        int type = itemStack.getType();
                        int data = itemStack.getDamage();
                        player.dropItem( new ItemStack( type, 1, (short) data ) );
                        if( itemStack.getAmount() <= 1 ) {
                            itemStack = null;
                        }
                        player.getInventory().setItem( player.getHeldItemSlot(), itemStack );
                    }
                }
            } else if( message.getStatus() == 4 ) {
                ItemStack itemStack = player.getItemInHand();
                if ( itemStack != null && !itemStack.isEmpty() ) {
                    synchronized ( connectionHandler.getPlayer() ) {
                        int type = itemStack.getType();
                        int data = itemStack.getDamage();
                        player.dropItem( new ItemStack( type, itemStack.getAmount(), (short) data ) );
                        player.getInventory().setItem( player.getHeldItemSlot(), null );
                    }
                }
            }
        }
    }

    private void spawnItems( Player player, Location location, BlockState state ) {
        Iterable<ItemStack> s;
        BlockSpec spec = Blocks.getBlockSpec( state.getTypeId() );
        if( spec == null ) { // fallback normal behavior
            s = Collections.singletonList( new ItemStack( state.getTypeId(), 1, (short) state.getData() ) );
        } else {
            s = spec.getDrops( player, state.getData() );
        }
        if( s != null ) {
            for ( ItemStack itemStack : s ) {
                player.getWorld().spawnItem( location, itemStack );
            }
        }

    }

}
