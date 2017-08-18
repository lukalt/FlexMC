package me.lukas81298.flexmc.io.listener.play;

import me.lukas81298.flexmc.inventory.item.ItemSpec;
import me.lukas81298.flexmc.inventory.item.Items;
import me.lukas81298.flexmc.io.listener.MessageInboundListener;
import me.lukas81298.flexmc.io.message.play.client.MessageC20UseItem;
import me.lukas81298.flexmc.io.netty.ConnectionHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author lukas
 * @since 17.08.2017
 */
public class ItemUseListener implements MessageInboundListener<MessageC20UseItem> {

    @Override
    public void handle( ConnectionHandler connectionHandler, MessageC20UseItem message ) throws Exception {
        connectionHandler.getPlayer().getInventory().setItemInHand( connectionHandler.getPlayer().getInventory().getItemInHand() );
        ItemStack itemStack = connectionHandler.getPlayer().getItemInHand();
        if( itemStack != null && itemStack.getType() != Material.AIR ) {
            ItemSpec spec = Items.getItemSpec( itemStack.getTypeId() );
            if( spec != null ) {
                spec.click( connectionHandler.getPlayer() );
            }
        }
    }

}
