package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.chess.R
import kotlinx.android.parcel.Parcelize
import java.lang.IllegalStateException

/**
 * @author Rasmus Emilsson
 */

/**
 * Class represents a Pawn in the game of Chess.
 */
class Pawn(color: Color) : ChessPiece(color)
{
    constructor(parcel: Parcel) : this(
            color = Color.CREATOR.createFromParcel(parcel)
    )

    override val resId: Int get() =
        if (color == Color.WHITE) R.drawable.ic_pawn_white else R.drawable.ic_pawn_black

    override val value: Double
        get() = VALUE

    override val classConstant: Int
        get() = CLASS_CONSTANT

    override fun moves(state: State) : List<Position>
    {
        val reachable: MutableList<Position> = mutableListOf()
        val pos = state.getPosition(this) ?: error(
                "$color pawn has null Position.class"
        )

        if (color == Color.WHITE)
        {
            try {
                val pos1 = Position(pos, 0, 1)

                if (state.isEmpty(pos1))
                {
                    reachable.add(pos1)
                }

                if (INIT_POS_WHITE.contains(pos))
                {
                    val pos2 = Position(pos, 0, 2)

                    if (state.isEmpty(pos1, pos2))
                    {
                        reachable.add(pos2)
                    }
                }
            } catch (e: IllegalStateException) {}

           try {
               val pos3 = Position(pos, 1, 1)

               if (state.hasEnemy(this, pos3))
               {
                   reachable.add(pos3)
               }
           } catch (e: IllegalStateException) {}

            try {
                val pos4 = Position(pos, -1, 1)

                if (state.hasEnemy(this, pos4))
                {
                    reachable.add(pos4)
                }
            } catch (e: IllegalStateException) {}
        }

        if (color == Color.BLACK)
        {
            try {
                val pos1 = Position(pos, 0, -1)

                if (state.isEmpty(pos1))
                {
                    reachable.add(pos1)
                }

                if (INIT_POS_BLACK.contains(pos))
                {
                    val pos2 = Position(pos, 0, -2)

                    if (state.isEmpty(pos1, pos2))
                    {
                        reachable.add(pos2)
                    }
                }
            } catch (e: IllegalStateException) {}

            try {
                val pos3 = Position(pos, 1, -1)

                if (state.hasEnemy(this, pos3))
                {
                    reachable.add(pos3)
                }
            } catch (e: IllegalStateException) {}

            try {
                val pos4 = Position(pos, -1, -1)

                if (state.hasEnemy(this, pos4))
                {
                    reachable.add(pos4)
                }
            } catch (e: IllegalStateException) {}

        }

        return reachable
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(classConstant)
        color.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int = 0

    fun getQueeningRanks() : List<Position> =
            if (color == Color.WHITE) QUEENING_RANK_WHITE else QUEENING_RANK_BLACK

    companion object
    {
        const val VALUE: Double = 1.0

        const val CLASS_CONSTANT: Int = 4

        private val INIT_POS_WHITE = listOf(
                Position('A', 2), Position('B', 2),
                Position('C', 2), Position('D', 2),
                Position('E', 2), Position('F', 2),
                Position('G', 2), Position('H', 2)
        )

        private val INIT_POS_BLACK = listOf(
                Position('A', 7), Position('B', 7),
                Position('C', 7), Position('D', 7),
                Position('E', 7), Position('F', 7),
                Position('G', 7), Position('H', 7)
        )

        private val QUEENING_RANK_WHITE = listOf(
                Position('A', 8), Position('B', 8),
                Position('C', 8), Position('D', 8),
                Position('E', 8), Position('F', 8),
                Position('G', 8), Position('H', 8)
        )

        private val QUEENING_RANK_BLACK = listOf(
                Position('A', 1), Position('B', 1),
                Position('C', 1), Position('D', 1),
                Position('E', 1), Position('F', 1),
                Position('G', 1), Position('H', 1)
        )

        @JvmField
        val CREATOR = object : Parcelable.Creator<Pawn>
        {
            override fun createFromParcel(source: Parcel): Pawn = Pawn(source)

            override fun newArray(size: Int): Array<Pawn?> = arrayOfNulls(size)

        }
    }
}