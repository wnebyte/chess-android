package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Class represents the color of a ChessPiece.
 */
enum class Color : Parcelable
{
    WHITE,
    BLACK;

    companion object
    {
        fun toColor(string: String): Color?
        {
            return when(string.toUpperCase(Locale.ENGLISH))
            {
                "WHITE" -> WHITE
                "BLACK" -> BLACK
                else -> null
            }
        }

        fun invert(color: Color): Color
        {
            return when (color)
            {
                WHITE -> BLACK
                BLACK -> WHITE
            }
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Color> {

            override fun createFromParcel(parcel: Parcel): Color = values()[parcel.readInt()]

            override fun newArray(size: Int): Array<Color?> = arrayOfNulls(size)
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeInt(this.ordinal)
    }

    override fun describeContents(): Int = 0

}