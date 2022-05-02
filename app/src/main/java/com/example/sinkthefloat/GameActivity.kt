package com.example.sinkthefloat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sinkthefloat.databinding.ActivityGameBinding
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var positions: Int = 0
    private lateinit var playerName: String
    private lateinit var difficulty: String
    private lateinit var playerOneBoard: IntArray
    private lateinit var playerOneAdapter: GameAdapter
    private lateinit var iaAdapter: GameAdapter
    private lateinit var realIaBoard: IntArray

    private val getUserBoard = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            playerOneBoard = result.data?.getIntArrayExtra("gridAdapter")!!
            playerOneAdapter = GameAdapter(this, playerOneBoard)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentInfo()
        setUpIaBoard()
        askForBoatsToUser()

        binding.boardGridView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            playerOneAdapter.setImage(position, R.drawable.pirate)
            binding.boardGridView.adapter = playerOneAdapter
        }
    }

    private fun setUpIaBoard() {
        val visibleIaBoard: IntArray = addBoxesToGrid()
        realIaBoard = createRealIaBoard()
        binding.iaBoardGridView.numColumns = positions
        iaAdapter = GameAdapter(this, realIaBoard)
        binding.iaBoardGridView.adapter = iaAdapter
    }

    private fun createRealIaBoard(): IntArray {
        var board = addBoxesToGrid()
        var boats = 0
        var boat = 0
        while(boats != 3) {
            var column = (0..6).random()
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
        board[row * 10 + column] = boat
        board[row * 10 + column + 1] = boat
        board[row * 10 + column + 2] = boat
        if(boat == R.drawable.boat2) board[row * 10 + column + 3] = boat
        return board
    }

    private fun boatInPosition(column: Int, row: Int, board: IntArray): Boolean {
        return board[row * 10 + column] != R.drawable.boardbox
                || board[row * 10 + column + 1] != R.drawable.boardbox
                || board[row * 10 + column + 2] != R.drawable.boardbox
                || board[row * 10 + column + 3] != R.drawable.boardbox
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