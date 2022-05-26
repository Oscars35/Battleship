package com.example.sinkthefloat.showgamesactivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sinkthefloat.databinding.FragmentRecyclerBinding
import com.example.sinkthefloat.gamebbdd.AppDatabase
import com.example.sinkthefloat.showgamesactivity.GamesAdapter

class RecyclerFragment: Fragment() {

    private lateinit var fragBinding: FragmentRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        fragBinding.gamesRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = GamesAdapter(AppDatabase.getInstance(requireContext()).gameDao().getAll())
        fragBinding.gamesRecycler.adapter = adapter
    }
}