package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.chess.R

/**
 * Class represents a square to be displayed on the UI.
 */
data class Square(val position: Position,
                  var background: Int,
                  var stroke: Int = 0,
                  var circle: Int = 0,
                  var chessPiece: ChessPiece? = null) : Parcelable
{
    private constructor(parcel: Parcel) : this(
            position = Position.CREATOR.createFromParcel(parcel),
            background = parcel.readInt(),
            stroke = parcel.readInt(),
            circle = parcel.readInt(),
            chessPiece = ChessPiece.CREATOR.createFromParcel(parcel)
    )

    /** the initial/default res value of the background */
    private val INIT_BACKGROUND_RES_ID: Int =
            if (isLight(background)) INIT_BACKGROUND_LIGHT_RES_ID else INIT_BACKGROUND_DARK_RES_ID

    /**
     * @return true if this Square does not have a model.ChessPiece object.
     */
    fun isEmpty(): Boolean = (chessPiece == null)

    /**
     * @return true if this Square is currently queued.
     */
    fun isQueued() : Boolean = (stroke == STROKE_QUEUED_RES_ID)

    /**
     * @return true if this Square currently has it's initial/default background.
     */
    fun hasDefaultBackground() : Boolean
    {
        return when (isLight(background))
        {
            true -> background == INIT_BACKGROUND_LIGHT_RES_ID
            else -> background == INIT_BACKGROUND_DARK_RES_ID
        }
    }

    /**
     * @return true if this Square currently has a circle.
     */
    fun hasCircle() : Boolean = (circle == CIRCLE_QUEUED_RES_ID)

    /**
     * Removes and returns this Squares ChessPiece object, if it has one,
     * otherwise returns null.
     */
    fun removeChessPiece(): ChessPiece?
    {
        val chessPiece = this.chessPiece
        this.chessPiece = null
        return chessPiece
    }

    /**
     * Puts this Square into the queued state.
     */
    fun queue()
    {
        stroke = STROKE_QUEUED_RES_ID
    }

    /**
     * Puts this Square into the dequeued state.
     */
    fun dequeue()
    {
        stroke = STROKE_DEFAULT_RES_ID
        circle = CIRCLE_DEFAULT_RES_ID
    }

    /**
     * Sets an alternate full, green stroke for this Square.
     */
    fun setGreenStroke()
    {
        stroke = R.drawable.shape_drawable_stroke_capturable
    }

    /**
     * Adds a circle to this Square.
     */
    fun addCircle()
    {
        circle = CIRCLE_QUEUED_RES_ID
    }

    /**
     * Sets the background of this Square to an alternate background.
     */
    fun setBackground()
    {
        background = if (isLight(INIT_BACKGROUND_RES_ID)) ALT_BACKGROUND_LIGHT_RES_ID else ALT_BACKGROUND_DARK_RES_ID
    }

    /**
     * Sets the background of this Square to its initial/default value.
     */
    fun resetBackground()
    {
        background = INIT_BACKGROUND_RES_ID
    }

    override fun equals(other: Any?): Boolean
    {
        if (other !is Square)
            return false

        val square: Square = other
        return square.position == this.position && square.background == this.background &&
                square.stroke == this.stroke && square.circle == this.circle &&
                square.chessPiece == this.chessPiece
    }

    override fun hashCode(): Int =
            12 + this.position.hashCode() + 2 + this.background.hashCode() +
                    this.stroke.hashCode() + 31 + this.circle.hashCode() + this.chessPiece.hashCode() + 5

    override fun toString(): String =
        "model.Square[position: $position, background: $background, stroke: $stroke, circle: $circle " +
                "chessPiece: $chessPiece]"

    override fun writeToParcel(dest: Parcel, flags: Int) {
        position.writeToParcel(dest, flags)
        dest.writeInt(background)
        dest.writeInt(stroke)
        dest.writeInt(circle)
        if (chessPiece != null) {
            chessPiece?.writeToParcel(dest, flags)
        } else {
            dest.writeInt(-1)
        }
    }

    override fun describeContents(): Int = 0

    companion object
    {
        /** static constants declaring the resource id's used by this class. */
        const val INIT_BACKGROUND_LIGHT_RES_ID: Int = R.drawable.shape_drawable_rectangle_light

        const val INIT_BACKGROUND_DARK_RES_ID: Int = R.drawable.shape_drawable_rectangle_dark

        const val ALT_BACKGROUND_LIGHT_RES_ID: Int = R.drawable.shape_drawable_rectangle_light_updated

        const val ALT_BACKGROUND_DARK_RES_ID: Int = R.drawable.shape_drawable_rectangle_dark_updated

        private const val CIRCLE_DEFAULT_RES_ID: Int = 0

        private const val CIRCLE_QUEUED_RES_ID: Int = R.drawable.shape_drawable_circle

        private const val STROKE_DEFAULT_RES_ID: Int = 0

        private const val STROKE_QUEUED_RES_ID: Int = R.drawable.shape_drawable_stroke_queued

        private fun isLight(res: Int): Boolean =
                (res == INIT_BACKGROUND_LIGHT_RES_ID) ||
                        (res == ALT_BACKGROUND_LIGHT_RES_ID)

        @JvmField
        val CREATOR = object : Parcelable.Creator<Square>
        {
            override fun createFromParcel(source: Parcel): Square = Square(source)

            override fun newArray(size: Int): Array<Square?> = arrayOfNulls(size)

        }
    }
}
