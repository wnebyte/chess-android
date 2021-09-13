package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Class represents a clock to be used in the game of Chess.
 */
class Clock(var seconds: Int) : Parcelable
{
    private constructor(parcel: Parcel) : this(
            seconds = parcel.readInt()
    )

    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Clock>
        {
            override fun createFromParcel(parcel: Parcel): Clock = Clock(parcel)

            override fun newArray(size: Int): Array<Clock?> = arrayOfNulls(size)

        }
    }

    override fun toString(): String = "$seconds seconds"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(seconds)
    }

    override fun describeContents(): Int = 0
}