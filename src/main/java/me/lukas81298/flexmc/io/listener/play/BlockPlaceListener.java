package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC1FBlockPlacement;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.util.EventFactory;
import me.lukas81298.flexmc.world.BlockState;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class BlockPlaceListener implements MessageInboundListener<MessageC1FBlockPlacement> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC1FBlockPlacement message ) {
        FlexPlayer player = connectionHandler.getPlayer();
        synchronized ( player.getInventory() ) {
            int heldSlot = player.getHeldItemSlot();
            ItemStack stack = player.getInventory().getItem( heldSlot );
            if( stack != null ) {
                stack.setAmount( stack.getAmount() - 1 );
                if( stack.getAmount() <= 0 ) {
                    stack = null;
                } else {
                    ItemStack itemInHand = player.getItemInHand();
                    BlockPlaceEvent event = new BlockPlaceEvent( player.getWorld().getBlock0( message.getPosition().getX(), message.getPosition().getY(), message.getPosition().getZ(), true ), null, null, itemInHand, player, true );
                    BlockState pr = new BlockState( event.getBlock().getType(), event.getBlock().getData() );
                    EventFactory.call( event );
                    if( !event.isCancelled() && event.canBuild() ) {
                        player.getWorld().setBlock( message.getPosition(), new BlockState( stack.getType(), stack.getDurability() ) );
                    } else {
                        player.getWorld().setBlock( message.getPosition(), pr );
                    }
                }
            }
            player.getInventory().setItem( heldSlot, stack );
        }
    }

}
