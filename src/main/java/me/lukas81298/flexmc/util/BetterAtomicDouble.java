package me.lukas81298.flexmc.util;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.function.DoubleUnaryOperator;

/**
 * @author lukas
 * @since 07.08.2017
 */
public class BetterAtomicDouble extends AtomicDouble {

    public BetterAtomicDouble( double initialValue ) {
        super( initialValue );
    }

    public final double updateAndGet( DoubleUnaryOperator updateFunction ) {
        double prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsDouble( prev );
        } while ( !compareAndSet( prev, next ) );
        return next;
    }

    public final double getAndUpdate( DoubleUnaryOperator updateFunction ) {
        double prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsDouble( prev );
        } while ( !compareAndSet( prev, next ) );
        return prev;
    }

}
