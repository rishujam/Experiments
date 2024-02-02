package com.example.experiments.snakeladder.model

/*
 * Created by Sudhanshu Kumar on 22/12/23.
 */

data class Player(
    val name: String,
    val position: Int,
    val nextPlayerAddress: Int
)