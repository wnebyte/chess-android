package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import java.lang.IllegalArgumentException

/**
 * Class represents an autonomous player in the game of Chess.
 */
class Player(val color: Color, val type: Int) : Parcelable
{
    private constructor(parcel: Parcel) : this(
            color = Color.CREATOR.createFromParcel(parcel),
            type = parcel.readInt()
    )

    init {
        if (!validTypes.contains(type)) {
            throw IllegalArgumentException("type: $type is an invalid argument")
        }

    }

    override fun toString(): String =
            "model.Player[color: $color, type: ${if (type == HUMAN) "HUMAN" else "COMPUTER"}]"

    override fun equals(other: Any?): Boolean {
        if (other !is Player)
            return false

        return other.color == this.color && other.type == this.type
    }

    override fun hashCode(): Int =
            12 + this.color.hashCode() + 32 + this.type.hashCode() + 7

    companion object
    {
        /** acceptable constants for the field Player.type */
        const val HUMAN = 1
        const val COMPUTER = 2

        private val validTypes = listOf(HUMAN, COMPUTER)

        @JvmField
        val CREATOR = object : Parcelable.Creator<Player>
        {
            override fun createFromParcel(parcel: Parcel): Player = Player(parcel)

            override fun newArray(size: Int): Array<Player?> = arrayOfNulls<Player>(size)

        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        color.writeToParcel(parcel, flags)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int = 0

}