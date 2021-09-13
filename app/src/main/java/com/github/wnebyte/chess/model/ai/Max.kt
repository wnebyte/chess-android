package com.github.wnebyte.chess.model.ai

import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.model.State
import com.github.wnebyte.chess.struct.Node

/**
 * Class implements the model.ai.MinMax interface.
 */
class Max : MinMax
{
    /** @inheritDoc */
    override fun generate(node: Node<State>): List<Node<State>>
    {
        val children = mutableListOf<Node<State>>()

        for (kv in node.getData().getChessPieces().filter { kv1 -> kv1.value.color == Color.WHITE })
        {
            for (position in kv.value.legalMoves(node.getData()))
            {
                children.add(Node(State.copy(node.getData())
                        .update(kv.value, position)))
            }
        }
        return children
    }

    /** @inheritDoc */
    override fun minmax(node: Node<State>, utilityFunction: UtilityFunction)
    {
        if (node.isLeaf())
        {
            node.getData().utility = utilityFunction.get(node.getData())
        }
        else
        {
            node.getData().utility =
                    node.getChildren().maxBy { it.getData().utility }!!.getData().utility
        }
    }

}