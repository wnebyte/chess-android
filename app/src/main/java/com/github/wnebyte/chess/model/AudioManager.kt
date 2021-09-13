package com.github.wnebyte.chess.model

import android.content.Context
import android.media.MediaPlayer
import com.github.wnebyte.chess.R

/**
 * Class is used to play short chess related audio clips.
 */
class AudioManager(context: Context?)
{
    private val audio1 = MediaPlayer.create(context, R.raw.chess_move)

    private val audio2 = MediaPlayer.create(context, R.raw.chess_capture)

    private val audio3 = MediaPlayer.create(context, R.raw.chess_move)

    /**
     * Plays the audio clip associated with the specified integer.
     */
    fun play(action: Int)
    {
        when (action)
        {
            Action.MOVE -> audio1.start()
            Action.CAPTURE -> audio2.start()
            Action.CHECK -> audio3.start()
        }
    }

    /**
     * Releases any media resources associated with the class instance.
     */
    fun release()
    {
        audio1.release()
        audio2.release()
        audio3.release()
    }
}