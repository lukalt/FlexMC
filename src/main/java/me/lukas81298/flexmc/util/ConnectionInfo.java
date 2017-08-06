package me.lukas81298.flexmc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lukas
 * @since 04.08.2017
 */
@AllArgsConstructor
@Getter
public class ConnectionInfo {

    private final String host;
    private final short port;

}
