package com.github.wnebyte.chess.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.wnebyte.chess.model.*
import com.github.wnebyte.chess.struct.Queue
import com.github.wnebyte.chess.util.SquareFactory
import com.github.wnebyte.chess.util.StateGenerator

private const val TAG = "GridViewModel"

class GridViewModel(private val state: SavedStateHandle) : ViewModel()
{
    val squares: MutableList<Square> = state["squares"] ?: SquareFactory.init()

    var reverseLayout: Boolean = state["reverseLayout"] ?: false

    var queued: Square? = squares.firstOrNull{sq -> sq.isQueued()}

    val legalSquares: MutableList<Square> = squares.filter{sq -> sq.hasCircle()}.toMutableList()

    val updatedSquares: Array<Square?> = arrayOf(
            squares.firstOrNull { sq -> !sq.hasDefaultBackground() },
            squares.lastOrNull { sq -> !sq.hasDefaultBackground() }
    )

    var gameOver: Boolean = state["gameOver"] ?: false

    lateinit var players: Queue<Player>

    init {
        val playerOne: Player? = state["player_1"]
        val playerTwo: Player? = state["player_2"]

        if (playerOne != null && playerTwo != null)
        {
            players = Queue(playerOne, playerTwo)
        }
    }

    fun playersInit() : Boolean =
            this::players.isInitialized

    /**
     * Resets the contents of the ViewModel.
     */
    fun reset()
    {
        state["squares"] = null
        state["reverseLayout"] = null
        state["player_1"] = null
        state["player_2"] = null
        state["gameOver"] = null
    }

    /**
     * Saves the content of the ViewModel to it's SavedStateHandle object.
     */
    fun save()
    {
        state["squares"]  = squares
        state["reverseLayout"] = reverseLayout
        state["player_1"] = players.peek()
        state["player_2"] = players.last()
        state["gameOver"] = gameOver
    }

    /**
     * @return true if a Square is currently queued.
     */
    fun isQueued(): Boolean = queued != null

    /**
     * Queues the specified Square.
     */
    fun queue(square: Square)
    {
        if (isQueued() && square == queued) {
            dequeue()
        } else {
            dequeue()
            square.queue()
            queued = square
            queueLegal()
        }
    }

    /**
     * Dequeues the currently queued Square.
     */
    fun dequeue()
    {
        queued?.dequeue()
        dequeueLegal()
        queued = null
    }

    /**
     * Queues all of the Squares which the currently queued Square's ChessPiece can reach as legal.
     */
    private fun queueLegal()
    {
        queued?.chessPiece?.legalMoves(toState())?.forEach {
            val square = squares.first { sq -> sq.position == it }
            square.addCircle()
            if (!square.isEmpty() && square.chessPiece?.color != players.peek().color) {
                square.setGreenStroke()
            }
            legalSquares.add(square)
        }
    }

    /**
     * Dequeues all of the currently queued legal squares.
     */
    private fun dequeueLegal()
    {
        legalSquares.forEach { it.dequeue() }
        legalSquares.clear()
    }

    /**
     * @return true if the specified Square is a friendly Square from the perspective of the
     * next player to make a move.
     */
    fun isFriendlySquare(square: Square) : Boolean =
            players.peek().type == Player.HUMAN && square.chessPiece?.color == players.peek().color

    /**
     * @return true if the specified square can be legally reached by the queued Square's ChessPiece.
     */
    fun isLegalSquare(square: Square): Boolean =
            players.peek().type == Player.HUMAN && isQueued() && legalSquares.contains(square)

    /**
     * Moves the currently queued Square's ChessPiece to the specified Square and updates the
     * player queue. Also dequeues any queued squares.
     * @return an int describing the nature of the update.
     */
    fun update(square: Square) : Int
    {
        if (isQueued() && legalSquares.contains(square))
        {
            val capture = !square.isEmpty()
            updatedSquares.forEach { it?.resetBackground() }
            square.chessPiece = queued?.removeChessPiece()
            updatedSquares[0] = queued
            updatedSquares[1] = square
            updatedSquares.forEach { it?.setBackground() }
            dequeue()

            val update = describeUpdate(toState(), capture)
            players.update()
            return update
        }

        return -1
    }

    /**
     * Moves the ChessPiece associated with the specified action.start position to the Square
     * associated with the specified action.end position and updates the player queue.
     * Also dequeues any queued squares.
     * @return an int describing the nature of the update.
     */
    fun update(action: Action) : Int
    {
        val startSquare = squares.first { sq -> sq.position == action.start }
        val endSquare   = squares.first { sq -> sq.position == action.end }
        val chessPiece = startSquare.removeChessPiece()
        val capture = !endSquare.isEmpty()

        endSquare.chessPiece = chessPiece
        updatedSquares.forEach { it?.resetBackground() }
        updatedSquares[0] = startSquare
        updatedSquares[1] = endSquare
        updatedSquares.forEach { it?.setBackground() }

        dequeue()
        val update =  describeUpdate(toState(), capture)
        players.update()
        return update
    }

    /**
     * @return a model.State representation of this viewmodel's underlying list of Squares.
     */
    fun toState(): State
    {
        val map: MutableMap<Position, ChessPiece> = mutableMapOf()

        squares.forEach {
            val chessPiece: ChessPiece? = it.chessPiece

            if (chessPiece != null)
            {
                map[it.position] = chessPiece
            }
        }

        return State(map)
    }

    /**
     * @return an int describing the nature of a model.State object.
     */
    private fun describeUpdate(state: State, capture: Boolean) : Int
    {
        return when {
            state.checkmate() -> {
                Action.CHECKMATE
            }
            state.stalemate(players.peek().color) -> {
                Action.STALEMATE
            }
            state.check(players.peek().color) -> {
                Action.CHECK
            }
            capture -> {
                Action.CAPTURE
            }
            else -> Action.MOVE
        }
    }

}