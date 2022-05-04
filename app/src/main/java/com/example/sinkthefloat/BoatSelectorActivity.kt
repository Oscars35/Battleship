package com.example.sinkthefloat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sinkthefloat.databinding.ActivityBoatSelectorBinding

class BoatSelectorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityBoatSelectorBinding
    lateinit var viewModel: BoatSelectorActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoatSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[BoatSelectorActivityViewModel::class.java]
        viewModel.userCellsWithBoats.value = mutableListOf()

        createObservers()

        val positions: Int = intent.getIntExtra("oceanLevel", 10)
        val board = IntArray(positions * positions) {R.drawable.boardbox}
        binding.shipSelectorGridView.numColumns = positions
        viewModel.gameAdapter.value = GameAdapter(this, board, binding.shipSelectorGridView)
        binding.shipSelectorGridView.adapter = viewModel.gameAdapter.value

        binding.shipSelectorGridView.setOnItemClickListener { adapterView, view, position, l ->
            when {
                threeBoatDoesNotFit(positions, position) -> Toast.makeText(this, "Select again, boat does not fit in here", Toast.LENGTH_LONG).show()
                fourBoatDoesNotFit(positions, position) -> Toast.makeText(this, "Select again, boat does not fit in here", Toast.LENGTH_LONG).show()
                thereIsABoatOnThisPosition(position) -> {
                    Toast.makeText(this, "Select again, boat does not fit in here", Toast.LENGTH_LONG).show()
                }
                else -> {
                    changeGridImagesWithSelected(position)
                    turnGridInvisible()
                }
            }
        }

        binding.boatOne.setOnClickListener(this)
        binding.boatTwo.setOnClickListener(this)
        binding.boatThree.setOnClickListener(this)
    }

    private fun createObservers() {
        viewModel.gameAdapter.observe(this, Observer { lastAdapter ->
            binding.shipSelectorGridView.adapter = lastAdapter
        })
    }

    private fun fourBoatDoesNotFit(positions: Int, position: Int): Boolean {
        return position % positions > positions - 4
                && viewModel.selected.value == binding.boatTwo.id
    }

    private fun threeBoatDoesNotFit(positions: Int, position: Int): Boolean {
        return position % positions > positions - 3
                && (viewModel.selected.value == binding.boatOne.id || viewModel.selected.value == binding.boatThree.id)
    }

    private fun thereIsABoatOnThisPosition(position: Int): Boolean {
        return when(viewModel.selected.value) {
            binding.boatOne.id, binding.boatThree.id -> noBoatInSelected(position)
            else -> noBoatInSelected(position) || viewModel.gameAdapter.value!!.getItem(position + 3) != R.drawable.boardbox
        }
    }

    private fun noBoatInSelected(position: Int): Boolean {
        return viewModel.gameAdapter.value!!.getItem(position) != R.drawable.boardbox
                || viewModel.gameAdapter.value!!.getItem(position + 1) != R.drawable.boardbox
                || viewModel.gameAdapter.value!!.getItem(position + 2) != R.drawable.boardbox
    }

    override fun onClick(p0: View?) {
        viewModel.alreadySelected.value = ++viewModel.alreadySelectedNumber
        turnGridVisible()
        when(p0?.id) {
            binding.boatOne.id -> viewModel.selected.value = binding.boatOne.id
            binding.boatTwo.id -> viewModel.selected.value = binding.boatTwo.id
            binding.boatThree.id -> viewModel.selected.value = binding.boatThree.id
        }
    }

    private fun changeGridImagesWithSelected(position: Int) {
        when(viewModel.selected.value) {
            binding.boatOne.id -> changeImages(position, R.drawable.appboat, 3)
            binding.boatTwo.id -> changeImages(position, R.drawable.boat2, 4)
            binding.boatThree.id -> changeImages(position, R.drawable.boat3, 3)
        }
        binding.shipSelectorGridView.adapter = viewModel.gameAdapter.value
    }

    private fun changeImages(position: Int, id: Int, objective: Int) {
        var position = position
        for(images in 1..objective) {
            viewModel.gameAdapter.value!!.setImage(position, id)
            viewModel.userCellsWithBoats.value!!.add(position)
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
        if(viewModel.alreadySelected.value == 3) endSelection()
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
        data.putExtra("gridAdapter", viewModel.gameAdapter.value!!.getImages())
        data.putExtra("userCellsWithBoats", viewModel.userCellsWithBoats.value!!.toIntArray())
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}