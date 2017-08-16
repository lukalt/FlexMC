package me.lukas81298.flexmc.world.block;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author lukas
 * @since 09.08.2017
 */
public class Blocks {

    private final static TIntObjectMap<BlockSpec> registeredBlocks = new TIntObjectHashMap<>();

    public synchronized static void initBlocks() {
        registerBlockSpec( BlockStone.class );
        registerBlockSpec( BlockGrass.class );
        registerBlockSpec( BlockLeaves.class );
        registerBlockSpec( BlockDiamondOre.class );
        registerBlockSpec( BlockCoalOre.class );
        registerBlockSpec( BlockCactus.class );
    }

    private static void registerBlockSpec( Class<? extends BlockSpec> spec ) {
        try {
            BlockSpec instance = spec.newInstance();
            registeredBlocks.put( instance.getType(), instance );
        } catch ( InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
        }
    }

    public static BlockSpec getBlockSpec( int type ) {
        return registeredBlocks.get( type );
    }
}
