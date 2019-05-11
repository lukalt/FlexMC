package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC1FBlockPlacement;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.BlockState;
import me.lukas81298.flexmc.world.block.BlockSpec;
import me.lukas81298.flexmc.world.block.Blocks;
import me.lukas81298.flexmc.world.block.FlexBlock;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class BlockPlaceListener implements MessageInboundListener<MessageC1FBlockPlacement> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC1FBlockPlacement message ) {
        Bukkit.broadcastMessage( "block place " + message );
        FlexPlayer player = connectionHandler.getPlayer();
        synchronized ( player.getInventory() ) {
            int heldSlot = player.getHeldItemSlot();
            ItemStack stack = player.getInventory().getItem( heldSlot );
            Bukkit.broadcastMessage( Objects.toString( stack ) );
            if ( stack != null && stack.getAmount() > 0 ) {
                Vector3i position = message.getPosition();
                switch ( message.getFace() ) {
                    case 0:
                        position.modify( 0, -1, 0 );
                        break;
                    case 1:
                        position.modify( 0, 1, 0 );
                        break;
                    case 2:
                        position.modify( 0, 0, -1 );
                        break;
                    case 3:
                        position.modify( 0, 0, 1 );
                        break;
                    case 4:
                        position.modify( -1, 0, 0 );
                        break;
                    case 5:
                        position.modify( 1, 0, 0 );
                        break;
                    default:
                        return;
                }
                stack.setAmount( stack.getAmount() - 1 );
                ItemStack itemInHand = player.getItemInHand();
                BlockPlaceEvent event = new BlockPlaceEvent( player.getWorld().getBlock0( position.getX(), position.getY(), position.getZ(), true ), null, null, itemInHand, player, true, EquipmentSlot.HAND );
                BlockState pr = new BlockState( event.getBlock().getType(), event.getBlock().getData() );
                EventFactory.call( event );
                if ( !event.isCancelled() && event.canBuild() ) {
                    player.getWorld().setBlock( position, new BlockState( stack.getType(), stack.getDurability() ) );
                } else {
                    player.getWorld().setBlock( position, pr );
                }
                if ( stack.getAmount() <= 0 ) {
                    stack = null;
                }
            } else {
                FlexBlock block = player.getWorld().getBlock0( message.getPosition().getX(), message.getPosition().getY(), message.getPosition().getZ(), false );
                BlockSpec blockSpec = Blocks.getBlockSpec( block.getTypeId() );
                if( blockSpec != null ) {
                    blockSpec.click( player, block );
                }
            }
            player.getInventory().setItem( heldSlot, stack );
        }
    }

}
