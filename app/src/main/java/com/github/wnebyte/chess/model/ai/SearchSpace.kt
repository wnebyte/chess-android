package com.github.wnebyte.chess.model.ai

import com.github.wnebyte.chess.model.Action
import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.model.Player
import com.github.wnebyte.chess.model.State
import com.github.wnebyte.chess.struct.Node

private const val TAG = "SearchSpace"

/**
 * Class represents a space of Node<State> object reachable from a specified state object.
 */
class SearchSpace(val root: Node<State>, val player: Player, val terminalDepth: Int)
{
    private val friendly: MinMax = when(player.color)
    {
        Color.WHITE -> Max()
        Color.BLACK -> Min()
    }

    private val opposition: MinMax = when(player.color)
    {
        Color.WHITE -> Min()
        Color.BLACK -> Max()
    }

    /**
     * Determines the utility value of a given model.State.
     */
    private val utilityFunction: UtilityFunction = object : UtilityFunction
    {
        override fun get(state: State): Double
        {
            var utility = 0.0

            for (chessPiece in state.getChessPieces().values)
            {
                if (chessPiece.color == Color.WHITE)
                {
                    utility += chessPiece.value
                }
                else {
                    utility -= chessPiece.value
                }
            }

            return utility
        }
    }

    /**
     * The only public method of this class returns a decision in the form of a model.Action object.
     */
    fun decision() : Action
    {
        populateTree(root)
        return minmax().action!!
    }

    /**
     * @return true if the terminal depth has been reached or if a terminal state (checkmate)
     * has been reached.
     */
    private fun cutoffTest(node: Node<State>, depth: Int) : Boolean =
            (terminalDepth <= depth) || (node.getData().checkmate()) || (node.getData().stalemate())

    /**
     * Populates the tree until a terminal depth/state has been reached.
     */
    private fun populateTree(node: Node<State>)
    {
        val depth: Int = node.getDepth()

        if (cutoffTest(node, depth))
        {
            if (depth % 2 == 0) {
                opposition.minmax(node, utilityFunction)
            }
            else {
                friendly.minmax(node, utilityFunction)
            }
            return
        }

        val nodes: List<Node<State>> = if (depth % 2 == 0) {
            friendly.generate(node)
        }
        else {
            opposition.generate(node)
        }

        for (n in nodes)
        {
            node.addChild(n)
            populateTree(n)
        }
    }

    /**
     * Is called after populateTree() to propagate the utility value of the leaf nodes up the tree.
     * @return the optimal State object that is a direct child of the root state.
     */
    private fun minmax() : State
    {
        for (depth in terminalDepth - 1 downTo 0)
        {
            for (node in root.nthDepthNodes(depth))
            {
                if (depth % 2 == 0) {
                    friendly.minmax(node, utilityFunction)
                }
                else {
                    opposition.minmax(node, utilityFunction)
                }
            }
        }

        root.getChildren().shuffle()
        return when (friendly is Min)
        {
            true -> root.getChildren().minBy { it.getData().utility }!!.getData()
            false -> root.getChildren().maxBy { it.getData().utility }!!.getData()
        }
    }
}