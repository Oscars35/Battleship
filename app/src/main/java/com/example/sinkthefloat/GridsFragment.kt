package com.example.sinkthefloat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sinkthefloat.databinding.FragmentGridsBinding


class GridsFragment : Fragment() {

    private lateinit var fragBinding: FragmentGridsBinding

    private lateinit var playerName: String
    private lateinit var difficulty: DifficultyLevel
    private lateinit var playerOneAdapter: GameAdapter
    private lateinit var iaAdapter: GameAdapter
    private var alreadyClicked: Boolean = false

    private lateinit var viewModel: GameActivityViewModel

    private val getUserBoard = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.playerOneBoard.value = result.data?.getIntArrayExtra("gridAdapter")!!
            viewModel.userCellsWithBoats.value = result.data?.getIntArrayExtra("userCellsWithBoats")!!
            fragBinding.remainingShipsTv.text = getString(R.string.reamining_ships) + ": " +
                    (viewModel.userCellsWithBoats.value!!.size - viewModel.userHitBoats.value!!).toString()
            playerOneAdapter = GameAdapter(requireActivity(), viewModel.playerOneBoard.value!!, fragBinding.boardGridView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = FragmentGridsBinding.inflate(inflater, container, false)
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameActivityViewModel::class.java]
        getAndSetActivityInfo()
        createObservers()

        setUpIaBoard()
        askForBoatsToUserIfFirstTime()

        fragBinding.shotDownShipsTv.text = getString(R.string.shot_down_ships) + ": " + viewModel.shotDownShips.value.toString()
        setActualTurn(playerName)

