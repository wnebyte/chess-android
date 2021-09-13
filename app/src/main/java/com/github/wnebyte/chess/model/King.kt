package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.chess.R
import java.lang.IllegalStateException

/**
 * @author Rasmus Emilsson
 */

/**
 * Class represents a King in the game of Chess.
 */
class King(color: Color) : ChessPiece(color)
{
    constructor(parcel: Parcel) : this(
        color = Color.CREATOR.createFromParcel(parcel)
    )

    override val resId: Int get() =
        if (color == Color.WHITE) R.drawable.ic_king_white else R.drawable.ic_king_black

    override val value: Double
        get() = VALUE

    override val classConstant: Int
        get() = CLASS_CONSTANT

    override fun moves(state: State): List<Position>
    {
        val reachable: MutableList<Position> = mutableListOf()
        val pos: Position = state.getPosition(this) ?: error("chessPiece has null Position")

        try
        {
            val pos1 = Position(pos, 1, 0)

            if (state.isEmpty(pos1) || state.hasEnemy(this, pos1))
            {
                reachable.add(pos1)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos2 = Position(pos, -1, 0)

            if (state.isEmpty(pos2) || state.hasEnemy(this, pos2))
            {
                reachable.add(pos2)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos3 = Position(pos, 0, 1)

            if (state.isEmpty(pos3) || state.hasEnemy(this, pos3))
            {
                reachable.add(pos3)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos4 = Position(pos, 0, -1)

            if (state.isEmpty(pos4) || state.hasEnemy(this, pos4))
            {
                reachable.add(pos4)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos5 = Position(pos, 1, 1)

            if (state.isEmpty(pos5) || state.hasEnemy(this, pos5))
            {
                reachable.add(pos5)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos6 = Position(pos, 1, -1)

            if (state.isEmpty(pos6) || state.hasEnemy(this, pos6))
            {
                reachable.add(pos6)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos7 = Position(pos, -1, 1)

            if (state.isEmpty(pos7) || state.hasEnemy(this, pos7))
            {
                reachable.add(pos7)
            }
        } catch (e: IllegalStateException) {}

        try
        {
            val pos8 = Position(pos, -1, -1)

            if (state.isEmpty(pos8) || state.hasEnemy(this, pos8))
            {
                reachable.add(pos8)
            }
        } catch (e: IllegalStateException) {}

        return reachable
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(classConstant)
        color.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int = 0

    fun getCastleActions() : List<Action> =
            if (color == Color.WHITE) CASTLE_ACTIONS_WHITE else CASTLE_ACTIONS_BLACK

    companion object
    {
        private const val VALUE: Double = 90.0

        const val CLASS_CONSTANT: Int = 2

        val INIT_POSITION_WHITE = Position('E', 1)

        val CASTLE_KS_POSITION_WHITE = Position('G', 1)

        val CASTLE_QS_POSITION_WHITE = Position('C', 1)

        val INIT_POSITION_BLACK = Position('E', 8)

        val CASTLE_KS_POSITION_BLACK = Position('G', 8)

        val CASTLE_QS_POSITION_BLACK = Position('C', 8)

        val CASTLE_KS_ACTION_WHITE = Action(INIT_POSITION_WHITE, CASTLE_KS_POSITION_WHITE)

        val CASTLE_QS_ACTION_WHITE = Action(INIT_POSITION_WHITE, CASTLE_QS_POSITION_WHITE)

        val CASTLE_KS_ACTION_BLACK = Action(INIT_POSITION_BLACK, CASTLE_KS_POSITION_BLACK)

        val CASTLE_QS_ACTION_BLACK = Action(INIT_POSITION_BLACK, CASTLE_QS_POSITION_BLACK)

        val CASTLE_ACTIONS_WHITE = listOf(
                Action(INIT_POSITION_WHITE, CASTLE_KS_POSITION_WHITE),
                Action(INIT_POSITION_WHITE, CASTLE_QS_POSITION_WHITE)
        )

        val CASTLE_ACTIONS_BLACK = listOf(
                Action(INIT_POSITION_BLACK, CASTLE_KS_POSITION_BLACK),
                Action(INIT_POSITION_BLACK, CASTLE_QS_POSITION_BLACK)
        )

        @JvmField
        val CREATOR = object : Parcelable.Creator<King>
        {
            override fun createFromParcel(source: Parcel): King = King(source)

            override fun newArray(size: Int): Array<King?> = arrayOfNulls(size)
        }
    }
}