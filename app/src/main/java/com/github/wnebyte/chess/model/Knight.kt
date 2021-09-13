package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.chess.R
import java.lang.IllegalStateException

/**
 * @author Rasmus Emilsson
 */

/**
 * Class represents a Knight in the game of Chess.
 */
class Knight(color: Color) : ChessPiece(color)
{
    constructor(parcel: Parcel) : this(
        color = Color.CREATOR.createFromParcel(parcel)
    )
    override val resId: Int get() =
        if (color == Color.WHITE) R.drawable.ic_knight_white else R.drawable.ic_knight_black

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
            val pos1 = Position(pos, 1, 2)

            if (state.isEmpty(pos1) || state.hasEnemy(this, pos1))
            {
                reachable.add(pos1)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos2 = Position(pos, 2, 1)

            if (state.isEmpty(pos2) || state.hasEnemy(this, pos2))
            {
                reachable.add(pos2)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos3 = Position(pos, 1, -2)

            if (state.isEmpty(pos3) || state.hasEnemy(this, pos3))
            {
                reachable.add(pos3)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos4 = Position(pos, 2, -1)

            if (state.isEmpty(pos4) || state.hasEnemy(this, pos4))
            {
                reachable.add(pos4)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos5 = Position(pos, -1, 2)

            if (state.isEmpty(pos5) || state.hasEnemy(this, pos5))
            {
                reachable.add(pos5)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos6 = Position(pos, -2, 1)

            if (state.isEmpty(pos6) || state.hasEnemy(this, pos6))
            {
                reachable.add(pos6)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos7 = Position(pos, -1, -2)

            if (state.isEmpty(pos7) || state.hasEnemy(this, pos7))
            {
                reachable.add(pos7)
            }

        } catch (e: IllegalStateException) {}

        try
        {
            val pos8 = Position(pos, -2, -1)

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

    companion object
    {
        private const val VALUE: Double = 3.0

        const val CLASS_CONSTANT: Int = 3

        @JvmField
        val CREATOR = object : Parcelable.Creator<Knight>
        {
            override fun createFromParcel(source: Parcel): Knight = Knight(source)

            override fun newArray(size: Int): Array<Knight?> = arrayOfNulls(size)
        }
    }
}