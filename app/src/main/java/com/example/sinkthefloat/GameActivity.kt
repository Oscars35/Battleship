package com.example.sinkthefloat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sinkthefloat.databinding.ActivityGameBinding
import java.util.*
import kotlin.system.exitProcess

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var positions: Int = 0
    private lateinit var playerName: String
    private lateinit var difficulty: String
    private lateinit var playerOneBoard: IntArray
    private lateinit var playerOneAdapter: GameAdapter
    private lateinit var iaAdapter: GameAdapter
    private lateinit var realIaBoard: IntArray
    private var alreadyClicked: Boolean = false

    private var iaCellsWithBoats = 0
    private lateinit var userCellsWithBoats: IntArray
    private var userHitBoats = 0

    private val getUserBoard = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            playerOneBoard = result.data?.getIntArrayExtra("gridAdapter")!!
            userCellsWithBoats = result.data?.getIntArrayExtra("userCellsWithBoats")!!
            playerOneAdapter = GameAdapter(this, playerOneBoard, binding.boardGridView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentInfo()
        setUpIaBoard()
        askForBoatsToUser()

        binding.iaBoardGridView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            selectedGridStuff(position)
        }
    }

    private fun selectedGridStuff(position: Int) {
        if(alreadyClickedPosition(position)) {
            Toast.makeText(this, R.string.position_already_guessed, Toast.LENGTH_SHORT).show()
        }
        else if (!alreadyClicked) {
            checkAndChangeView(position)
        }
    }

    private fun alreadyClickedPosition(position: Int): Boolean {
        return iaAdapter.getItem(position) == R.drawable.failedhit
                || iaAdapter.getItem(position) == R.drawable.destroyedboat
    }

    private fun checkAndChangeView(position: Int) {
        alreadyClicked = true
        checkPosition(position)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            changeView(View.GONE, View.VISIBLE)
            iaPredictPosition()
            changeViewWithDelay()
            alreadyClicked = false
        }, 1000L)
    }

    private fun changeViewWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            changeView(View.VISIBLE, View.GONE)
        }, 2000L)
    }

    private fun iaPredictPosition() {
        when(difficulty) {
            "Easy" -> predictPosition(6)
            "Medium" -> predictPosition(4)
            "Hard" -> predictPosition(2)
        }
    }

    private fun predictPosition(probability: Int) {
        if((1..probability).random() == 1)
            hitBoat()
        else
            noHitBoat()
    }

    private fun noHitBoat() {
        var predicted = (0 until positions).random() * positions + (0..9).random() // ROW + COLUMN
        while(!noBoatInThisPosition(predicted)) {
            predicted = (0 until positions).random() * positions + (0..9).random()
        }
        changeImageWithPredicted(predicted)
    }

    private fun changeImageWithPredicted(predicted: Int) {
        playerOneAdapter.setImage(predicted, R.drawable.failedhit)
        binding.boardGridView.adapter = playerOneAdapter
    }

    private fun noBoatInThisPosition(predicted: Int): Boolean {
        return playerOneBoard[predicted] != R.drawable.appboat
                && playerOneBoard[predicted] != R.drawable.boat2
                && playerOneBoard[predicted] != R.drawable.boat3
    }

    private fun hitBoat() {
        playerOneAdapter.setImage(userCellsWithBoats[userHitBoats], R.drawable.destroyedboat)
        binding.boardGridView.adapter = playerOneAdapter
        userHitBoats += 1
        checkForIaWinner()
    }

    private fun checkForIaWinner() {
        if(userHitBoats == userCellsWithBoats.size)
            goWinnerScreen("IA")
    }

    private fun changeView(view1: Int, view2: Int) {
        binding.iaBoardGridView.visibility = view1
        binding.boardGridView.visibility = view2
        binding.boardGridView.adapter = playerOneAdapter
    }

    private fun checkPosition(position: Int) {
        if(boatInPositionSelected(position)) {
            iaAdapter.setImage(position, R.drawable.destroyedboat)
            binding.iaBoardGridView.adapter = iaAdapter
            iaCellsWithBoats -= 1
            checkForUserWinner()
        }
        else {
            iaAdapter.setImage(position, R.drawable.failedhit)
            binding.iaBoardGridView.adapter = iaAdapter
        }
    }

    private fun checkForUserWinner() {
        if(iaCellsWithBoats == 0) {
            goWinnerScreen(playerName)
        }
    }

    private fun goWinnerScreen(winnerName: String) {
        val intent = Intent(this, WinnerActivity::class.java)
        intent.putExtra("winner", winnerName)
        setResult(Activity.RESULT_OK, intent)
        startActivity(intent)
        finish()
    }

    private fun boatInPositionSelected(position: Int): Boolean {
        return realIaBoard[position] == R.drawable.appboat
                || realIaBoard[position] == R.drawable.boat2
                || realIaBoard[position] == R.drawable.boat3
    }

    private fun setUpIaBoard() {
        val visibleIaBoard: IntArray = addBoxesToGrid()
        realIaBoard = createRealIaBoard()
        binding.iaBoardGridView.numColumns = positions
        iaAdapter = GameAdapter(this, visibleIaBoard, binding.iaBoardGridView)
        binding.iaBoardGridView.adapter = iaAdapter
    }

    private fun createRealIaBoard(): IntArray {
        var board = addBoxesToGrid()
        var boats = 0
        var boat = 0
        while(boats != 3) {
            var column = (0..positions - 4).random()
            var row = (0 until positions).random()
            boat = (1..3).random()
            if (!boatInPosition(column,row, board))  {
                when(boat) {
                    1 -> board = addBoatToGrid(row, column, board, R.drawable.appboat)
                    2 -> board = addBoatToGrid(row, column, board, R.drawable.boat2)
                    3-> board = addBoatToGrid(row, column, board, R.drawable.boat3)
                }
                boats += 1
            }
        }
        return board
    }

    private fun addBoatToGrid(row: Int, column: Int, board: IntArray, boat: Int) : IntArray {
        board[row * positions + column] = boat
        board[row * positions + column + 1] = boat
        board[row * positions + column + 2] = boat
        iaCellsWithBoats += 3
        if(boat == R.drawable.boat2) {
            board[row * positions + column + 3] = boat
            iaCellsWithBoats += 1
        }
        return board
    }

    private fun boatInPosition(column: Int, row: Int, board: IntArray): Boolean {
        return board[row * positions + column] != R.drawable.boardbox
                || board[row * positions + column + 1] != R.drawable.boardbox
                || board[row * positions + column + 2] != R.drawable.boardbox
                || board[row * positions + column + 3] != R.drawable.boardbox
    }

    private fun getIntentInfo() {
        positions = intent.getStringExtra("oceanLevel")!!.toInt()
        playerName = intent.getStringExtra("playerName")!!
        difficulty = intent.getStringExtra("difficulty")!!
        binding.boardGridView.numColumns = positions
    }

    private fun addBoxesToGrid(): IntArray {
        return IntArray(positions * positions) {R.drawable.boardbox}
    }

    private fun askForBoatsToUser() {
        val intent= Intent(this, BoatSelectorActivity::class.java)
        intent.putExtra("oceanLevel", positions)
        getUserBoard.launch(intent)
    }
}