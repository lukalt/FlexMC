package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC1FBlockPlacement;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import me.lukas81298.flexmc.world.BlockState;
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
                    player.getWorld().setBlock( message.getPosition(), new BlockState( stack.getType(), stack.getDurability() ) );
                }
            }
            player.getInventory().setItem( heldSlot, stack );
        }
    }

}
