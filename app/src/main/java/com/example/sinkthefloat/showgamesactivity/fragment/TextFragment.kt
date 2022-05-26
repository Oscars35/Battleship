package com.example.sinkthefloat.showgamesactivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sinkthefloat.databinding.SecondaryFragmentInfoBinding

class TextFragment: Fragment() {

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
        fragBinding.gameId.text = "HOLA"
    }
}