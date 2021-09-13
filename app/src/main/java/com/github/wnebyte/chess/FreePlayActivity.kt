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
import com.github.wnebyte.chess.controller.FreePlayController
import com.github.wnebyte.chess.controller.GridViewModel
import com.github.wnebyte.chess.model.GameIntentParam
import com.github.wnebyte.chess.struct.Queue

/** LogCat Tag */
private const val TAG = "MPLocalGameActivity"

/** Activity parameter Tag*/
private const val EXTRA_GAME_INTENT_PARAM = "ExtraGameIntentParam"

/** Modal dialog Tag */
private const val DIALOG_END_OF_GAME = "DIALOG_END_OF_GAME"

class MultiPlayerLocalGameActivity : AppCompatActivity(),
        GridFragment.Callbacks, GameFinishedDialogFragment.Callbacks
{
    private val vm: GridViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Activity onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // retrieves any intentExtras
        val intentParam: GameIntentParam =
                intent.getParcelableExtra(EXTRA_GAME_INTENT_PARAM) ?:
                        GameIntentParam.newFreePlayInstance()

        if (!vm.playersInit()) {
            // if the viewmodel's player queue is not initialized
            vm.players = Queue(intentParam.playerOne, intentParam.playerTwo)
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null)
        {
            val fragment = FreePlayController
                    .newInstance()
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

    override fun onStop() {
        Log.i(TAG, "Activity onStop()")
        super.onStop()
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
        fun newIntent(packageContext: Context, gameIntentParam: GameIntentParam) : Intent =
                Intent(packageContext, MultiPlayerLocalGameActivity::class.java).apply {
                    putExtra(EXTRA_GAME_INTENT_PARAM, gameIntentParam)
                }
    }

}