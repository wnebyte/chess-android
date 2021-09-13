package com.github.wnebyte.chess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.wnebyte.chess.model.*

/** LogCat Tag */
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity()
{
    private lateinit var singlePlayerButton: Button

    private lateinit var localMultiPlayerButton: Button

    private lateinit var onlineMultiPlayerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        singlePlayerButton = findViewById(R.id.single_player_button)
        localMultiPlayerButton = findViewById(R.id.local_multi_player_button)
        onlineMultiPlayerButton = findViewById(R.id.online_multi_player_button)

        // onClick starts a new SinglePlayerConfigActivity
        singlePlayerButton.setOnClickListener {
            val intent = SinglePlayerConfigActivity
                    .newIntent(this)
            startActivity(intent)
        }

        // onClick starts a new FreePlayActivity
        localMultiPlayerButton.setOnClickListener {
            val intent = MultiPlayerLocalGameActivity
                    .newIntent(this,
                            GameIntentParam.newFreePlayInstance())
            startActivity(intent)
        }

        // onClick starts a new MultiPlayerConfigActivity
        onlineMultiPlayerButton.setOnClickListener {
            val intent = MultiPlayerConfigActivity.newIntent(this)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    companion object
    {
        fun newIntent(packageContext: Context) : Intent =
                Intent(packageContext, MainActivity::class.java)
    }
}