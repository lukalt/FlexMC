package me.lukas81298.flexmc.impl.scoreboard;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lukas
 * @since 18.08.2017
 */
public class FlexScoreboard implements Scoreboard {

    private final Map<String, FlexTeam> teams = new ConcurrentHashMap<>();
    private final Map<String, FlexObjective> objectives = new ConcurrentHashMap<>();

    @Override
    public Objective registerNewObjective( String s, String s1 ) throws IllegalArgumentException {
        return this.objectives.computeIfAbsent( s, new Function<String, FlexObjective>() {
            @Override
            public FlexObjective apply( String s ) {
                return new FlexObjective( FlexScoreboard.this, s, s1 );
            }
        } );
    }

    @Override
    public Objective getObjective( String s ) throws IllegalArgumentException {
        return this.objectives.get( s );
    }

    @Override
    public Set<Objective> getObjectivesByCriteria( String s ) throws IllegalArgumentException {
        Set<Objective> objectives = new HashSet<>();
        for ( FlexObjective flexObjective : this.objectives.values() ) {
            if ( flexObjective.getCriteria().equals( s ) ) {
                objectives.add( flexObjective );
            }
        }
        return objectives;
    }

    @Override
    public Set<Objective> getObjectives() {
        return ImmutableSet.copyOf( this.objectives.values() );
    }

    @Override
    public Objective getObjective( DisplaySlot displaySlot ) throws IllegalArgumentException {
        for ( FlexObjective flexObjective : this.objectives.values() ) {
            if ( flexObjective.getDisplaySlot() == displaySlot ) {
                return flexObjective;
            }
        }
        return null;
    }

    @Override
    public Set<Score> getScores( OfflinePlayer offlinePlayer ) throws IllegalArgumentException {
        return getScores( offlinePlayer.getName() );
    }

    @Override
    public Set<Score> getScores( String s ) throws IllegalArgumentException {
        return Collections.emptySet();
    }

    @Override
    public void resetScores( OfflinePlayer offlinePlayer ) throws IllegalArgumentException {

    }

    @Override
    public void resetScores( String s ) throws IllegalArgumentException {

    }

    @Override
    public Team getPlayerTeam( OfflinePlayer offlinePlayer ) throws IllegalArgumentException {
        return this.getEntryTeam( offlinePlayer.getName() );
    }

    @Override
    public Team getEntryTeam( String s ) throws IllegalArgumentException {
        for ( FlexTeam flexTeam : this.teams.values() ) {
            if ( flexTeam.hasEntry( s ) ) {
                return flexTeam;
            }
        }
        return null;
    }

    @Override
    public Team getTeam( String s ) throws IllegalArgumentException {
        return this.teams.get( s );
    }

    @Override
    public Set<Team> getTeams() {
        return ImmutableSet.copyOf( this.teams.values() );
    }

    @Override
    public Team registerNewTeam( String s ) throws IllegalArgumentException {
        return this.teams.computeIfAbsent( s, new Function<String, FlexTeam>() {
            @Override
            public FlexTeam apply( String s ) {
                return new FlexTeam( FlexScoreboard.this, s );
            }
        } );
    }

    @Override
    @Deprecated
    public Set<OfflinePlayer> getPlayers() {
        return this.getEntries().stream().map( name -> Bukkit.getOfflinePlayer( name ) ).collect( Collectors.toSet() );
    }

    @Override
    public Set<String> getEntries() {
        Set<String> s = new HashSet<>();
        for ( FlexObjective flexObjective : this.objectives.values() ) {
            s.addAll( flexObjective.getScores().values().stream().map( ( score ) -> score.getEntry() ).collect( Collectors.toSet() ) );
        }
        return s;
    }

    @Override
    public void clearSlot( DisplaySlot displaySlot ) throws IllegalArgumentException {

    }
}
