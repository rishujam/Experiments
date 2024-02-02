package com.example.experiments.snakeladder.action

/*
 * Created by Sudhanshu Kumar on 22/12/23.
 */

sealed class BoardAction {
    data class SnakeHead(val tail: Int) : BoardAction()
    data class LadderBottom(val top: Int) : BoardAction()
}