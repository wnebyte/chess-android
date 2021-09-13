package com.github.wnebyte.chess.controller

import com.github.wnebyte.chess.model.Action
import com.github.wnebyte.chess.model.Square

class FreePlayController : GridFragment()
{
    /**
     * Called by the super class after an acceptable inputEvent.
     * Updates the ViewModel and UI.
     * Callbacks to the hosting activity if the game is finished.
     */
    override fun finalizeMove(square: Square)
    {
        val update = vm.update(square)
        audio.play(update)
        updateUI()

        when (update)
        {
            Action.CHECKMATE -> callbacks?.onGameIsFinished("CHECKMATE")
            Action.STALEMATE -> callbacks?.onGameIsFinished("STALEMATE")
        }
    }

    companion object
    {
        /**
         * @return a new instance.
         */
        fun newInstance() : FreePlayController =
                FreePlayController()
    }
}