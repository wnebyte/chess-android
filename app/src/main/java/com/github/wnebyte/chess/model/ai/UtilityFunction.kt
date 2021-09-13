package com.github.wnebyte.chess.model.ai

import com.github.wnebyte.chess.model.State

/**
 * Interface is to determine the utility value of a model.State object.
 */
interface UtilityFunction
{
    /**
     * @return the utility value of the specified model.State object.
     */
    fun get(state: State) : Double
}