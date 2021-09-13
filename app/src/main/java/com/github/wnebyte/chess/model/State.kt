package com.github.wnebyte.chess.model

import java.lang.IllegalStateException
import java.lang.StringBuilder

/**
 * @author Rasmus Emilsson
 *
 * Class represents the state of the chessboard at a given time.
 */
class State(private val chessPieces: MutableMap<Position, ChessPiece>, var action: Action? = null)
{
    /** instance of an inner class used to directly access this class's Map of <Position, ChessPiece> pairs.*/
    private val board = BoardState()

    /** value used by the model.ai package */
    var utility: Double = Double.NaN

    /**
     * @return a nullable model.position object associated with the specified ChessPiece.
     */
    fun getPosition(chessPiece: ChessPiece): Position? = board.getPosition(chessPiece)

    /**
     * @return true if all of the specified model.position objects are empty,
     * i.e. void of an associated ChessPiece.
     */
    fun isEmpty(vararg positions: Position): Boolean
    {
        return positions.all { p -> board.isEmpty(p) }
    }

    /**
     * @return true if the specified ChessPiece has an enemy ChessPiece at the specified position.
     */
    fun hasEnemy(chessPiece: ChessPiece, position: Position): Boolean
    {
        return board.hasEnemy(chessPiece.color, position)
    }

    /**
     * @return true if the specified color has put the opposite color into a state of check.
     */
    fun check(color: Color): Boolean =
            board.check(color)

    /**
     * @return true if either Color.WHITE or Color.BLACK has put the opposite color into a state of check.
     */
    fun check() : Boolean =
            (board.check(Color.WHITE) || board.check(Color.BLACK))

    /**
     * @return true if the specified color has put the opposite color into a state of checkmate.
     */
    fun checkmate(color: Color) : Boolean =
            board.checkmate(color)

    /**
     * @return true if either Color.WHITE or Color.BLACK has put the opposite color into a state of checkmate.
     */
    fun checkmate() : Boolean =
            (board.checkmate(Color.WHITE) || board.checkmate(Color.BLACK))

    /**
     * @return true if the current state is a stalemate.
     */
    fun stalemate() : Boolean = (board.stalemate(Color.WHITE) || board.stalemate(Color.BLACK))

    fun stalemate(color: Color) : Boolean = board.stalemate(color)

    /**
     * Updates this State object by changing the model.position object of the specified ChessPiece
     * to the specified position object.
     */
    fun update(chessPiece: ChessPiece, end: Position) : State
    {
        val start: Position = getPosition(chessPiece) ?: error("chessPiece has no position")
        return update(Action(start, end))
    }

    /**
     * Removes any ChessPiece associated with the action.end position, and updates the model.position,
     * of the ChessPiece which is associated with the specified action.start position object,
     * to the specified action.end position.
     */
    fun update(action: Action) : State
    {
        board.update(action)
        val chessPiece = board.getChessPiece(action.end)

        if (chessPiece is Pawn)
        {
            if (chessPiece.getQueeningRanks().contains(action.end))
            {
                board.toQueen(action.end, chessPiece)
            }
        }
        else if (chessPiece is King)
        {
            if (chessPiece.getCastleActions().contains(action))
            {
                
            }
        }

        this.action = action
        return this
    }

    /**
     * Is used by the model.ai package.
     * @return the underlying Map of <Position, ChessPiece> pairs.
     */
    fun getChessPieces() : Map<Position, ChessPiece> =
            chessPieces

    override fun toString(): String
    {
        val builder = StringBuilder()

        builder.append("origin: ${action?.toString()} ")

        for (kv in board.getFriendly(Color.WHITE))
        {
            builder.append("[position: ${kv.key}, chessPiece: [${kv.value}]]\n")
        }
        for (kv in board.getFriendly(Color.BLACK))
        {
            builder.append("[position: ${kv.key}, chessPiece: [${kv.value}]]\n")
        }
        return builder.toString()
    }

    companion object
    {
        /**
         * @return a shallow copy of the specified state object.
         */
        fun copy(state: State): State
        {
            val map: MutableMap<Position, ChessPiece> = mutableMapOf()
            map.putAll(state.chessPieces)
            return State(map, state.action)
        }

    }

    /**
     * Inner class is used for direct access of the outer class's underlying Map of
     * <Position, ChessPiece> pairs.
     */
    private inner class BoardState
    {
        fun getPosition(chessPiece: ChessPiece): Position?
        {
            if (chessPieces.containsValue(chessPiece))
            {
                return chessPieces.filter { p -> p.value == chessPiece }.keys.first()
            }

            return null
        }

        fun getChessPiece(position: Position) = chessPieces[position]

        fun isEmpty(position: Position): Boolean
        {
            return !chessPieces.containsKey(position)
        }

        fun hasChessPiece(position: Position): Boolean
        {
            return chessPieces.containsKey(position)
        }

        fun hasEnemy(color: Color, position: Position): Boolean
        {
            return getOpponent(color).containsKey(position)
        }

        fun hasFriendly(color: Color, position: Position): Boolean
        {
            return getFriendly(color).containsKey(position)
        }

        fun isAttacked(color: Color, position: Position): Boolean
        {
            return getOpponent(color)
                    .filter { p -> p.value.moves(this@State).contains(position) }
                    .values.isNotEmpty()
        }

        fun check(color: Color): Boolean
        {
            val king: Position? = getOpponent(color).filter { kv -> kv.value is King }
                    .keys.first()

            return getFriendly(color).filter { p -> p.value.moves(this@State)
                    .contains(king)}.isNotEmpty()
        }

        fun checkmate(color: Color) : Boolean
        {
            return (check(color) &&
                    getOpponent(color).all{ kv -> kv.value.legalMoves(this@State).isEmpty() })
        }

        fun stalemate(color: Color) : Boolean
        {
            return (!check(color) && getOpponent(color)
                    .all { kv -> kv.value.legalMoves(this@State).isEmpty() })
        }

        fun update(action: Action)
        {
            if (!chessPieces.containsKey(action.start))
                throw IllegalStateException("${action.start} is empty")
            val chessPiece: ChessPiece = chessPieces.remove(action.start)!!
            chessPieces[action.end] = chessPiece
        }

        fun toQueen(position: Position, pawn: ChessPiece)
        {
            chessPieces[position] = Queen(pawn.color)
        }

        public fun getFriendly(color: Color) : Map<Position, ChessPiece> =
                chessPieces.filter { kv -> kv.value.color == color }

        public fun getOpponent(color: Color) : Map<Position, ChessPiece> =
                chessPieces.filterNot { kv -> kv.value.color == color }


    }
}