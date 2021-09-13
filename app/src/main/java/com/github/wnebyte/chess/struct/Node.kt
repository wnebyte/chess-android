package com.github.wnebyte.chess.struct

import kotlin.math.max

class Node<T>
{
    private val data: T

    private var parent: Node<T>?

    private val children: MutableList<Node<T>> = mutableListOf()

    constructor(data: T)
    {
        this.data = data
        this.parent = null
    }

    constructor(data: T, parent: Node<T>)
    {
        this.data = data
        this.parent = parent
    }

    fun addChild(child: Node<T>)
    {
        child.setParent(this)
        this.children.add(child)
    }

    fun addChildren(children: List<Node<T>>): Node<T>
    {
        children.forEach { child -> this.addChild(child) }
        return this
    }

    fun getChildren(): MutableList<Node<T>> = this.children

    fun setParent(parent: Node<T>)
    {
        this.parent = parent
    }

    fun getParent() : Node<T>? = this.parent

    fun getData() : T = data

    fun isRoot() : Boolean = (this.getParent() == null)

    fun isLeaf() : Boolean = this.children.isEmpty()

    fun getHeight() : Int
    {
        if (isLeaf()) {
            return 0
        }

        var h = 0
        for (node in getChildren())
        {
            h = max(h, node.getHeight())
        }
        return h + 1
    }

    fun getDepth() : Int
    {
        if (isRoot()) {
            return 0
        }

        var d = 0
        d = max(d, getParent()!!.getDepth())
        return d + 1
    }

    fun nthDepthNodes(n: Int) : List<Node<T>>
    {
        val nodes = mutableListOf<Node<T>>()

        if (getDepth() == n) {
            nodes.add(this)
        }
        else {
            for (node in getChildren()) {
                nodes.addAll(node.nthDepthNodes(n))
            }
        }
        return nodes
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Node<*>)
            return false

        @Suppress("UNCHECKED_CAST")
        val node: Node<T> = other as Node<T>
        return node.getParent() == this.getParent() && node.getChildren() == this.getChildren() &&
                node.getData() == this.getData()
    }

    override fun hashCode(): Int =
            12 + getParent().hashCode() + 3 + getChildren().hashCode() + 7 + getData().hashCode()
}