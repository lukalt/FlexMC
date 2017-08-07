package me.lukas81298.flexmc.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 07.08.2017
 */
@RequiredArgsConstructor
@Getter
public class BiTuple<K, V> {

    private final K a;
    private final V b;
}
