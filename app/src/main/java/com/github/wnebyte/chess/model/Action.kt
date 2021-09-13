package com.github.wnebyte.chess.model

/**
 * Class encapsulates two model.Position objects.
 */
class Action(val start: Position, val end: Position)
{
    override fun equals(other: Any?): Boolean
    {
        if (other !is Action)
            return false

        return other.start == this.start && other.end == this.end
    }

    override fun hashCode(): Int =
            32 + this.start.hashCode() + 12 + this.end.hashCode() + 5

    override fun toString(): String =
            "model.Action[start=${start},end=${end}]"

    companion object
    {
        const val MOVE: Int = 1

        const val CAPTURE: Int = 2

        const val CHECK: Int = 3

        const val CHECKMATE: Int = 4

        const val STALEMATE: Int = 5

        fun isCastle(action: Action) : Boolean =
                (King.CASTLE_ACTIONS_WHITE.contains(action)) ||
                        (King.CASTLE_ACTIONS_BLACK.contains(action))

        fun getRookResponse(action: Action) : Action =
                Rook.getCastleResponse(action)

        fun of(start: String, end: String) : Action =
            Action(Position.of(start), Position.of(end))
    }
}