package com.github.wnebyte.chess

import android.util.Log
import com.github.wnebyte.chess.model.Action
import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.model.Position
import com.github.wnebyte.chess.model.State
import com.github.wnebyte.chess.util.StateGenerator
import org.junit.Assert
import org.junit.Test

private const val TAG = "StateTest"

class StateTest
{
    @Test
    fun testCheckBlack00()
    {
        val state = StateGenerator.ofCheck1(Color.BLACK)
        val check = state.check()
        val checkBlack = state.check(Color.BLACK)
        val checkWhite = state.check(Color.WHITE)
        val checkmate = state.checkmate()
        val checkmateBlack = state.checkmate(Color.BLACK)
        val checkmateWhite = state.checkmate(Color.WHITE)

        Assert.assertTrue(check)
        Assert.assertTrue(checkBlack)
        Assert.assertFalse(checkWhite)
        Assert.assertFalse(checkmate)
        Assert.assertFalse(checkmateBlack)
        Assert.assertFalse(checkmateWhite)
    }

    @Test
    fun testCheckWhite00()
    {
        val state = StateGenerator.ofCheck1(Color.WHITE)
        val check = state.check()
        val checkBlack = state.check(Color.BLACK)
        val checkWhite = state.check(Color.WHITE)
        val checkmate = state.checkmate()
        val checkmateBlack = state.checkmate(Color.BLACK)
        val checkmateWhite = state.checkmate(Color.WHITE)

        Assert.assertTrue(check)
        Assert.assertTrue(checkWhite)
        Assert.assertFalse(checkBlack)
        Assert.assertFalse(checkmate)
        Assert.assertFalse(checkmateBlack)
        Assert.assertFalse(checkmateWhite)
    }

    /*
    @Test
    fun testCheckmateBlack00()
    {
        val state = StateGenerator.ofCheckmate1(Color.BLACK)
        val check = state.check()
        val checkBlack = state.check(Color.BLACK)
        val checkWhite = state.check(Color.WHITE)
        val checkmate = state.checkmate()
        val checkmateBlack = state.checkmate(Color.BLACK)
        val checkmateWhite = state.checkmate(Color.WHITE)

        error("#legal moves: ${state.numberOfLegalMoves(Color.WHITE)}")

        /*
        Assert.assertTrue(checkmate)
        Assert.assertTrue(checkmateBlack)
        Assert.assertTrue(check)
        Assert.assertTrue(checkBlack)
        Assert.assertFalse(checkWhite)
        Assert.assertFalse(checkmateWhite)

         */
    }

     */

    @Test
    fun testCopy()
    {
        val state0 = StateGenerator.ofLonelyWhitePawn()
        val state1 = State.copy(state0).update(Action(Position('H', 8), Position('D', 2)))
        Log.d(TAG,state0.toString() + "\n" + state1.toString())
    }
}