package me.lukas81298.flexmc.impl.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class DelayedTask extends AbstractTask {

    private int delay;
    public DelayedTask( int delay, int taskId, Plugin owner, boolean sync, FlexScheduler scheduler, Runnable runnable ) {
        super( taskId, owner, sync, scheduler, runnable );
        this.delay = delay;
    }

    @Override
    public boolean execute() {
        this.delay--;
        if( this.delay <= 0 ) {
            this.runnable.run();
            return true;
        }
        return false;
    }

}
