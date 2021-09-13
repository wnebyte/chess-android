package com.github.wnebyte.chess.model

enum class ActionResult
{
    CHECK_BLACK,
    CHECK_WHITE,
    CHECKMATE_BLACK,
    CHECKMATE_WHITE,
    MOVE_BLACK,
    MOVE_WHITE,
    CAPTURE_BLACK,
    CAPTURE_WHITE,
    STALEMATE
}