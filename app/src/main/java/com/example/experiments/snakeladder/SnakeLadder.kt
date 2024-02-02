package com.example.experiments.snakeladder

import com.example.experiments.snakeladder.action.BoardAction
import com.example.experiments.snakeladder.data.SnakeLadderRepository
import com.example.experiments.snakeladder.model.BoardCell
import com.example.experiments.snakeladder.model.Ladder
import com.example.experiments.snakeladder.model.Player
import com.example.experiments.snakeladder.model.Snake
import com.example.experiments.snakeladder.util.Constants

/*
 * Created by Sudhanshu Kumar on 21/12/23.
 */

//TODO For now assuming there is only one thing in a cell out of 4 (head, tail, top and bottom)

class SnakeLadder {

    private lateinit var repository: SnakeLadderRepository

    fun main() {
        repository = SnakeLadderRepository()
        repository.takeInput()
        initGameData()
    }

    //Business Data
    private lateinit var players : MutableMap<String, Player>
    private val board = mutableMapOf<Int, BoardCell>()
    private var currentPlayer = players.values.toList().getOrNull(0)
    private val winners = mutableListOf<Player>()

    private fun initGameData() {
        //Setup Board
        for(i in (1..100)) {
            board[i] = BoardCell(i, null)
        }
        val snakes = repository.getSnakes()
        for(i in snakes) {
            board.getOrDefault(i.head, null)?.action?.let {
                throw Exception("Cannot have multiple actions")
            } ?: run {
                board[i.head] = BoardCell(i.head, BoardAction.SnakeHead(i.tail))
            }
        }
        val ladders = repository.getLadders()
        for(i in ladders) {
            board.getOrDefault(i.bottom, null)?.action?.let {
                throw Exception("Cannot have multiple actions")
            } ?: run {
                board[i.bottom] = BoardCell(i.bottom, BoardAction.LadderBottom(i.top))
            }
        }

        //Setup Players
        updateNextPlayerAddress()
    }

    private fun play() {
        currentPlayer?.let {
            rollDice(it)
            currentPlayer = players.values.toList().getOrNull(it.nextPlayerAddress)
        } ?: {
            throw Exception("No Player found")
        }
    }

    private fun rollDice(player: Player) {
        val numberOnDice = (Constants.DICE_MIN..Constants.DICE_MAX).random()
        updatePlayerPositionAfterDiceRoll(numberOnDice, player)
    }

    private fun updatePlayerPositionAfterDiceRoll(numberOnDice: Int, player: Player) {
        val currentPosition = player.position
        var newPosition = currentPosition + numberOnDice
        val cell = board.getOrDefault(newPosition, null)
        cell?.action?.let {
            newPosition = when(it) {
                is BoardAction.SnakeHead -> {
                    it.tail
                }

                is BoardAction.LadderBottom -> {
                    it.top
                }
            }
            players[player.name] = player.copy(
                position = newPosition
            )
            if(newPosition > 100) {
                winners.add(player)
                repository.playersIn.remove(player.name)
                if(repository.playersIn.size < 2) {
                    //TODO Send event for game ended
                }
                updateNextPlayerAddress()
            }
        }
    }

    private fun updateNextPlayerAddress() {
        for(i in repository.playersIn.indices) {
            val key = repository.playersIn[i]
            val nextPlayerAddress = if(i + 1 > repository.playersIn.size - 1) {
                0
            } else {
                i + 1
            }
            players.getOrDefault(key, null)?.let {
                players[key] = it.copy(
                    nextPlayerAddress = nextPlayerAddress
                )
            } ?: run {
                key to Player(key, 0, nextPlayerAddress)
            }
        }
    }

}