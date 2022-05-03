package com.example.sinkthefloat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.sinkthefloat.databinding.ActivityWinnerBinding

class WinnerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWinnerBinding
    private var winner: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        winner = intent.getStringExtra("winner")
        binding.winnerTv.text = getString(R.string.winner) + " " + winner

        binding.exitButton.setOnClickListener(this)
        binding.mailButton.setOnClickListener(this)
        binding.newGameButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.newGameButton.id -> startNewGame()
            binding.mailButton.id -> sendMail()
            binding.exitButton.id -> exitApp()
        }
    }

    private fun sendMail() {
        if(TextUtils.isEmpty(binding.playerNameEditText.text)) binding.playerNameEditText.error = "You have to enter mail first"
        else sendMailViaIntent()
    }

    private fun sendMailViaIntent() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + binding.playerNameEditText.text.toString()))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Winner of the match")
        intent.putExtra(Intent.EXTRA_TEXT, winner.toString())
        startActivity(intent)
    }

    private fun startNewGame() {
        finish()
    }

    private fun exitApp() {
        finishAffinity()
    }
}