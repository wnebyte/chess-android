package com.github.wnebyte.chess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.github.wnebyte.chess.controller.GridViewModel
import com.github.wnebyte.chess.controller.SinglePlayerConfigViewModel
import com.github.wnebyte.chess.model.Clock
import com.github.wnebyte.chess.model.Color
import com.github.wnebyte.chess.model.GameIntentParam
import com.github.wnebyte.chess.model.Player

class SinglePlayerConfigActivity : AppCompatActivity()
{
    private val vm: SinglePlayerConfigViewModel by viewModels()

    private lateinit var whiteCheckBox: CheckBox

    private lateinit var blackCheckBox: CheckBox

    private lateinit var normalCheckBox: CheckBox

    private lateinit var hardCheckBox: CheckBox

    private lateinit var clockSpinner: Spinner

    private lateinit var timeControlSpinner: Spinner

    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_player_config)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        whiteCheckBox = findViewById(R.id.whiteCheckBox)
        blackCheckBox = findViewById(R.id.blackCheckBox)
        normalCheckBox = findViewById(R.id.normalCheckBox)
        hardCheckBox = findViewById(R.id.hardCheckBox)
        clockSpinner = findViewById(R.id.clockSpinner)
        timeControlSpinner = findViewById(R.id.timeControlSpinner)
        startButton = findViewById(R.id.startButton)

        whiteCheckBox.setOnCheckedChangeListener(onCheckedChangeListener(blackCheckBox))
        blackCheckBox.setOnCheckedChangeListener(onCheckedChangeListener(whiteCheckBox))
        normalCheckBox.setOnCheckedChangeListener(onCheckedChangeListener(hardCheckBox))
        hardCheckBox.setOnCheckedChangeListener(onCheckedChangeListener(normalCheckBox))
        clockSpinner.isEnabled = false
        timeControlSpinner.isEnabled = false
        startButton.setOnClickListener {
            startGameActivity()
        }

        whiteCheckBox.isChecked = vm.whiteCheckBoxIsChecked
        blackCheckBox.isChecked = !vm.whiteCheckBoxIsChecked
        normalCheckBox.isChecked = vm.normalCheckBoxIsChecked
        hardCheckBox.isChecked = !vm.normalCheckBoxIsChecked
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

    private fun onCheckedChangeListener(vararg checkBoxes: CheckBox) : CompoundButton.OnCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkBoxes.forEach {
                        it.isChecked = false
                    }
                }
                vm.whiteCheckBoxIsChecked = whiteCheckBox.isChecked
                vm.normalCheckBoxIsChecked = normalCheckBox.isChecked
            }

    fun startGameActivity()
    {
        val playerColor: Color = if (whiteCheckBox.isChecked) Color.WHITE else Color.BLACK
        val player = Player(playerColor, Player.HUMAN)
        val computerColor = Color.invert(playerColor)
        val computer = Player(computerColor, Player.COMPUTER)
        val players: Array<Player> = arrayOf(player, computer)

        val difficulty: Int = when {
            normalCheckBox.isChecked -> 1
            else -> 2
        }
        val intent = GameActivity
                .newIntent(this,
                        GameIntentParam(
                                players.first { p -> p.color == Color.WHITE },
                                players.first { p -> p.color == Color.BLACK},
                                Clock(5 * 60), difficulty
                        )
                )
        startActivity(intent)
    }

    companion object
    {
        fun newIntent(packageContext: Context) : Intent
        {
            return Intent(packageContext, SinglePlayerConfigActivity::class.java)
        }
    }
}