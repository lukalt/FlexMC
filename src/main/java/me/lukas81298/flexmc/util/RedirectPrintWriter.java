package me.lukas81298.flexmc.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * @author lukas
 * @since 14.08.2017
 */
public class RedirectPrintWriter extends PrintStream {

    private final Logger logger;

    public RedirectPrintWriter( Logger logger ) {
        super( new ByteArrayOutputStream() );
        this.logger = logger;
    }

    @Override
    public void print( boolean b ) {
        logger.info( b ? "true" : "false" );
    }

    @Override
    public void print( char c ) {
        logger.info( Character.toString( c ) );
    }

    @Override
    public void print( int i ) {
        logger.info( Integer.toString( i ) );
    }

    @Override
    public void print( long l ) {
        logger.info( Long.toString( l ) );
    }

    @Override
    public void print( float f ) {
        super.print( f );
    }

    @Override
    public void print( double d ) {
        super.print( d );
    }

    @Override
    public void print( char[] s ) {
        super.print( s );
    }

    @Override
    public void print( String s ) {
        super.print( s );
    }

    @Override
    public void print( Object obj ) {
        super.print( obj );
    }

    @Override
    public void println() {
        super.println();
    }

    @Override
    public void println( boolean x ) {
        super.println( x );
    }

    @Override
    public void println( char x ) {
        super.println( x );
    }

    @Override
    public void println( int x ) {
        super.println( x );
    }

    @Override
    public void println( long x ) {
        super.println( x );
    }

    @Override
    public void println( float x ) {
        super.println( x );
    }

    @Override
    public void println( double x ) {
        super.println( x );
    }

    @Override
    public void println( char[] x ) {
        super.println( x );
    }

    @Override
    public void println( String x ) {
        logger.info( x );
    }

    @Override
    public void println( Object x ) {
        logger.info( x == null ? "null" : x.toString() );
    }

    @Override
    public PrintStream printf( String format, Object... args ) {
        return super.printf( format, args );
    }

    @Override
    public PrintStream format( String format, Object... args ) {
        return super.format( format, args );
    }
}
