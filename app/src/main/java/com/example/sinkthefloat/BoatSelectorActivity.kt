package com.example.sinkthefloat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sinkthefloat.databinding.ActivityBoatSelectorBinding

class BoatSelectorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityBoatSelectorBinding
    private lateinit var gameAdapter: GameAdapter
    private var selected = 0
    private var alreadySelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoatSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val positions: Int = intent.getIntExtra("oceanLevel", 10)
        val board = IntArray(positions * positions) {R.drawable.boardbox}
        binding.shipSelectorGridView.numColumns = positions
        gameAdapter = GameAdapter(this, board)
        binding.shipSelectorGridView.adapter = gameAdapter

        binding.shipSelectorGridView.setOnItemClickListener { adapterView, view, position, l ->
            if(position % 10 > 7 && (selected == binding.boatOne.id || selected == binding.boatThree.id))
                Toast.makeText(this, "Select again, boat does not fit in here", Toast.LENGTH_LONG).show()
            else if (position % 10 > 6 && selected == binding.boatTwo.id)
                Toast.makeText(this, "Select again, boat does not fit in here", Toast.LENGTH_LONG).show()
            else if(thereIsABoatOnThisPosition(position)) {
                Toast.makeText(this, "Select again, boat does not fit in here", Toast.LENGTH_LONG).show()
            }
            else {
                changeGridImagesWithSelected(position)
                turnGridInvisible()
            }
        }

        binding.boatOne.setOnClickListener(this)
        binding.boatTwo.setOnClickListener(this)
        binding.boatThree.setOnClickListener(this)
    }

    private fun thereIsABoatOnThisPosition(position: Int): Boolean {
        return gameAdapter.getItem(position) != R.drawable.boardbox
            || gameAdapter.getItem(position + 1) != R.drawable.boardbox
            || gameAdapter.getItem(position + 2) != R.drawable.boardbox
            || gameAdapter.getItem(position + 3) != R.drawable.boardbox
    }

    override fun onClick(p0: View?) {
        alreadySelected += 1
        turnGridVisible()
        when(p0?.id) {
            binding.boatOne.id -> selected = binding.boatOne.id
            binding.boatTwo.id -> selected = binding.boatTwo.id
            binding.boatThree.id -> selected = binding.boatThree.id
        }
    }

    private fun changeGridImagesWithSelected(position: Int) {
        when(selected) {
            binding.boatOne.id -> changeImages(position, R.drawable.appboat, 3)
            binding.boatTwo.id -> changeImages(position, R.drawable.boat2, 4)
            binding.boatThree.id -> changeImages(position, R.drawable.boat3, 3)
        }
        binding.shipSelectorGridView.adapter = gameAdapter
    }

    private fun changeImages(position: Int, id: Int, objective: Int) {
        var position = position
        for(images in 1..objective) {
            gameAdapter.setImage(position, id)
            position += 1
        }
    }

    private fun turnGridVisible() {
        binding.boatImagesIv.visibility = View.GONE
        binding.boatOne.visibility = View.GONE
        binding.boatTwo.visibility = View.GONE
        binding.boatThree.visibility = View.GONE
        binding.threeBlocksOneTv.visibility = View.GONE
        binding.fourBlocksOneTv.visibility = View.GONE
        binding.availableShipsTv.visibility = View.GONE
        binding.threeBlocksTwoTv.visibility = View.GONE
        binding.shipSelectorGridView.visibility = View.VISIBLE
    }

    private fun turnGridInvisible() {
        if(alreadySelected == 3) endSelection()
        binding.boatImagesIv.visibility = View.VISIBLE
        binding.boatOne.visibility = View.VISIBLE
        binding.boatTwo.visibility = View.VISIBLE
        binding.boatThree.visibility = View.VISIBLE
        binding.threeBlocksOneTv.visibility = View.VISIBLE
        binding.fourBlocksOneTv.visibility = View.VISIBLE
        binding.availableShipsTv.visibility = View.VISIBLE
        binding.threeBlocksTwoTv.visibility = View.VISIBLE
        binding.shipSelectorGridView.visibility = View.GONE
    }

    private fun endSelection() {
        val data = Intent()
        data.putExtra("gridAdapter", gameAdapter.getImages())
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}