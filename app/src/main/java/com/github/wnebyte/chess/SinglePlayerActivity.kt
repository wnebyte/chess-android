package com.github.wnebyte.chess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.github.wnebyte.chess.controller.GameFinishedDialogFragment
import com.github.wnebyte.chess.controller.GridFragment
import com.github.wnebyte.chess.controller.SinglePlayerController
import com.github.wnebyte.chess.controller.GridViewModel
import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.model.GameIntentParam
import com.github.wnebyte.chess.struct.Queue

/** LogCat Tag */
private const val TAG = "SPGameActivity"

/** Activity parameter Tag*/
private const val EXTRA_GAME_INTENT_PARAM = "EXTRA_GAME_INTENT_PARAM"

/** Modal dialog Tag */
private const val DIALOG_END_OF_GAME = "DIALOG_END_OF_GAME"

class GameActivity : AppCompatActivity(),
        GridFragment.Callbacks, GameFinishedDialogFragment.Callbacks
{
    // Todo: set players when a new game is started
    private val vm: GridViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val gameIntentParam: GameIntentParam =
                intent.getParcelableExtra(EXTRA_GAME_INTENT_PARAM) ?:
                GameIntentParam.newSinglePlayerInstance()

        if (!vm.playersInit()) {
            vm.players = Queue(gameIntentParam.playerOne, gameIntentParam.playerTwo)
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null)
        {
            val fragment = SinglePlayerController
                    .newInstance(gameIntentParam.difficulty)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i(TAG, "Activity onSaveInstanceState()")
        vm.save()
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        Log.i(TAG, "Activity onDestroy()")
        super.onDestroy()
    }

    override fun onGameIsFinished(message: String) {
        GameFinishedDialogFragment.newInstance(message).apply {
            isCancelable = false
            show(supportFragmentManager, DIALOG_END_OF_GAME)
        }
    }

    override fun onPositiveButtonClick() {
        vm.reset()
        viewModelStore.clear()
        recreate()
    }

    override fun onNegativeButtonClick() {
        onBackPressed()
    }

    companion object
    {
        /**
         * @return a new instance.
         */
        fun newIntent(packageContext: Context, gameIntentParam: GameIntentParam) : Intent
        {
            return Intent(packageContext, GameActivity::class.java).apply {
                putExtra(EXTRA_GAME_INTENT_PARAM, gameIntentParam)
            }
        }
    }
}