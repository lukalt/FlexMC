package me.lukas81298.flexmc.util;

import gnu.trove.set.TByteSet;
import gnu.trove.set.hash.TByteHashSet;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lukas
 * @since 17.08.2017
 */
@RequiredArgsConstructor
public class IdProvider {

    private final byte max;
    private final TByteSet usedIds = new TByteHashSet();
    private final Lock lock = new ReentrantLock();

    public void freeId( byte b ) {
        this.lock.lock();
        try {
            this.usedIds.remove( b );
        } finally {
            this.lock.unlock();
        }
    }
    public byte getNextId() {
        this.lock.lock();
        try {
            byte b = 0;
            do {
                b++;
            } while ( b <= this.max && this.usedIds.contains( b ) );
            this.usedIds.add( b );
            return b;
        } finally {
            this.lock.unlock();
        }
    }

    protected void clearAll() {
        this.lock.lock();
        try {
            this.usedIds.clear();
        } finally {
            this.lock.unlock();
        }
    }
}
