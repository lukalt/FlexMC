package me.lukas81298.flexmc.impl.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author lukas
 * @since 13.08.2017
 */
@RequiredArgsConstructor
@Getter
public class FlexSchedulerTask implements BukkitTask {

    private final int taskId;
    private final Plugin owner;
    private final boolean sync;
    private final FlexScheduler scheduler;

    @Override
    public void cancel() {
        this.scheduler.cancelTask( this.taskId );
    }
}
