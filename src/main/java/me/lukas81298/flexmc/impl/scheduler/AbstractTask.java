package me.lukas81298.flexmc.impl.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * @author lukas
 * @since 13.08.2017
 */
public abstract class AbstractTask extends FlexSchedulerTask {

    protected final Runnable runnable;

    public AbstractTask( int taskId, Plugin owner, boolean sync, FlexScheduler scheduler, Runnable runnable ) {
        super( taskId, owner, sync, scheduler );
        this.runnable = runnable;
    }

    public abstract boolean execute();

}
