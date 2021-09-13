package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.chess.R
import java.lang.IllegalStateException

/**
 * @author Rasmus Emilsson
 */

/**
 * Class represents a Queen in the game of Chess.
 */
class Queen(color: Color) : ChessPiece(color)
{
    constructor(parcel: Parcel) : this(
        color = Color.CREATOR.createFromParcel(parcel)
    )

    override val resId: Int get() =
        if (color == Color.WHITE) R.drawable.ic_queen_white else R.drawable.ic_queen_black

    override val value: Double
        get() = VALUE

    override val classConstant: Int
        get() = CLASS_CONSTANT

    override fun moves(state: State): List<Position>
    {
        val reachable: MutableList<Position> = mutableListOf()
        val pos: Position = state.getPosition(this) ?: error("chessPiece has null Position")

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

        x = 1
        y = 1
        isEmpty = true
        try
        {
            while (x <= 8 && y <= 8 && isEmpty)
            {
                val nPos = Position(pos, x, y)
                isEmpty = state.isEmpty(nPos)

                if (isEmpty || state.hasEnemy(this, nPos))
                {
                    reachable.add(nPos)
                }
                else {
                    break
                }
                x++
                y++
            }
        } catch (e: IllegalStateException) {}

        x = 1
        y = -1
        isEmpty = true
        try
        {
            while (x <= 8 && -8 <= y && isEmpty)
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
                y--
            }
        } catch (e: IllegalStateException) {}

        x = -1
        y = 1
        isEmpty = true
        try
        {
            while (-8 <= x && y <= 8 && isEmpty)
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
                y++
            }
        } catch (e: IllegalStateException) {}

        x = -1
        y = -1
        isEmpty = true
        try
        {
            while (-8 <= x && -8 <= y && isEmpty)
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
        private const val VALUE: Double = 9.0

        const val CLASS_CONSTANT: Int = 5

        @JvmField
        val CREATOR = object : Parcelable.Creator<Queen>
        {
            override fun createFromParcel(source: Parcel): Queen = Queen(source)

            override fun newArray(size: Int): Array<Queen?> = arrayOfNulls(size)

        }
    }
}