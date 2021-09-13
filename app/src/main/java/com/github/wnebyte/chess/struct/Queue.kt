package com.github.wnebyte.chess.struct

import java.lang.IllegalStateException
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

open class Queue<T>(vararg items: T)
{
    private val queue: Queue<T> = ArrayBlockingQueue<T>(items.size)

    init {
        if (items.size < 2) {
            throw IllegalStateException("lt 2 items")
        }
        queue.addAll(items)
    }

    fun peek() : T = queue.peek()!!

    fun last() : T = queue.last()

    fun update() : T?
    {
        val t: T? = queue.poll()
        queue.add(t)
        return t
    }

}