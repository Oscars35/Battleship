package com.example.sinkthefloat.showgamesactivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sinkthefloat.R
import com.example.sinkthefloat.databinding.ActivityShowgamesBinding
import com.example.sinkthefloat.gamebbdd.Game


class GamesAdapter(private val games: List<Game>): RecyclerView.Adapter<GamesAdapter.GameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return GameHolder(layoutInflater.inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        holder.render(games[position])
        val binding = ActivityShowgamesBinding.inflate(holder.getView().rootView.context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        if(binding.secondaryFragment?.id == null) {
            setActivityOnClickListener(holder, position)
        }
        else {

        }
    }

    private fun setActivityOnClickListener(holder: GameHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ShowGame::class.java)
            intent.putExtra("gameId", games[position].gameId)
            intent.putExtra("date", games[position].date)
            intent.putExtra("playerName", games[position].playerName)
            intent.putExtra("oceanName", games[position].oceanName.toString())
            intent.putExtra("difficulty", games[position].difficulty)
            intent.putExtra("winner", games[position].winner)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    class GameHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun render(game: Game) {
            view.findViewById<TextView>(R.id.hourTv).text = "Date: " + game.date
            view.findViewById<TextView>(R.id.playerTv).text = "Player name: " + game.playerName
            view.findViewById<TextView>(R.id.winnerTv).text = "Game winner: " + game.winner.toString()
        }

        fun getView(): View {
            return view
        }
    }

}