package com.github.wnebyte.chess.model.ai

import com.github.wnebyte.chess.model.State
import com.github.wnebyte.chess.struct.Node

/**
 * Interface declares abstract functions used to generate and determine the value of individual
 * board states.
 */
interface MinMax
{
    /**
     * @return a list of Node<State> objects generated from a specified Node<State> object.
     */
    fun generate(node: Node<State>) : List<Node<State>>

    /**
     * Determines and sets the utility value of a Node<State> object using the specified UtilityFunction.
     */
    fun minmax(node: Node<State>, utilityFunction: UtilityFunction)
}