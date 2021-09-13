package com.github.wnebyte.chess

import com.github.wnebyte.chess.struct.Node
import org.junit.Assert
import org.junit.Test

class NodeTest
{
    @Test
    fun testDepth()
    {
        val root = Node<String>("root")
        val node1 = Node("state1")
                .addChildren(listOf(Node("state2"), Node("state3")))
        root.addChild(node1)
        root.addChild(Node("state4"))
        val node2 = Node("state6")
        root.addChild(Node("state5").addChildren(listOf(node2, Node("state6"), Node("state7"))))

        Assert.assertEquals(0, root.getDepth())
        Assert.assertEquals(1, node1.getDepth())
        Assert.assertEquals(2, node2.getDepth())
    }
}