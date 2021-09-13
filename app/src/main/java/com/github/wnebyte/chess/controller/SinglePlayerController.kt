package com.github.wnebyte.chess.controller

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.github.wnebyte.chess.model.Action
import com.github.wnebyte.chess.model.Player
import com.github.wnebyte.chess.model.Square
import com.github.wnebyte.chess.model.ai.SearchSpace
import com.github.wnebyte.chess.struct.Node

private const val ARG_DIFFICULTY = "ARG_DIFFICULTY"

class SinglePlayerController : GridFragment()
{
    private val mainHandler = Handler(Looper.getMainLooper())

    private val handlerThread = HandlerThread("HandlerThread").apply {
        start()
    }

    /** Handler is responsible for interacting with the model.ai package and finding the next
     * move for the computer.
     * After which it posts to the main thread to update the model and UI.
     */
    private val backgroundHandler = Handler(handlerThread.looper, Handler.Callback {
        try {
            val action = SearchSpace(Node(vm.toState()), vm.players.peek(),
                arguments?.getInt(ARG_DIFFICULTY) ?: 2)
                .decision()

            mainHandler.post {
                val update = vm.update(action)
                audio.play(update)
                updateUI()
                when (update)
                {
                    Action.CHECKMATE -> {
                        vm.gameOver = true
                        callbacks?.onGameIsFinished("CHECKMATE")
                    }
                    Action.STALEMATE -> {
                        vm.gameOver = true
                        callbacks?.onGameIsFinished("STALEMATE")
                    }
                }
            }
            true
        } catch (e: Exception) {
            // should not throw an exception unless it's fed bad input (a terminal state) after the game has finished.
            vm.gameOver = true
            callbacks?.onGameIsFinished("END")
            true
        }
    })

    /**
     * Called after onCreateView().
     * If the next player to play is the computer, performs a computer move.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        if ((!vm.gameOver) && (vm.players.peek().type == Player.COMPUTER))
        {
            backgroundHandler.sendEmptyMessage(1)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Called when the fragment is no longer attached.
     * Quits the handlers thread loop.
     */
    override fun onDetach() {
        handlerThread.quitSafely()
        super.onDetach()
    }

    /**
     * Called by the super class after an acceptable inputEvent.
     * Updates the ViewModel, UI, and starts the process of having the
     * computer make a move on a background thread.
     * Callbacks to the hosting activity if the game is finished.
     */
    override fun finalizeMove(square: Square)
    {
        val update = vm.update(square)
        audio.play(update)
        updateUI()

        if (update == Action.CHECKMATE) {
            vm.gameOver = true
            callbacks?.onGameIsFinished("CHECKMATE")
            return
        }
        else if (update == Action.STALEMATE) {
            vm.gameOver = true
            callbacks?.onGameIsFinished("STALEMATE")
            return
        }

        if (!vm.gameOver) {
            backgroundHandler.sendEmptyMessage(1)
        }
    }

    companion object
    {
        /**
         * @return a new instance.
         */
        fun newInstance(difficulty: Int) : SinglePlayerController =
                SinglePlayerController().apply {
                    val args = bundleOf()
                    args.putInt(ARG_DIFFICULTY, difficulty)
                    arguments = args
                }
    }
}