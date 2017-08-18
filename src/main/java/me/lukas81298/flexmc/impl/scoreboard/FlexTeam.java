package me.lukas81298.flexmc.impl.scoreboard;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lukas
 * @since 18.08.2017
 */
@RequiredArgsConstructor
@Getter
public class FlexTeam implements Team {

    private final FlexScoreboard scoreboard;
    private final String name;

    private final Set<String> members = ConcurrentHashMap.newKeySet();
    private final Map<Option, OptionStatus> options = new EnumMap<>( Option.class );

    private volatile String prefix = "", suffix = "", displayName = "";
    private volatile ChatColor color = ChatColor.WHITE;


    @Override
    public void setDisplayName( String s ) throws IllegalStateException, IllegalArgumentException {
        this.displayName = s;
    }

    @Override
    public void setPrefix( String s ) throws IllegalStateException, IllegalArgumentException {
        this.prefix = s;
    }

    @Override
    public void setSuffix( String s ) throws IllegalStateException, IllegalArgumentException {
        this.suffix = s;
    }

    @Override
    public void setColor( ChatColor chatColor ) {
        this.color = chatColor;
    }

    @Override
    public boolean allowFriendlyFire() throws IllegalStateException {
        return false;
    }

    @Override
    public void setAllowFriendlyFire( boolean b ) throws IllegalStateException {

    }

    @Override
    public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
        return false;
    }

    @Override
    public void setCanSeeFriendlyInvisibles( boolean b ) throws IllegalStateException {

    }

    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setNameTagVisibility( NameTagVisibility nameTagVisibility ) throws IllegalArgumentException {

    }

    @Override
    public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
        return this.getEntries().stream().map( ( name ) -> Bukkit.getOfflinePlayer( name ) ).collect( Collectors.toSet() );
    }

    @Override
    public Set<String> getEntries() throws IllegalStateException {
        return ImmutableSet.copyOf( this.members );
    }

    @Override
    public int getSize() throws IllegalStateException {
        return this.members.size();
    }

    @Override
    public void addPlayer( OfflinePlayer offlinePlayer ) throws IllegalStateException, IllegalArgumentException {
        this.addEntry( offlinePlayer.getName() );
    }

    @Override
    public void addEntry( String s ) throws IllegalStateException, IllegalArgumentException {
        if ( this.members.add( s ) ) {

        }
    }

    @Override
    public boolean removePlayer( OfflinePlayer offlinePlayer ) throws IllegalStateException, IllegalArgumentException {
        return this.removeEntry( offlinePlayer.getName() );
    }

    @Override
    public boolean removeEntry( String s ) throws IllegalStateException, IllegalArgumentException {
        if ( this.members.remove( s ) ) {

            return true;
        }
        return false;
    }

    @Override
    public void unregister() throws IllegalStateException {

    }

    @Override
    public boolean hasPlayer( OfflinePlayer offlinePlayer ) throws IllegalArgumentException, IllegalStateException {
        return this.hasEntry( offlinePlayer.getName() );
    }

    @Override
    public boolean hasEntry( String s ) throws IllegalArgumentException, IllegalStateException {
        return this.members.contains( s );
    }

    @Override
    public OptionStatus getOption( Option option ) throws IllegalStateException {
        return null;
    }

    @Override
    public void setOption( Option option, OptionStatus optionStatus ) throws IllegalStateException {

    }
}
