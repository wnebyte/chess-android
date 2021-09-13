package com.github.wnebyte.chess

import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.struct.Queue
import org.junit.Assert
import org.junit.Test

class QueueTest
{
    @Test
    fun test00()
    {
        val queue: Queue<Color> = Queue(Color.WHITE, Color.BLACK)

        Assert.assertEquals(Color.WHITE, queue.peek())
        queue.update()
        Assert.assertEquals(Color.BLACK, queue.peek())
        queue.update()
        Assert.assertEquals(Color.WHITE, queue.peek())
    }
}