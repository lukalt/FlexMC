package me.lukas81298.flexmc.impl.scoreboard;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author lukas
 * @since 18.08.2017
 */
@RequiredArgsConstructor
public class FlexObjective implements Objective {

    @Getter
    private final FlexScoreboard scoreboard;

    @Getter
    private final String name;
    @Getter
    private String displayName = "";
    @Getter
    private final String criteria;
    @Getter( AccessLevel.PACKAGE )
    private final Map<String, FlexScore> scores = new ConcurrentHashMap<>();
    private DisplaySlot displaySlot = DisplaySlot.SIDEBAR;

    @Override
    public void setDisplayName( String s ) throws IllegalStateException, IllegalArgumentException {
        this.displayName = s;
    }

    @Override
    public boolean isModifiable() throws IllegalStateException {
        return true;
    }

    @Override
    public void unregister() throws IllegalStateException {

    }

    @Override
    public void setDisplaySlot( DisplaySlot displaySlot ) throws IllegalStateException {
        this.displaySlot = displaySlot;
    }

    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        return this.displaySlot;
    }

    @Override
    public Score getScore( OfflinePlayer offlinePlayer ) throws IllegalArgumentException, IllegalStateException {
        return this.getScore( offlinePlayer.getName() );
    }

    @Override
    public Score getScore( String s ) throws IllegalArgumentException, IllegalStateException {
        return this.scores.computeIfAbsent( s, new Function<String, FlexScore>() {
            @Override
            public FlexScore apply( String s ) {
                return new FlexScore( FlexObjective.this, s );
            }
        } );
    }

}
