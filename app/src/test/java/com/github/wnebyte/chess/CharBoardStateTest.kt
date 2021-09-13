package com.github.wnebyte.chess

import com.github.wnebyte.chess.model.Action
import com.github.wnebyte.chess.model.Position
import com.github.wnebyte.chess.modelv2.CharBoardState
import org.junit.Test

private const val TAG = "CharBoardStateTest"

class CharBoardStateTest
{
    @Test
    fun test00() {
        val state = CharBoardState()
            .update(Action.of("G1", "H3")).update(Action.of("G2", "G3")).update(Action.of("F1", "G2"))
            .update(Action.of("E1", "G1"))
        println(state.prettify())
    }
}