package com.github.wnebyte.chess.util

import com.github.wnebyte.chess.model.Position
import com.github.wnebyte.chess.model.Square

class SquareFactory
{
    companion object
    {
        fun init(): MutableList<Square>
        {
            val squares = mutableListOf<Square>()
            var isWhite = true

            for (row in 8 downTo 1)
            {
                for (col in 1..8)
                {
                    val position = Position(col, row)
                    val square = Square(position,
                            if (isWhite) Square.INIT_BACKGROUND_LIGHT_RES_ID
                            else Square.INIT_BACKGROUND_DARK_RES_ID)
                    square.chessPiece = StateGenerator.init[position]
                    squares.add(square)
                    isWhite = !isWhite
                }
                isWhite = !isWhite
            }

            return squares
        }

        fun empty() : MutableList<Square>
        {
            val squares = mutableListOf<Square>()
            var isWhite = true

            for (row in 8 downTo 1)
            {
                for (col in 1..8)
                {
                    val position = Position(col, row)
                    val square = Square(position,
                            if (isWhite) Square.INIT_BACKGROUND_LIGHT_RES_ID
                            else Square.INIT_BACKGROUND_DARK_RES_ID)
                    squares.add(square)
                    isWhite = !isWhite
                }
                isWhite = !isWhite
            }

            return squares
        }
    }
}