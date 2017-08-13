package me.lukas81298.flexmc.impl.scheduler;

import me.lukas81298.flexmc.Flex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author lukas
 * @since 13.08.2017
 */
public class FlexDummySyncWorker implements Runnable {

    private final Thread thread;
    private final Map<Integer, AbstractTask> runningTaks = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger( 0 );

    public FlexDummySyncWorker() {
        this.thread = new Thread( this );
        this.thread.start();
    }

    public boolean isRunning() {
        return Thread.currentThread().equals( this.thread );
    }

    @Override
    public void run() {
        while ( Flex.getServer().isRunning() ) {
            long start = System.currentTimeMillis();
            this.runningTaks.entrySet().removeIf( new Predicate<Map.Entry<Integer, AbstractTask>>() {
                @Override
                public boolean test( Map.Entry<Integer, AbstractTask> integerAbstractTaskEntry ) {
                    return integerAbstractTaskEntry.getValue().execute();
                }
            } );
            long diff = System.currentTimeMillis() - start;
            try {
                Thread.sleep( Math.max( 0, 50 - diff ) );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }

        }
    }

    public int nextId() {
        return this.idCounter.get();
    }

    public void addTask( AbstractTask task ) {
        this.runningTaks.put( task.getTaskId(), task );
    }

}
