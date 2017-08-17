package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.entity.FlexPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lukas
 * @since 17.08.2017
 */
@RequiredArgsConstructor
public class FlexInventoryView extends InventoryView {

    private final FlexPlayer player;
    private final Lock lock = new ReentrantLock();

    @Getter
    private volatile FlexInventory topInventory;

    public void setTopInventory( FlexInventory topInventory ) {
        this.lock.lock();
        try {
            this.topInventory = topInventory;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public Inventory getBottomInventory() {
        return this.player.getInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return this.player;
    }

    @Override
    public InventoryType getType() {
        this.lock.lock();
        try {
            return this.topInventory == null ? InventoryType.PLAYER : this.topInventory.getType();
        } finally {
            this.lock.unlock();
        }
    }
}
