package me.lukas81298.flexmc.inventory;

import lombok.Getter;
import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.io.message.play.server.MessageS16SetSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author lukas
 * @since 07.08.2017
 */
public abstract class FlexInventory implements Inventory {

    private ItemStack[] items;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final byte windowId;
    @Getter private final String title;
    protected final Set<FlexPlayer> viewers = ConcurrentHashMap.newKeySet();

    public FlexInventory( int size, byte windowId, String title ) {
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

    @Override
    public HashMap<Integer, ItemStack> addItem( ItemStack... itemStacks ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem( ItemStack... itemStacks ) throws IllegalArgumentException {
        return null;
    }

    private void setItem0( int slot, ItemStack itemStack ) {
        if( slot < 0 || slot >= items.length ) {
            throw new IllegalArgumentException( "Invalid slot " + slot + ", expected [0;" + ( slot - 1 ) + "]" );
        }
        this.items[ slot ] = itemStack;
        for( FlexPlayer player : this.viewers ) {
            player.getConnectionHandler().sendMessage( new MessageS16SetSlot( windowId, (short) getRawSlow( slot ), itemStack == null ? ItemStackConstants.AIR : itemStack ) );
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

    @Override
    public ItemStack[] getStorageContents() {
        return new ItemStack[0];
    }

    @Override
    public void setStorageContents( ItemStack[] itemStacks ) throws IllegalArgumentException {

    }

    @Override
    public boolean contains( int i ) {
        for ( ItemStack item : this.items ) {
            if( item != null && item.getTypeId() == i ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains( Material material ) throws IllegalArgumentException {
        for ( ItemStack item : items ) {
            if( item != null && material == item.getType() ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains( ItemStack itemStack ) {
        for ( ItemStack item : items ) {
            if( itemStack.equals( item ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains( int i, int i1 ) {
        int j = 0;
        for ( ItemStack item : items ) {
            if( item != null && item.getTypeId() == i ) {
                j += item.getAmount();
            }
        }
        return j >= i1;
    }

    @Override
    public boolean contains( Material material, int i ) throws IllegalArgumentException {
        int j = 0;
        for ( ItemStack item : items ) {
            if( item != null && item.getType() == material ) {
                j += item.getAmount();
            }
        }
        return j >= i;
    }

    @Override
    public boolean contains( ItemStack itemStack, int i ) {
        int j = 0;
        for ( ItemStack item : items ) {
            if( item != null && itemStack.equals( item ) ) {
                j += item.getAmount();
            }
        }
        return j >= i;
    }

    @Override
    public boolean containsAtLeast( ItemStack itemStack, int i ) {
        return contains( itemStack, i );
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all( int i ) {
        HashMap<Integer, ItemStack> results = new HashMap<>();
        for ( int index = 0; index < items.length; index++ ) {
            ItemStack itemStack = items[ index ];
            if( itemStack != null && itemStack.getTypeId() == i ) {
                results.put( index, itemStack );
            }
        }
        return results;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all( Material material ) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> results = new HashMap<>();
        for ( int index = 0; index < items.length; index++ ) {
            ItemStack itemStack = items[ index ];
            if( itemStack != null && itemStack.getType() == material) {
                results.put( index, itemStack );
            }
        }
        return results;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all( ItemStack itemStack ) {
        HashMap<Integer, ItemStack> results = new HashMap<>();
        for ( int index = 0; index < items.length; index++ ) {
            ItemStack item = items[ index ];
            if( itemStack != null && itemStack.equals( item ) ) {
                results.put( index, itemStack );
            }
        }
        return results;
    }

    @Override
    public int first( int i ) {
        int j = 0;
        for ( ItemStack item : items ) {
            if( item != null && item.getTypeId() == i ) {
                return j;
            }
            j++;
        }
        return -1;
    }

    @Override
    public int first( Material material ) throws IllegalArgumentException {
        int j = 0;
        for ( ItemStack item : items ) {
            if( item != null && item.getType() == material ) {
                return j;
            }
            j++;
        }
        return -1;
    }

    @Override
    public int first( ItemStack itemStack ) {
        int j = 0;
        for ( ItemStack item : items ) {
            if( itemStack.equals( item ) ) {
                return j;
            }
            j++;
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        int i = 0;
        for ( ItemStack item : this.items ) {
            if( item == null || item.getType() == Material.AIR ) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public void remove( int i ) {

    }

    @Override
    public void remove( Material material ) throws IllegalArgumentException {

    }

    @Override
    public void remove( ItemStack itemStack ) {

    }

    @Override
    public void clear( int i ) {

    }

    @Override
    public void clear() {
        setContents( new ItemStack[ items.length ] );
    }

    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<>( viewers );
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new ArrayList<>( Arrays.asList( items ) ).listIterator();
    }

    @Override
    public ListIterator<ItemStack> iterator( int i ) {
        List<ItemStack> list = new ArrayList<>();
        for ( ItemStack item : items ) {
            if( item != null && item.getTypeId() == i ) {
                list.add( item );
            }
        }
        return list.listIterator();
    }

    @Override
    public Location getLocation() {
        return null;
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

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setMaxStackSize( int i ) {

    }

    @Override
    public String getName() {
        return title;
    }

    protected int getRawSlow( int virtualSlot ) {
        return virtualSlot;
    }

    public abstract boolean click( FlexPlayer player, short slot, byte button, int mode, ItemStack itemStack );

    @Override
    public void forEach( Consumer<? super ItemStack> action ) {
        for ( ItemStack item : this.items ) {
            action.accept( item );
        }
    }

    @Override
    public Spliterator<ItemStack> spliterator() {
        return new Spliterator<ItemStack>() {
            @Override
            public boolean tryAdvance( Consumer<? super ItemStack> action ) {
                return false;
            }

            @Override
            public Spliterator<ItemStack> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };
    }

}
