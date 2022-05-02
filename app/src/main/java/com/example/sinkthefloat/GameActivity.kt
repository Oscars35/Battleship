package com.example.sinkthefloat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.sinkthefloat.databinding.ActivityGameBinding
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var positions: Int = 0
    private lateinit var playerName: String
    private lateinit var difficulty: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentInfo()

        val playerOneBoard: IntArray = addBoxesToGrid()
        val iaBoard: IntArray = addBoxesToGrid()

        askForBoatsToUser()

        val playerOneAdapter = GameAdapter(this, playerOneBoard)
        binding.boardGridView.adapter = playerOneAdapter
        binding.boardGridView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            playerOneAdapter.setImage(position, R.drawable.pirate)
            binding.boardGridView.adapter = playerOneAdapter
        }
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
        startActivity(intent)
    }
}