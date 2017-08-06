package me.lukas81298.flexmc.world;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 05.08.2017
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode( callSuper = false )
public class BlockState {

    private final int id, data;

}
