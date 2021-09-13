package com.github.wnebyte.chess.util

import com.github.wnebyte.chess.model.*

class StateGenerator
{
    companion object
    {
        fun ofLonelyWhitePawn() : State
        {
            val map = mutableMapOf(
                    Pair(Position('H', 1), King(Color.WHITE)),
                    Pair(Position('D', 2), Pawn(Color.WHITE)),
                    Pair(Position('H', 8), King(Color.BLACK))
            )

            return State(map)
        }

        fun ofCheckmate1(color: Color) : State
        {
            val map = mutableMapOf(
                    Pair(Position('H', 1), King(color)),
                    Pair(Position('A', 1), Rook(color)),
                    Pair(Position('C', 1), Rook(color)),
                    Pair(Position('D', 3), Queen(color)),
                    Pair(Position('B', 8), King(Color.invert(color)))
            )

            return State(map)
        }

        fun ofCheckmate2(color: Color) : State
        {
            val map = mutableMapOf(
                    Pair(Position('C', 1), King(Color.invert(color))),
                    Pair(Position('H', 1), Rook(color)),
                    Pair(Position('E', 2), Rook(color)),
                    Pair(Position('B', 8), Queen(color)),
                    Pair(Position('D', 8), King(color)),
                    Pair(Position('A', 3), Bishop(color)),
                    Pair(Position('D', 1), Bishop(color))
            )

            return State(map)
        }

        fun ofCheck1(color: Color) : State
        {
            val map = mutableMapOf(
                    Pair(Position('C', 1), King(Color.invert(color))),
                    Pair(Position('H', 1), Rook(color)),
                    Pair(Position('B', 8), Queen(color)),
                    Pair(Position('D', 8), King(color)),
                    Pair(Position('A', 3), Bishop(color)),
                    Pair(Position('D', 1), Bishop(color)),
                    Pair(Position('H', 8), Rook(color))
            )

            return State(map)
        }

        val init = mutableMapOf(
                Pair(Position('A', 1), Rook(Color.WHITE)),
                Pair(Position('A', 2), Pawn(Color.WHITE)),
                Pair(Position('A', 8), Rook(Color.BLACK)),
                Pair(Position('A', 7), Pawn(Color.BLACK)),
                Pair(Position('B', 1), Knight(Color.WHITE)),
                Pair(Position('B', 2), Pawn(Color.WHITE)),
                Pair(Position('B', 8), Knight(Color.BLACK)),
                Pair(Position('B', 7), Pawn(Color.BLACK)),
                Pair(Position('C', 1), Bishop(Color.WHITE)),
                Pair(Position('C', 2), Pawn(Color.WHITE)),
                Pair(Position('C', 8), Bishop(Color.BLACK)),
                Pair(Position('C', 7), Pawn(Color.BLACK)),
                Pair(Position('D', 1), Queen(Color.WHITE)),
                Pair(Position('D', 2), Pawn(Color.WHITE)),
                Pair(Position('D', 8), Queen(Color.BLACK)),
                Pair(Position('D', 7), Pawn(Color.BLACK)),
                Pair(Position('E', 1), King(Color.WHITE)),
                Pair(Position('E', 2), Pawn(Color.WHITE)),
                Pair(Position('E', 8), King(Color.BLACK)),
                Pair(Position('E', 7), Pawn(Color.BLACK)),
                Pair(Position('F', 1), Bishop(Color.WHITE)),
                Pair(Position('F', 2), Pawn(Color.WHITE)),
                Pair(Position('F', 8), Bishop(Color.BLACK)),
                Pair(Position('F', 7), Pawn(Color.BLACK)),
                Pair(Position('G', 1), Knight(Color.WHITE)),
                Pair(Position('G', 2), Pawn(Color.WHITE)),
                Pair(Position('G', 8), Knight(Color.BLACK)),
                Pair(Position('G', 7), Pawn(Color.BLACK)),
                Pair(Position('H', 1), Rook(Color.WHITE)),
                Pair(Position('H', 2), Pawn(Color.WHITE)),
                Pair(Position('H', 8), Rook(Color.BLACK)),
                Pair(Position('H', 7), Pawn(Color.BLACK))
        )

        fun apply(squares: MutableList<Square>, state: State) : MutableList<Square>
        {
            squares.forEach {
                it.removeChessPiece()
            }

            state.getChessPieces().forEach{
                squares.first{sq -> sq.position == it.key}.chessPiece = it.value
            }

            return squares
        }
    }
}