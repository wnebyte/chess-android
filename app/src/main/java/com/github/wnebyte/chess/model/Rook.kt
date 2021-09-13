package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.chess.R
import java.lang.IllegalStateException

/**
 * @author Rasmus Emilsson
 */

/**
 * Class represents a Rook in the game of Chess.
 */
class Rook(color: Color) : ChessPiece(color)
{
    constructor(parcel: Parcel) : this(
        color = Color.CREATOR.createFromParcel(parcel)
    )

    override val resId: Int get() =
        if (color == Color.WHITE) R.drawable.ic_rook_white else R.drawable.ic_rook_black

    override val value: Double
        get() = VALUE

    override val classConstant: Int
        get() = CLASS_CONSTANT

    override fun moves(state: State): List<Position>
    {
        val reachable: MutableList<Position> = mutableListOf()
        val pos: Position = state.getPosition(this) ?: error(
                "ChessPiece: $this has no associated Position within State: $state"
        )

        var x = 1
        var y = 0
        var isEmpty = true
        try
        {
            while (x <= 8 && isEmpty)
            {
                val nPos = Position(pos, x, y)
                isEmpty = state.isEmpty(nPos)

                if (isEmpty || state.hasEnemy(this, nPos))
                {
                    reachable.add(nPos)
                } else {
                    break
                }
                x++
            }
        } catch (e: IllegalStateException) {}

        x = -1
        y = 0
        isEmpty = true
        try
        {
            while (-8 <= x && isEmpty)
            {
                val nPos = Position(pos, x, y)
                isEmpty = state.isEmpty(nPos)

                if (isEmpty || state.hasEnemy(this, nPos))
                {
                    reachable.add(nPos)
                } else {
                    break
                }
                x--
            }
        } catch (e: IllegalStateException) {}

        x = 0
        y = 1
        isEmpty = true
        try
        {
            while (y <= 8 && isEmpty)
            {
                val nPos = Position(pos, x, y)
                isEmpty = state.isEmpty(nPos)

                if (isEmpty || state.hasEnemy(this, nPos))
                {
                    reachable.add(nPos)
                } else {
                    break
                }
                y++
            }
        } catch (e: IllegalStateException) {}

        x = 0
        y = -1
        isEmpty = true
        try
        {
            while (-8 <= y && isEmpty)
            {
                val nPos = Position(pos, x, y)
                isEmpty = state.isEmpty(nPos)

                if (isEmpty || state.hasEnemy(this, nPos))
                {
                    reachable.add(nPos)
                } else {
                    break
                }
                y--
            }
        } catch (e: IllegalStateException) {}

        return reachable
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(classConstant)
        color.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int = 0

    companion object
    {
        private const val VALUE: Double = 5.0

        const val CLASS_CONSTANT: Int = 6

        private val INIT_KS_POSITION_WHITE = Position('H', 1)

        private val INIT_QS_POSITION_WHITE = Position('A', 1)

        private val CASTLE_KS_POSITION_WHITE = Position('F', 1)

        private val CASTLE_QS_POSITION_WHITE = Position('D', 1)

        private val INIT_KS_POSITION_BLACK = Position('H', 8)

        private val INIT_QS_POSITION_BLACK = Position('A', 8)

        private val CASTLE_KS_POSITION_BLACK = Position('F', 8)

        private val CASTLE_QS_POSITION_BLACK = Position('D', 8)

        private val CASTLE_KS_ACTION_WHITE = Action(INIT_KS_POSITION_WHITE, CASTLE_KS_POSITION_WHITE)

        val CASTLE_QS_ACTION_WHITE = Action(INIT_QS_POSITION_WHITE, CASTLE_QS_POSITION_WHITE)

        val CASTLE_KS_ACTION_BLACK = Action(INIT_KS_POSITION_BLACK, CASTLE_KS_POSITION_BLACK)

        val CASTLE_QS_ACTION_BLACK = Action(INIT_QS_POSITION_BLACK, CASTLE_QS_POSITION_BLACK)

        private val CASTLE_ACTIONS_WHITE = listOf(
                CASTLE_KS_ACTION_WHITE,
                Action(INIT_QS_POSITION_WHITE, CASTLE_QS_POSITION_WHITE)
        )

        private val CASTLE_ACTIONS_BLACK = listOf(
                Action(INIT_KS_POSITION_BLACK, CASTLE_KS_POSITION_BLACK),
                Action(INIT_QS_POSITION_BLACK, CASTLE_QS_POSITION_BLACK)
        )

        private val CASTLE_RESPONSE: Map<Action, Action> = mapOf(
                Pair(King.CASTLE_KS_ACTION_WHITE, CASTLE_KS_ACTION_WHITE),
                Pair(King.CASTLE_QS_ACTION_WHITE, CASTLE_QS_ACTION_WHITE),
                Pair(King.CASTLE_KS_ACTION_BLACK, CASTLE_KS_ACTION_BLACK),
                Pair(King.CASTLE_QS_ACTION_BLACK, CASTLE_QS_ACTION_BLACK)
        )

        fun getCastleActions(color: Color) : List<Action> =
                if (color == Color.WHITE) CASTLE_ACTIONS_WHITE else CASTLE_ACTIONS_BLACK

        fun getCastleResponse(action: Action) : Action =
                CASTLE_RESPONSE[action] ?: error(
                        "$action is not a castle action."
                )

        @JvmField
        val CREATOR = object : Parcelable.Creator<Rook>
        {
            override fun createFromParcel(source: Parcel): Rook = Rook(source)

            override fun newArray(size: Int): Array<Rook?> = arrayOfNulls(size)
        }

    }
}