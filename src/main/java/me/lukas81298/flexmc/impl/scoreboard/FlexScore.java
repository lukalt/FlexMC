package me.lukas81298.flexmc.impl.scoreboard;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author lukas
 * @since 18.08.2017
 */
@RequiredArgsConstructor
public class FlexScore implements Score {

    private final FlexObjective objective;
    private final String name;
    private volatile int score;

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer( this.name );
    }

    @Override
    public String getEntry() {
        return this.name;
    }

    @Override
    public Objective getObjective() {
        return this.objective;
    }

    @Override
    public int getScore() throws IllegalStateException {
        return this.score;
    }

    @Override
    public void setScore( int i ) throws IllegalStateException {
        this.score = i;
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }
}
