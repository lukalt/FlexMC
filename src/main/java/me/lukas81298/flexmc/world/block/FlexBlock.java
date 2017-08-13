package me.lukas81298.flexmc.world.block;

import lombok.AllArgsConstructor;
import me.lukas81298.flexmc.util.Vector3i;
import me.lukas81298.flexmc.world.FlexWorld;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author lukas
 * @since 13.08.2017
 */
@AllArgsConstructor
public class FlexBlock implements Block {

    private FlexWorld world;
    private Vector3i position;
    private me.lukas81298.flexmc.world.BlockState state;

    @Override
    public byte getData() {
        return (byte) state.getData();
    }

    @Override
    public Block getRelative( int i, int i1, int i2 ) {
        return null;
    }

    @Override
    public Block getRelative( BlockFace blockFace ) {
        return null;
    }

    @Override
    public Block getRelative( BlockFace blockFace, int i ) {
        return null;
    }

    @Override
    public Material getType() {
        return state.getType();
    }

    @Override
    public int getTypeId() {
        return state.getTypeId();
    }

    @Override
    public byte getLightLevel() {
        return 15;
    }

    @Override
    public byte getLightFromSky() {
        return 15;
    }

    @Override
    public byte getLightFromBlocks() {
        return 15;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @Override
    public Location getLocation() {
        return new Location( world, getX(), getY(), getZ() );
    }

    @Override
    public Location getLocation( Location location ) {
        location.setX( getX() );
        location.setY( getY() );
        location.setZ( getZ() );
        return location;
    }

    @Override
    public Chunk getChunk() {
        return world.getChunkAt( getX(), getZ() );
    }

    @Override
    public synchronized void setData( byte b ) {
        state = new me.lukas81298.flexmc.world.BlockState( getType(), b );
        world.setBlock( position, state );
    }

    @Override
    public synchronized void setData( byte b, boolean b1 ) {
        state = new me.lukas81298.flexmc.world.BlockState( getType(), b );
        world.setBlock( position, state );
    }

    @Override
    public synchronized void setType( Material material ) {
        state = new me.lukas81298.flexmc.world.BlockState( material, getData() );
        world.setBlock( position, state );
    }

    @Override
    public synchronized void setType( Material material, boolean b ) {
        state = new me.lukas81298.flexmc.world.BlockState( material, getData() );
        world.setBlock( position, state );
    }

    @Override
    public synchronized boolean setTypeId( int i ) {
        state = new me.lukas81298.flexmc.world.BlockState( i, getData() );
        world.setBlock( position, state );
        return true;
    }

    @Override
    public synchronized boolean setTypeId( int i, boolean b ) {
        state = new me.lukas81298.flexmc.world.BlockState( i, getData() );
        world.setBlock( position, state );
        return true;
    }

    @Override
    public synchronized boolean setTypeIdAndData( int i, byte b, boolean b1 ) {
        state = new me.lukas81298.flexmc.world.BlockState( i, b );
        world.setBlock( position, state );
        return true;
    }

    @Override
    public BlockFace getFace( Block block ) {
        return null;
    }

    @Override
    public BlockState getState() {
        return null;
    }

    @Override
    public Biome getBiome() {
        return world.getBiome( getX(), getZ() );
    }

    @Override
    public void setBiome( Biome biome ) {
        world.setBiome( getX(), getZ(), biome );
    }

    @Override
    public boolean isBlockPowered() {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isBlockFacePowered( BlockFace blockFace ) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered( BlockFace blockFace ) {
        return false;
    }

    @Override
    public int getBlockPower( BlockFace blockFace ) {
        return 0;
    }

    @Override
    public int getBlockPower() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return getType() == Material.AIR;
    }

    @Override
    public boolean isLiquid() {
        switch ( getType() ) {
            case LAVA:
            case STATIONARY_LAVA:
            case WATER:
            case STATIONARY_WATER:
                return true;

        }
        return false;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getHumidity() {
        return 0;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.IGNORE;
    }

    @Override
    public boolean breakNaturally() {
        return false;
    }

    @Override
    public boolean breakNaturally( ItemStack itemStack ) {
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return null;
    }

    @Override
    public Collection<ItemStack> getDrops( ItemStack itemStack ) {
        return null;
    }

    @Override
    public void setMetadata( String s, MetadataValue metadataValue ) {

    }

    @Override
    public List<MetadataValue> getMetadata( String s ) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasMetadata( String s ) {
        return false;
    }

    @Override
    public void removeMetadata( String s, Plugin plugin ) {

    }
}
