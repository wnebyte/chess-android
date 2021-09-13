package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Class is used to encapsulate data required to start a game activity.
 */
class GameIntentParam(var playerOne: Player, var playerTwo: Player,
                      val clock: Clock, val difficulty: Int = 0) : Parcelable
{
    private constructor(parcel: Parcel) : this(
            playerOne = Player.CREATOR.createFromParcel(parcel),
            playerTwo = Player.CREATOR.createFromParcel(parcel),
            clock = Clock.CREATOR.createFromParcel(parcel),
            difficulty = parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        playerOne.writeToParcel(dest, flags)
        playerTwo.writeToParcel(dest, flags)
        clock.writeToParcel(dest, flags)
        dest.writeInt(difficulty)
    }

    override fun describeContents(): Int = 0

    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<GameIntentParam>
        {
            override fun createFromParcel(source: Parcel): GameIntentParam =
                    GameIntentParam(source)

            override fun newArray(size: Int): Array<GameIntentParam?> = arrayOfNulls(size)

        }

        /**
         * Creates and returns a default "FreePlay" instance.
         */
        fun newFreePlayInstance() : GameIntentParam =
                GameIntentParam(
                        Player(Color.WHITE, Player.HUMAN),
                        Player(Color.BLACK, Player.HUMAN),
                        Clock(5 * 60)
                )

        /**
         * Creates and returns a default "SinglePlayer" instance,
         * where the player is playing the white pieces against the computer on normal difficulty.
         */
        fun newSinglePlayerInstance() : GameIntentParam =
                GameIntentParam(
                        Player(Color.WHITE, Player.HUMAN),
                        Player(Color.BLACK, Player.COMPUTER),
                        Clock(5 * 60), 2
                )
    }
}