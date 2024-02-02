package com.example.experiments.snakeladder.model

import com.example.experiments.snakeladder.action.BoardAction

/*
 * Created by Sudhanshu Kumar on 21/12/23.
 */

data class BoardCell(
    val number: Int,
    val action: BoardAction? = null
)