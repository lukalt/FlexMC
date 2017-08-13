package me.lukas81298.flexmc.impl.scheduler;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class FlexScheduler implements BukkitScheduler {

    @Getter
    private final FlexDummySyncWorker dummySyncWorker = new FlexDummySyncWorker();

    @Override
    @Deprecated
    public int scheduleSyncDelayedTask( Plugin plugin, Runnable runnable, long l ) {
        return this.runTaskLater( plugin, runnable, l ).getTaskId();
    }

    @Override
    @Deprecated
    public int scheduleSyncDelayedTask( Plugin plugin, BukkitRunnable bukkitRunnable, long l ) {
        return this.runTaskLater( plugin, bukkitRunnable, l ).getTaskId();
    }

    @Override
    @Deprecated
    public int scheduleSyncDelayedTask( Plugin plugin, Runnable runnable ) {
        return 0;
    }

    @Override
    @Deprecated
    public int scheduleSyncDelayedTask( Plugin plugin, BukkitRunnable bukkitRunnable ) {
        return 0;
    }

    @Override
    @Deprecated
    public int scheduleSyncRepeatingTask( Plugin plugin, Runnable runnable, long l, long l1 ) {
        return 0;
    }

    @Override
    @Deprecated
    public int scheduleSyncRepeatingTask( Plugin plugin, BukkitRunnable bukkitRunnable, long l, long l1 ) {
        return 0;
    }

    @Override
    @Deprecated
    public int scheduleAsyncDelayedTask( Plugin plugin, Runnable runnable, long l ) {
        return 0;
    }

    @Override
    @Deprecated
    public int scheduleAsyncDelayedTask( Plugin plugin, Runnable runnable ) {
        return 0;
    }

    @Override
    @Deprecated
    public int scheduleAsyncRepeatingTask( Plugin plugin, Runnable runnable, long l, long l1 ) {
        return 0;
    }

    @Override
    @Deprecated
    public <T> Future<T> callSyncMethod( Plugin plugin, Callable<T> callable ) {
        return null;
    }

    @Override
    public void cancelTask( int i ) {

    }

    @Override
    public void cancelTasks( Plugin plugin ) {

    }

    @Override
    public void cancelAllTasks() {

    }

    @Override
    public boolean isCurrentlyRunning( int i ) {
        return false;
    }

    @Override
    public boolean isQueued( int i ) {
        return false;
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return Collections.emptyList();
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return Collections.emptyList();
    }

    @Override
    public BukkitTask runTask( Plugin plugin, Runnable runnable ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTask( Plugin plugin, BukkitRunnable bukkitRunnable ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskAsynchronously( Plugin plugin, Runnable runnable ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskAsynchronously( Plugin plugin, BukkitRunnable bukkitRunnable ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskLater( Plugin plugin, Runnable runnable, long l ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskLater( Plugin plugin, BukkitRunnable bukkitRunnable, long l ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously( Plugin plugin, Runnable runnable, long l ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously( Plugin plugin, BukkitRunnable bukkitRunnable, long l ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskTimer( Plugin plugin, Runnable runnable, long l, long l1 ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskTimer( Plugin plugin, BukkitRunnable bukkitRunnable, long l, long l1 ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously( Plugin plugin, Runnable runnable, long l, long l1 ) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously( Plugin plugin, BukkitRunnable bukkitRunnable, long l, long l1 ) throws IllegalArgumentException {
        return null;
    }
}
