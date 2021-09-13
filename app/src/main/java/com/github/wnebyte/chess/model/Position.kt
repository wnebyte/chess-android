package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Class represents an [x, y] position/coordinate in 2D space.
 */
class Position(val x: Int, val y: Int) : Parcelable
{
    constructor(x: Char, y: Int) : this(charToInt[x.toUpperCase()] ?: error("$x is not within A..H"), y)

    constructor(position: Position, relX: Int, relY: Int)
            : this(position.x + relX, position.y + relY)

    private constructor(parcel: Parcel) : this(
            x = parcel.readInt(),
            y = parcel.readInt()
    )

    init
    {
        if (!withinBounds())
            throw IllegalStateException("x=${x} and/or y=${y} is out of bounds.")
    }

    fun xAsChar(): Char = charToInt.filter { p -> p.value == this.x }.keys.first()

    fun withinBounds(): Boolean = x in 1..8 && y in 1..8

    override fun toString(): String =
            "[x:${charToInt.filter { p -> p.value == this.x }.keys.first()}, y:${this.y}]"

    override fun equals(other: Any?): Boolean
    {
        if (other !is Position)
            return false

        val position: Position = other
        return position.x == this.x && position.y == this.y
    }

    override fun hashCode(): Int =
            32 + this.x.hashCode() + 2 + this.y.hashCode() + 7

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(x)
        dest.writeInt(y)
    }

    override fun describeContents(): Int = 0

    companion object
    {
        private val charToInt
                = mapOf(
                Pair('A', 1), Pair('B', 2),
                Pair('C', 3), Pair('D', 4),
                Pair('E', 5), Pair('F', 6),
                Pair('G', 7), Pair('H', 8)
        )

        fun of(position: String) : Position {
            val array = position.toCharArray()

            /*
            if ((array.size != 2) || (!charToInt.keys.contains(array[0].toUpperCase())) ||
                (!charToInt.values.contains(array[1].toInt())))
            {
                throw IllegalArgumentException(
                    "illegal position argument."
                )
            }

             */

            return Position(array[0], Integer.parseInt(array[1].toString()))
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Position>
        {
            override fun createFromParcel(source: Parcel): Position = Position(source)

            override fun newArray(size: Int): Array<Position?> = arrayOfNulls(size)
        }
    }
}