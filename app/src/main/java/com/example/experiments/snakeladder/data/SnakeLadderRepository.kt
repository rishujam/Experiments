package com.example.experiments.snakeladder.data

import com.example.experiments.snakeladder.model.Ladder
import com.example.experiments.snakeladder.model.Snake

/*
 * Created by Sudhanshu Kumar on 22/12/23.
 */

class SnakeLadderRepository {

    private lateinit var snakes: List<Snake>
    private lateinit var ladders: List<Ladder>
    lateinit var playersIn: MutableList<String>

    fun takeInput() {
        snakes = listOf(
            Snake(20, 10),
            Snake(40, 7),
            Snake(50, 14),
            Snake(61, 21),
            Snake(100, 51),
            Snake(91, 55),
            Snake(88, 36)
        )
        ladders = listOf(
            Ladder(bottom = 9, top = 25),
            Ladder(bottom = 5 , top = 94),
            Ladder(bottom = 22, top = 53),
            Ladder(45, 66),
            Ladder(63, 94)
        )
        playersIn = mutableListOf(
            "Rishu",
            "Sudhanshu",
            "Prashant"
        )
    }

    fun getSnakes() = snakes

    fun getLadders() = ladders

}