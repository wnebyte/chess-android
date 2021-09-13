package com.github.wnebyte.chess

import com.github.wnebyte.chess.model.Action
import com.github.wnebyte.chess.model.Position
import org.junit.Assert
import org.junit.Test

class ActionTest
{
    @Test
    fun test00() {
        val start = Position('A', 5)
        val end = Position('G', 8)
        val start1 = Position.of("A5")
        val end1 = Position.of("G8")

        val action = Action(start, end)
        val action1 = Action.of("A5", "G8")

        Assert.assertEquals(start1, start)
        Assert.assertEquals(end1, end)
        Assert.assertEquals(action1, action)
    }
}