        fragBinding.iaBoardGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            addLog("User selected position: ${position.toString()}")
            selectedGridStuff(position)
        }
    }

    private fun askForBoatsToUserIfFirstTime() {
        if(viewModel.firstTime.value!!) {
            askForBoatsToUser()
            addLog("User selected his boats")
            viewModel.firstTime.value = false
        }
    }

    private fun askForBoatsToUser() {
        val intent= Intent(requireActivity(), BoatSelectorActivity::class.java)
        intent.putExtra("oceanLevel", viewModel.positions)
        getUserBoard.launch(intent)
    }

    private fun getAndSetActivityInfo() {
        viewModel.positions = arguments?.getString("oceanLevel")!!.toInt()
        fragBinding.boardGridView.numColumns = viewModel.positions
        playerName = arguments?.getString("playerName", "default").toString()
        difficulty = arguments?.getSerializable("difficulty") as DifficultyLevel
        if(viewModel.firstTime.value!!) viewModel.logsArray.value = arguments?.getStringArrayList("logsArray")
    }

    private fun addLog(message: String) {
        viewModel.logsArray.value!!.add(message)
    }

    private fun createObservers() {
        viewModel.actualTurn.observe(requireActivity(), Observer {
            fragBinding.actualTurnTv.text = getString(R.string.actual_turn) + ": " + viewModel.actualTurn.value
        })

        viewModel.userCellsWithBoats.observe(requireActivity(), Observer {
            fragBinding.remainingShipsTv.text = getString(R.string.reamining_ships) + ": " +
                    (viewModel.userCellsWithBoats.value!!.size - viewModel.userHitBoats.value!!).toString()
        })

        viewModel.visibleIaBoard.observe(requireActivity(), Observer {
            iaAdapter = GameAdapter(requireActivity(), viewModel.visibleIaBoard.value!!, fragBinding.iaBoardGridView)
            fragBinding.iaBoardGridView.adapter = iaAdapter
        })

        viewModel.playerOneBoard.observe(requireActivity(), Observer {
            playerOneAdapter = GameAdapter(requireActivity(), viewModel.playerOneBoard.value!!, fragBinding.boardGridView)
            fragBinding.boardGridView.adapter = playerOneAdapter
        })
    }

    private fun setActualTurn(actualTurnName: String) {
        viewModel.actualTurn.value = actualTurnName
        fragBinding.actualTurnTv.text = activity?.getString(R.string.actual_turn) + ": " + viewModel.actualTurn.value
    }

    private fun selectedGridStuff(position: Int) {
        if(alreadyClickedPosition(position)) {
            Toast.makeText(requireActivity(), R.string.position_already_guessed, Toast.LENGTH_SHORT).show()
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
            setActualTurn("AI")
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
            DifficultyLevel.EASY -> predictPosition(GameActivity.Constants.MIN_PROBABILITY)
            DifficultyLevel.MEDIUM -> predictPosition(GameActivity.Constants.MEDIUM_PROBABILITY)
            DifficultyLevel.HARD -> predictPosition(GameActivity.Constants.MAX_PROBABILITY)
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
        viewModel.playerOneBoard.value = playerOneAdapter.getImages()
        playerOneAdapter.notifyDataSetChanged()
    }

    private fun noBoatInThisPosition(predicted: Int): Boolean {
        return viewModel.playerOneBoard.value!![predicted] != R.drawable.appboat
                && viewModel.playerOneBoard.value!![predicted] != R.drawable.boat2
                && viewModel.playerOneBoard.value!![predicted] != R.drawable.boat3
                && viewModel.playerOneBoard.value!![predicted] != R.drawable.failedhit
                && viewModel.playerOneBoard.value!![predicted] != R.drawable.destroyedboat
    }

    private fun hitBoat() {
        addLog("AI hit boat in position: ${viewModel.userCellsWithBoats.value!![viewModel.userHitBoats.value!!]}")
        playerOneAdapter.setImage(viewModel.userCellsWithBoats.value!![viewModel.userHitBoats.value!!], R.drawable.destroyedboat)
        viewModel.playerOneBoard.value = playerOneAdapter.getImages()
        playerOneAdapter.notifyDataSetChanged()
        viewModel.userHitBoats.value = ++viewModel.userHitBoatsModify
        fragBinding.remainingShipsTv.text = getString(R.string.reamining_ships) + ": " +
                (viewModel.userCellsWithBoats.value!!.size - viewModel.userHitBoats.value!!).toString()
        checkForIaWinner()
    }

    private fun checkForIaWinner() {
        if(viewModel.userHitBoats.value!! == viewModel.userCellsWithBoats.value!!.size)
            goWinnerScreen("IA")
    }

    private fun changeView(view1: Int, view2: Int) {
        fragBinding.iaBoardGridView.visibility = view1
        fragBinding.boardGridView.visibility = view2
        fragBinding.boardGridView.adapter = playerOneAdapter
    }

    private fun checkPosition(position: Int) {
        if(boatInPositionSelected(position)) {
            iaAdapter.setImage(position, R.drawable.destroyedboat)
            viewModel.visibleIaBoard.value = iaAdapter.getImages()
            iaAdapter.notifyDataSetChanged()
            viewModel.iaCellsWithBoats.value = --viewModel.iaCellsWithBoatsModify
            viewModel.shotDownShips.value = ++viewModel.shotDownShipsModify
            fragBinding.shotDownShipsTv.text = getString(R.string.shot_down_ships) + ": " + viewModel.shotDownShips.value.toString()
            checkForUserWinner()
        }
        else {
            iaAdapter.setImage(position, R.drawable.failedhit)
            viewModel.visibleIaBoard.value = iaAdapter.getImages()
            iaAdapter.notifyDataSetChanged()
        }
    }

    private fun checkForUserWinner() {
        if(viewModel.iaCellsWithBoats.value == 0) {
            goWinnerScreen(playerName)
        }
    }

    private fun goWinnerScreen(winnerName: String) {
        val intent = Intent(requireActivity(), WinnerActivity::class.java)
        intent.putExtra("winner", winnerName)
        intent.putExtra("logsArray", viewModel.logsArray.value)
        requireActivity().setResult(Activity.RESULT_OK, intent)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    private fun boatInPositionSelected(position: Int): Boolean {
        return viewModel.realIaBoard.value!![position] == R.drawable.appboat
                || viewModel.realIaBoard.value!![position] == R.drawable.boat2
                || viewModel.realIaBoard.value!![position] == R.drawable.boat3
    }

    private fun setUpIaBoard() {
        if(viewModel.firstTime.value!!) viewModel.visibleIaBoard.value = addBoxesToGrid()
        if(viewModel.firstTime.value!!) viewModel.realIaBoard.value = createRealIaBoard()
        fragBinding.iaBoardGridView.numColumns = viewModel.positions
        iaAdapter = GameAdapter(requireActivity(), viewModel.visibleIaBoard.value!!, fragBinding.iaBoardGridView)
        fragBinding.iaBoardGridView.adapter = iaAdapter
        addLog("AI board generated")
    }

    private fun createRealIaBoard(): IntArray {
        var board = addBoxesToGrid()
        var boats = 0
        while(boats != GameActivity.Constants.MAX_BOATS) {
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
        viewModel.iaCellsWithBoatsModify += 3
        viewModel.iaCellsWithBoats.value = viewModel.iaCellsWithBoatsModify
        if(boat == R.drawable.boat2) {
            board[row * viewModel.positions + column + 3] = boat
            viewModel.iaCellsWithBoats.value = ++viewModel.iaCellsWithBoatsModify
        }
        return board
    }

    private fun boatInPosition(column: Int, row: Int, board: IntArray): Boolean {
        return board[row * viewModel.positions + column] != R.drawable.boardbox
                || board[row * viewModel.positions + column + 1] != R.drawable.boardbox
                || board[row * viewModel.positions + column + 2] != R.drawable.boardbox
                || board[row * viewModel.positions + column + 3] != R.drawable.boardbox
    }

    private fun addBoxesToGrid(): IntArray {
        return IntArray(viewModel.positions * viewModel.positions) {R.drawable.boardbox}
    }

}