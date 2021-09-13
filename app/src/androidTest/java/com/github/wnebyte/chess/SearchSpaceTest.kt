package com.github.wnebyte.chess

import android.util.Log
import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.model.Player
import com.github.wnebyte.chess.model.ai.SearchSpace
import com.github.wnebyte.chess.struct.Node
import com.github.wnebyte.chess.struct.Queue
import com.github.wnebyte.chess.util.StateGenerator
import org.junit.Test

private const val TAG = "SearchSpaceTest"

class SearchSpaceTest
{
    private val players = Queue(Player(Color.BLACK, Player.COMPUTER), Player(Color.WHITE, Player.HUMAN))

    @Test
    fun test00()
    {
        val root = StateGenerator.ofLonelyWhitePawn()
        val response = SearchSpace(Node(root), players.peek(), 2).decision()
        Log.d(TAG, response.toString())
    }

    @Test
    fun test01()
    {
        val root = StateGenerator.ofCheck1(Color.WHITE)
        val response = SearchSpace(Node(root), players.peek(), 2).decision()
        Log.d(TAG, response.toString())
    }

    @Test
    fun test02()
    {
        val root = StateGenerator.ofLonelyWhitePawn()
        val response = SearchSpace(Node(root), players.peek(), 2).decision()

    }
}