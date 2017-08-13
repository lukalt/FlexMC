package me.lukas81298.flexmc.world;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

/**
 * @author lukas
 * @since 05.08.2017
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode( callSuper = false )
public class BlockState {

    private final Material type;
    private final int data;

    public BlockState( int typeId, int data ) {
        this.type = Material.getMaterial( typeId );
        this.data = data;
    }

    public BlockState( int typeId ) {
        this( typeId, 0 );
    }

    public BlockState( Material material ) {
        this( material, 0 );
    }

    public int getTypeId() {
        return this.type.getId();
    }
}
