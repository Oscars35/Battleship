package com.example.sinkthefloat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sinkthefloat.databinding.FragmentGridsBinding

class GridsFragment : Fragment() {

    private lateinit var binding: FragmentGridsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGridsBinding.inflate(inflater, container, false)
        binding.boardGridView.numColumns = 4
        binding.iaBoardGridView.numColumns = 4
        return binding.root
    }

    fun getBinding(): FragmentGridsBinding {
        return binding
    }
}