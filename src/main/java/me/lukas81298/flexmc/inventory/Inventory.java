package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import me.lukas81298.flexmc.entity.Player;
import me.lukas81298.flexmc.io.message.play.server.MessageS16SetSlot;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class Inventory {

    private ItemStack[] items;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final byte windowId;
    @Getter private final String title;
    protected final Set<Player> viewers = ConcurrentHashMap.newKeySet();

    public Inventory( int size, byte windowId, String title ) {
        this.items = new ItemStack[ size ];
        this.windowId = windowId;
        this.title = title;
    }

    public void addItem( ItemStack itemStack ) {
        if( itemStack == null ) {
            return;
        }
        this.lock.writeLock().lock();
        try {
            for( int i = 0; i < items.length; i++ ) {
                ItemStack t = items[ i ];
                if( t == null ) {
                    setItem0( i, itemStack );
                    return;
                } else if( t.isSimilar( itemStack ) && t.getAmount() + itemStack.getAmount() <= 64 ) {
                    t.setAmount( t.getAmount() + itemStack.getAmount() );
                    setItem0( i, t );
                    return;
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void setItem( int slot, ItemStack itemStack ) {
        this.lock.writeLock().lock();
        try {
            setItem0( slot, itemStack );
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private void setItem0( int slot, ItemStack itemStack ) {
        if( slot < 0 || slot >= items.length ) {
            throw new IllegalArgumentException( "Invalid slot " + slot + ", expected [0;" + ( slot - 1 ) + "]" );
        }
        this.items[ slot ] = itemStack;
        for( Player player : this.viewers ) {
            player.getConnectionHandler().sendMessage( new MessageS16SetSlot( windowId, (short) getRawSlow( slot ), itemStack == null ? ItemStack.AIR : itemStack ) );
        }
    }

    public void setContents( ItemStack[] items ) {
        this.lock.writeLock().lock();
        try {
            if( this.items.length != items.length ) {
                throw new IllegalArgumentException( "Invalid contents, array length must be " + items.length );
            }
            int i = 0;
            for ( ItemStack item : items ) {
                setItem0( i, item );
                i++;
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public ItemStack getItem( int slot ) {
        if( slot < 0 || slot >= items.length ) {
            throw new IllegalArgumentException( "Invalid slot " + slot + ", expected [0;" + ( slot - 1 ) + "]" );
        }
        this.lock.readLock().lock();
        try {
            return items[ slot ];
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public ItemStack[] getContents() {
        this.lock.readLock().lock();
        try {
            ItemStack[] s = new ItemStack[ items.length ];
            System.arraycopy( items, 0, s, 0, items.length );
            return s;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public int getSize() {
        this.lock.readLock().lock();
        try {
            return this.items.length;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    protected int getRawSlow( int virtualSlot ) {
        return virtualSlot;
    }
}
