package com.github.wnebyte.chess.controller

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import java.lang.Exception
import java.lang.IllegalStateException

private const val ARG_MESSAGE = "ARG_MESSAGE"

/**
 * Class represents a dialog to be shown at the end of a game.
 */
class GameFinishedDialogFragment : DialogFragment()
{
    private var callbacks: Callbacks? = null

    interface Callbacks
    {
        /** callback function will be called when the user presses the dialog's positive button */
        fun onPositiveButtonClick()

        /** callback function will be called when the user presses the dialog's negative button */
        fun onNegativeButtonClick()
    }

    /**
     * Called when the fragment is first attached.
     * @throws IllegalStateException if the hosting activity does not implement the proper
     * interface.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callbacks = context as Callbacks
        }
        catch (e: Exception) {
            throw IllegalStateException(
                    "Activity needs to implement the GameFinishedDialogFragment.Callbacks interface"
            )
        }
    }

    /**
     * Called when the fragment is no longer attached.
     * Releases resources.
     */
    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

    /**
     * @return a dialog which displays a custom message.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val message: String = arguments?.getString(ARG_MESSAGE) ?: "End of Game"
            builder.setMessage(message)
                    /*
                    .setPositiveButton("REMATCH") { _, _ ->
                        callbacks?.onPositiveButtonClick()
                    }
                     */
                    .setNegativeButton("BACK") { _, _ ->
                        callbacks?.onNegativeButtonClick()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object
    {
        /**
         * @return a new instance.
         */
        fun newInstance(message: String) : GameFinishedDialogFragment =
                GameFinishedDialogFragment().apply {
                    val args = bundleOf()
                    args.putString(ARG_MESSAGE, message)
                    arguments = args
                }
    }
}