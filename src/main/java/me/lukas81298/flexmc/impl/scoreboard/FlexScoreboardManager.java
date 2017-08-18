package me.lukas81298.flexmc.impl.scoreboard;

import lombok.Getter;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * @author lukas
 * @since 18.08.2017
 */
public class FlexScoreboardManager implements ScoreboardManager {

    @Getter
    private final FlexScoreboard mainScoreboard = new FlexScoreboard();

    @Override
    public Scoreboard getNewScoreboard() {
        return new FlexScoreboard();
    }
}
