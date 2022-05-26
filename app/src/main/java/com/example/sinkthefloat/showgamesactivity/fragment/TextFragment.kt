package com.example.sinkthefloat.showgamesactivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.sinkthefloat.databinding.SecondaryFragmentInfoBinding
import com.example.sinkthefloat.gamebbdd.AppDatabase
import com.example.sinkthefloat.gamebbdd.Game
import org.w3c.dom.Text
import java.time.LocalDateTime

class TextFragment(): Fragment() {

    private lateinit var game: Game
    private lateinit var viewModel: TextFragmentViewModel

    private lateinit var fragBinding: SecondaryFragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = SecondaryFragmentInfoBinding.inflate(inflater, container, false)
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TextFragmentViewModel::class.java]
        setText()
    }

    private fun setText() {
        setViewModelGame()
        fragBinding.gameIdTv.text = "Game number: " + viewModel.game.value?.gameId.toString()
        fragBinding.dateTv.text = "Date: " + viewModel.game.value?.date
        fragBinding.playerNameTv.text = "Player name: " + viewModel.game.value?.playerName
        fragBinding.difficultyTv.text = "Difficulty level: " + viewModel.game.value?.difficulty
        fragBinding.oceanNameTv.text = "Ocean name: " + viewModel.game.value?.oceanName.toString()
        fragBinding.winnerTv.text = "Winner: " + viewModel.game.value?.winner
    }

    private fun setViewModelGame() {
        if(viewModel.firstTime.value == true) {
            viewModel.game.value = game
            viewModel.firstTime.value = false
        }
    }

    fun setGame(game: Game) {
        this.game = game
    }
}