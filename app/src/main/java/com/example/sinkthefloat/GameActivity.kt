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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sinkthefloat.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private var MAX_BOATS = 3
    private var MIN_PROBABILITY = 6
    private var MEDIUM_PROBABILITY = 4
    private var MAX_PROBABILITY = 2

    private lateinit var binding: ActivityGameBinding
    private lateinit var playerName: String
    private lateinit var difficulty: String
    private lateinit var playerOneAdapter: GameAdapter
    private lateinit var iaAdapter: GameAdapter
    private var alreadyClicked: Boolean = false

    private var iaCellsWithBoats = 0
    private var userHitBoats = 0
    private lateinit var viewModel: GameActivityViewModel

    private val getUserBoard = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.playerOneBoard.value = result.data?.getIntArrayExtra("gridAdapter")!!
            viewModel.userCellsWithBoats.value = result.data?.getIntArrayExtra("userCellsWithBoats")!!
            binding.remainingShipsTv.text = getString(R.string.reamining_ships) + ": " + (viewModel.userCellsWithBoats.value!!.size - userHitBoats).toString()
            playerOneAdapter = GameAdapter(this, viewModel.playerOneBoard!!.value!!, binding.boardGridView!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GameActivityViewModel::class.java]
        createObservers()

        getIntentInfo()
        setUpIaBoard()
        askForBoatsToUserIfFirstTime()

        binding.shotDownShipsTv.text = getString(R.string.shot_down_ships) + ": " + viewModel.shotDownShips.value.toString()
        setActualTurn(playerName)

        binding.iaBoardGridView!!.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            addLog("User selected position: ${position.toString()}")
            selectedGridStuff(position)
        }
    }

    private fun addLog(message: String) {
        viewModel.logsArray.value!!.add(message)
    }

    private fun createObservers() {
        viewModel.actualTurn.observe(this, Observer {
            binding.actualTurnTv.text = getString(R.string.actual_turn) + ": " + viewModel.actualTurn.value
        })

        viewModel.visibleIaBoard.observe(this, Observer {
            iaAdapter = GameAdapter(this, viewModel.visibleIaBoard!!.value!!, binding!!.iaBoardGridView)
            binding.iaBoardGridView.adapter = iaAdapter
        })

        viewModel.playerOneBoard.observe(this, Observer {
            playerOneAdapter = GameAdapter(this, viewModel.playerOneBoard.value!!, binding!!.boardGridView)
            binding.boardGridView.adapter = playerOneAdapter
        })
    }

    private fun askForBoatsToUserIfFirstTime() {
        if(viewModel.firstTime.value!!) {
            askForBoatsToUser()
            addLog("User selected his boats")
            viewModel.firstTime.value = false
        }
    }

    private fun setActualTurn(actualTurnName: String) {
        viewModel.actualTurn.value = actualTurnName
        binding.actualTurnTv.text = getString(R.string.actual_turn) + ": " + viewModel.actualTurn.value
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
            setActualTurn("IA")
            iaPredictPosition()
            changeViewWithDelay()
            alreadyClicked = false
        }, 1000L)
    }

    private fun changeViewWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            changeView(View.VISIBLE, View.GONE)
            setActualTurn(playerName)
        }, 2000L)
    }

    private fun iaPredictPosition() {
        when(difficulty) {
            "Easy" -> predictPosition(MIN_PROBABILITY)
            "Medium" -> predictPosition(MEDIUM_PROBABILITY)
            "Hard" -> predictPosition(MAX_PROBABILITY)
        }
    }

    private fun predictPosition(probability: Int) {
        if((1..probability).random() == 1)
            hitBoat()
        else
            noHitBoat()
    }

    private fun noHitBoat() {
        var predicted = (0 until viewModel.positions).random() * viewModel.positions + (0..9).random() // ROW + COLUMN
        while(!noBoatInThisPosition(predicted)) {
            predicted = (0 until viewModel.positions).random() * viewModel.positions + (0..9).random()
        }
        addLog("AI Failed to hit a boat in position: $predicted")
        changeImageWithPredicted(predicted)
    }

    private fun changeImageWithPredicted(predicted: Int) {
        playerOneAdapter.setImage(predicted, R.drawable.failedhit)
        binding.boardGridView.adapter = playerOneAdapter
    }

    private fun noBoatInThisPosition(predicted: Int): Boolean {
        return viewModel.playerOneBoard!!.value!![predicted] != R.drawable.appboat
                && viewModel.playerOneBoard!!.value!![predicted] != R.drawable.boat2
                && viewModel.playerOneBoard!!.value!![predicted] != R.drawable.boat3
    }

    private fun hitBoat() {
        addLog("AI hit boat in position: ${viewModel.userCellsWithBoats.value!![userHitBoats]}")
        playerOneAdapter.setImage(viewModel.userCellsWithBoats.value!![userHitBoats], R.drawable.destroyedboat)
        binding.boardGridView!!.adapter = playerOneAdapter
        userHitBoats += 1
        binding.remainingShipsTv.text = getString(R.string.reamining_ships) + ": " + (viewModel.userCellsWithBoats.value!!.size - userHitBoats).toString()
        checkForIaWinner()
    }

    private fun checkForIaWinner() {
        if(userHitBoats == viewModel.userCellsWithBoats.value!!.size)
            goWinnerScreen("IA")
    }

    private fun changeView(view1: Int, view2: Int) {
        binding.iaBoardGridView!!.visibility = view1
        binding.boardGridView!!.visibility = view2
        binding.boardGridView!!.adapter = playerOneAdapter
    }

    private fun checkPosition(position: Int) {
        if(boatInPositionSelected(position)) {
            iaAdapter.setImage(position, R.drawable.destroyedboat)
            binding.iaBoardGridView!!.adapter = iaAdapter
            iaCellsWithBoats -= 1
            viewModel.shotDownShips.value = ++viewModel.shotDownShipsModify
            binding.shotDownShipsTv.text = getString(R.string.shot_down_ships) + ": " + viewModel.shotDownShips.value.toString()
            checkForUserWinner()
        }
        else {
            iaAdapter.setImage(position, R.drawable.failedhit)
            binding.iaBoardGridView!!.adapter = iaAdapter
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
        intent.putExtra("logsArray", viewModel.logsArray.value)
        setResult(Activity.RESULT_OK, intent)
        startActivity(intent)
        finish()
    }

    private fun boatInPositionSelected(position: Int): Boolean {
        return viewModel.realIaBoard.value!![position] == R.drawable.appboat
                || viewModel.realIaBoard.value!![position] == R.drawable.boat2
                || viewModel.realIaBoard.value!![position] == R.drawable.boat3
    }

    private fun setUpIaBoard() {
        if(viewModel.firstTime.value!!) viewModel.visibleIaBoard.value = addBoxesToGrid()
        if(viewModel.firstTime.value!!) viewModel.realIaBoard.value = createRealIaBoard()
        binding.iaBoardGridView!!.numColumns = viewModel.positions
        iaAdapter = GameAdapter(this, viewModel.visibleIaBoard!!.value!!, binding!!.iaBoardGridView!!)
        binding.iaBoardGridView!!.adapter = iaAdapter
        addLog("AI board generated")
    }

    private fun createRealIaBoard(): IntArray {
        var board = addBoxesToGrid()
        var boats = 0
        while(boats != MAX_BOATS) {
            var column = validColumn()
            var row = validRow()
            var boat = genBoat()
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

    private fun genBoat(): Int = (1..3).random()

    private fun validColumn(): Int = (0..viewModel.positions - 4).random()

    private fun validRow(): Int = (0 until viewModel.positions).random()

    private fun addBoatToGrid(row: Int, column: Int, board: IntArray, boat: Int) : IntArray {
        board[row * viewModel.positions + column] = boat
        board[row * viewModel.positions + column + 1] = boat
        board[row * viewModel.positions + column + 2] = boat
        iaCellsWithBoats += 3
        if(boat == R.drawable.boat2) {
            board[row * viewModel.positions + column + 3] = boat
            iaCellsWithBoats += 1
        }
        return board
    }

    private fun boatInPosition(column: Int, row: Int, board: IntArray): Boolean {
        return board[row * viewModel.positions + column] != R.drawable.boardbox
                || board[row * viewModel.positions + column + 1] != R.drawable.boardbox
                || board[row * viewModel.positions + column + 2] != R.drawable.boardbox
                || board[row * viewModel.positions + column + 3] != R.drawable.boardbox
    }

    private fun getIntentInfo() {
        viewModel.positions = intent.getStringExtra("oceanLevel")!!.toInt()
        playerName = intent.getStringExtra("playerName")!!
        difficulty = intent.getStringExtra("difficulty")!!
        if(viewModel.firstTime.value!!) viewModel.logsArray.value = intent.getStringArrayListExtra("logsArray")
        binding.boardGridView!!.numColumns = viewModel.positions
        addLog("Got intent info")
    }

    private fun addBoxesToGrid(): IntArray {
        return IntArray(viewModel.positions * viewModel.positions) {R.drawable.boardbox}
    }

    private fun askForBoatsToUser() {
        val intent= Intent(this, BoatSelectorActivity::class.java)
        intent.putExtra("oceanLevel", viewModel.positions)
        getUserBoard.launch(intent)
    }
}