package me.lukas81298.flexmc;

import me.lukas81298.flexmc.entity.FlexPlayer;
import me.lukas81298.flexmc.impl.scheduler.FlexScheduler;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class FlexServerImpl implements Server {

    private final FlexServer flex;
    private final FlexScheduler scheduler;

    private volatile GameMode defaultGameMode = GameMode.SURVIVAL;

    public FlexServerImpl( FlexServer flex ) {
        this.flex = flex;
        this.scheduler = new FlexScheduler();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getVersion() {
        return "1.12.1-0.1-SNAPSHOT";
    }

    @Override
    public String getBukkitVersion() {
        return "1.12.1-R0.1-SNAPSHOT";
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return null;
    }

    @Override
    public int getMaxPlayers() {
        return flex.getConfig().getMaxPlayers();
    }

    @Override
    public int getPort() {
        return flex.getConfig().getServerPort();
    }

    @Override
    public int getViewDistance() {
        return 8;
    }

    @Override
    public String getIp() {
        return flex.getConfig().getServerAddress();
    }

    @Override
    public String getServerName() {
        return flex.getWorld().getName();
    }

    @Override
    public String getServerId() {
        return "";
    }

    @Override
    public String getWorldType() {
        return "normal";
    }

    @Override
    public boolean getGenerateStructures() {
        return false;
    }

    @Override
    public boolean getAllowEnd() {
        return false;
    }

    @Override
    public boolean getAllowNether() {
        return false;
    }

    @Override
    public boolean hasWhitelist() {
        return false;
    }

    @Override
    public void setWhitelist( boolean b ) {

    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        return Collections.emptySet(); // todo implement
    }

    @Override
    public void reloadWhitelist() {

    }

    @Override
    public int broadcastMessage( String s ) {
        int i = 0;
        for ( FlexPlayer player : flex.getPlayerManager().getOnlinePlayers() ) {
            player.sendMessage( s );
            i++;
        }
        return i;
    }

    @Override
    public String getUpdateFolder() {
        return "update";
    }

    @Override
    public File getUpdateFolderFile() {
        return new File( getUpdateFolder() );
    }

    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public Player getPlayer( String s ) {
        return (Player) this.flex.getPlayerManager().getPlayer( s );
    }

    @Override
    public Player getPlayerExact( String s ) {
        return (Player) this.flex.getPlayerManager().getPlayer( s );
    }

    @Override
    public List<Player> matchPlayer( String s ) {
        List<Player> players = new ArrayList<>();
        for ( FlexPlayer player : this.flex.getPlayerManager().getOnlinePlayers() ) {
            if( player.getName().toLowerCase().startsWith( s.toLowerCase() ) ) {
                players.add( (Player) player );
            }
        }
        return players;
    }

    @Override
    public Player getPlayer( UUID uuid ) {
        return (Player) this.flex.getPlayerManager().getPlayer( uuid );
    }

    @Override
    public PluginManager getPluginManager() {
        return this.flex.getPluginManager();
    }

    @Override
    public BukkitScheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

    @Override
    public List<World> getWorlds() {
        return null;
    }

    @Override
    public World createWorld( WorldCreator worldCreator ) {
        return null;
    }

    @Override
    public boolean unloadWorld( String s, boolean b ) {
        return false;
    }

    @Override
    public boolean unloadWorld( World world, boolean b ) {
        return false;
    }

    @Override
    public World getWorld( String s ) {
        return null;
    }

    @Override
    public World getWorld( UUID uuid ) {
        return null;
    }

    @Override
    public MapView getMap( short i ) {
        return null;
    }

    @Override
    public MapView createMap( World world ) {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public void reloadData() {

    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public PluginCommand getPluginCommand( String s ) {
        return null;
    }

    @Override
    public void savePlayers() {

    }

    @Override
    public boolean dispatchCommand( CommandSender commandSender, String s ) throws CommandException {
        return false;
    }

    @Override
    public boolean addRecipe( Recipe recipe ) {
        return false;
    }

    @Override
    public List<Recipe> getRecipesFor( ItemStack itemStack ) {
        return null;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;
    }

    @Override
    public void clearRecipes() {

    }

    @Override
    public void resetRecipes() {

    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        return null;
    }

    @Override
    public int getSpawnRadius() {
        return 0;
    }

    @Override
    public void setSpawnRadius( int i ) {

    }

    @Override
    public boolean getOnlineMode() {
        return this.flex.getConfig().isVerifyUsers();
    }

    @Override
    public boolean getAllowFlight() {
        return false;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public void shutdown() {
        this.flex.stop();
    }

    @Override
    public int broadcast( String s, String s1 ) {
        return 0;
    }

    @Override
    public OfflinePlayer getOfflinePlayer( String s ) {
        return null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer( UUID uuid ) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return Collections.emptySet();
    }

    @Override
    public void banIP( String s ) {

    }

    @Override
    public void unbanIP( String s ) {

    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return Collections.emptySet();
    }

    @Override
    public BanList getBanList( BanList.Type type ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        return Collections.emptySet();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return this.defaultGameMode;
    }

    @Override
    public void setDefaultGameMode( GameMode gameMode ) {
        this.defaultGameMode = gameMode;
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;
    }

    @Override
    public File getWorldContainer() {
        return new File( this.flex.getWorldManager().getConfig().getWorldContainer() );
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Messenger getMessenger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HelpMap getHelpMap() {
       throw new UnsupportedOperationException();
    }

    @Override
    public Inventory createInventory( InventoryHolder inventoryHolder, InventoryType inventoryType ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Inventory createInventory( InventoryHolder inventoryHolder, InventoryType inventoryType, String s ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Inventory createInventory( InventoryHolder inventoryHolder, int i ) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Inventory createInventory( InventoryHolder inventoryHolder, int i, String s ) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Merchant createMerchant( String s ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    @Deprecated
    public boolean isPrimaryThread() {
        return this.scheduler.getDummySyncWorker().isRunning();
    }

    @Override
    public String getMotd() {
        return this.flex.getConfig().getServerName();
    }

    @Override
    public String getShutdownMessage() {
        return "Server closed";
    }

    @Override
    public Warning.WarningState getWarningState() {
        return Warning.WarningState.OFF;
    }

    @Override
    public ItemFactory getItemFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CachedServerIcon getServerIcon() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CachedServerIcon loadServerIcon( File file ) throws IllegalArgumentException, Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public CachedServerIcon loadServerIcon( BufferedImage bufferedImage ) throws IllegalArgumentException, Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdleTimeout( int i ) {

    }

    @Override
    public int getIdleTimeout() {
        return 0;
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData( World world ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BossBar createBossBar( String s, BarColor barColor, BarStyle barStyle, BarFlag... barFlags ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getEntity( UUID uuid ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Advancement getAdvancement( NamespacedKey namespacedKey ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Advancement> advancementIterator() {
        return Collections.<Advancement>emptyList().iterator();
    }

    @Override
    public UnsafeValues getUnsafe() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendPluginMessage( Plugin plugin, String s, byte[] bytes ) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return Collections.emptySet();
    }
}
