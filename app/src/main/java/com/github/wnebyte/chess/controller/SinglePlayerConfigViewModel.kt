package com.github.wnebyte.chess.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SinglePlayerConfigViewModel(var state: SavedStateHandle) : ViewModel()
{
    var whiteCheckBoxIsChecked: Boolean = state["white_is_checked"] ?: true

    var normalCheckBoxIsChecked: Boolean = state["normal_is_checked"] ?: true
}