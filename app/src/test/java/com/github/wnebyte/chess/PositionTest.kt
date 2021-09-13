package com.github.wnebyte.chess

import com.github.wnebyte.chess.model.Position
import org.junit.Test

class PositionTest
{
    @Test
    fun test00()
    {
        val list = mutableListOf<Position>()
        var x = 1
        var y = 2

        for (i in 1..8)
        {
            list.add(Position(x++, y))
            println(list[list.size - 1].toString())
        }


    }
}