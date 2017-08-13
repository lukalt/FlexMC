package me.lukas81298.flexmc.inventory.meta;

import gnu.trove.TCollections;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectIntProcedure;
import io.gomint.taglib.NBTTagCompound;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lukas
 * @since 13.08.2017
 */
@EqualsAndHashCode( callSuper = false )
public class FlexItemMeta implements ItemMeta {

    @Getter
    @Setter
    private String displayName = null;
    @Getter
    @Setter
    private String localizedName = null;
    @Getter
    @Setter
    private List<String> lore = Collections.emptyList();
    @Getter
    @Setter
    private boolean unbreakable = false;
    @Getter
    private final TObjectIntMap<Enchantment> enchantments = TCollections.synchronizedMap( new TObjectIntHashMap<>( 10, .75F, 0 ) );
    private final Set<ItemFlag> flags = ConcurrentHashMap.newKeySet();

    @Override
    public boolean hasDisplayName() {
        return this.displayName != null;
    }

    @Override
    public boolean hasLocalizedName() {
        return this.localizedName != null;
    }


    @Override
    public boolean hasLore() {
        return lore != null && !lore.isEmpty();
    }

    @Override
    public boolean hasEnchants() {
        synchronized ( this.enchantments ) {
            if( this.enchantments.isEmpty() ) {
                return false;
            }
            for( int i : this.enchantments.values() ) {
                if( i > 0 ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasEnchant( Enchantment enchantment ) {
        return this.getEnchantLevel( enchantment ) > 0;
    }

    @Override
    public int getEnchantLevel( Enchantment enchantment ) {
        return this.enchantments.get( enchantment );
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        Map<Enchantment, Integer> map = new HashMap<>();
        this.enchantments.forEachEntry( new TObjectIntProcedure<Enchantment>() {
            @Override
            public boolean execute( Enchantment enchantment, int i ) {
                map.put( enchantment, i );
                return true;
            }
        } );
        return map;
    }

    @Override
    public boolean addEnchant( Enchantment enchantment, int i, boolean b ) {
        this.enchantments.put( enchantment, Math.max( 1, i ) );
        return true; // todo check if applicable
    }

    @Override
    public boolean removeEnchant( Enchantment enchantment ) {
        return this.enchantments.remove( enchantment ) != 0;
    }

    @Override
    public boolean hasConflictingEnchant( Enchantment enchantment ) {
        return false;
    }

    @Override
    public void addItemFlags( ItemFlag... itemFlags ) {
        this.flags.addAll( Arrays.asList( itemFlags ) );
    }

    @Override
    public void removeItemFlags( ItemFlag... itemFlags ) {
        for ( ItemFlag itemFlag : itemFlags ) {
            this.flags.remove( itemFlag );
        }
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return this.flags;
    }

    @Override
    public boolean hasItemFlag( ItemFlag itemFlag ) {
        return !this.flags.isEmpty();
    }

    @Override
    public ItemMeta clone() {
        FlexItemMeta n = new FlexItemMeta();
        n.setDisplayName( this.displayName );
        n.setLore( this.lore );
        n.setLocalizedName( this.localizedName );
        n.flags.addAll( this.flags );
        n.enchantments.putAll( this.enchantments );
        n.unbreakable = this.unbreakable;
        return n;
    }

    @Override
    public Map<String, Object> serialize() {
        return Collections.emptyMap();
    }

    public synchronized void write( NBTTagCompound tagCompound ) {
        if( this.displayName != null ) {
            tagCompound.addValue( "Name", this.displayName );
        }
        if( this.hasLore() ) {
            tagCompound.addValue( "Lore", this.lore );
        }
        if( this.localizedName != null ) {
            tagCompound.addValue( "LocName", this.localizedName );
        }
        tagCompound.addValue( "Unbreakable", (byte) ( this.unbreakable ? 1 : 0 ) );

    }
}
