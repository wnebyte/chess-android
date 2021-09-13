package com.github.wnebyte.chess.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author Rasmus Emilsson
 */

/**
 * Class represents a ChessPiece in the game of Chess.
 */
abstract class ChessPiece(val color: Color) : Parcelable
{
    /** unique identifier */
    private val uuid: UUID = UUID.randomUUID()

    /** specifies an svg/png resources to be used for depiction  */
    abstract val resId: Int

    /** a value used by classes of the model.ai package */
    abstract val value: Double

    /** value used for serialization */
    abstract val classConstant: Int

    /**
     * Determines which model.position objects this ChessPiece can reach given the specified
     * model.State object.
     */
    abstract fun moves(state: State): List<Position>

    /**
     * Determines which model.position objects this ChessPiece can legally reach given the
     * specified model.State object.
     */
    fun legalMoves(state: State): List<Position> =
            moves(state).filterNot {  pos -> State.copy(state)
                    .update(this, pos).check(Color.invert(color)) }

    override fun equals(other: Any?): Boolean
    {
        if (other !is ChessPiece)
            return false

        val chessPiece: ChessPiece = other
        return chessPiece.color == this.color && chessPiece.resId == this.resId &&
                chessPiece.uuid == this.uuid
    }

    override fun hashCode(): Int =
        this.color.hashCode() + 55 + this.resId.hashCode() + 7 + this.uuid.hashCode() + 2

    override fun toString(): String =
            "[uuid: ${uuid}, class: ${javaClass.simpleName}, color: ${color}]"

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(classConstant)
        color.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int = 0


    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ChessPiece>
        {
            override fun createFromParcel(source: Parcel): ChessPiece? = getConcreteClass(source)

            override fun newArray(size: Int): Array<ChessPiece?> = arrayOfNulls(size)

        }

        /**
         * Used for serialization.
         */
        fun getConcreteClass(source: Parcel) : ChessPiece?
        {
            return when(source.readInt())
            {
                Bishop.CLASS_CONSTANT -> Bishop(source)
                King.CLASS_CONSTANT -> King(source)
                Knight.CLASS_CONSTANT -> Knight(source)
                Pawn.CLASS_CONSTANT -> Pawn(source)
                Queen.CLASS_CONSTANT -> Queen(source)
                Rook.CLASS_CONSTANT -> Rook(source)
                else -> null
            }
        }
    }

}