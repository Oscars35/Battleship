package com.example.sinkthefloat.showgamesactivity.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sinkthefloat.gamebbdd.Game

class TextFragmentViewModel: ViewModel() {

    val game: MutableLiveData<Game> by lazy {
        MutableLiveData<Game>()
    }

    val firstTime: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
}