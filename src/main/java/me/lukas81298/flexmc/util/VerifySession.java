package me.lukas81298.flexmc.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lukas
 * @since 09.08.2017
 */
@RequiredArgsConstructor
@Getter
public class VerifySession {

    private final byte[] token;
    private final String username;
    private final AtomicInteger state = new AtomicInteger( 0 );
}
