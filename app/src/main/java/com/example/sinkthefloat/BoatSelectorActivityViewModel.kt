package com.example.sinkthefloat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoatSelectorActivityViewModel: ViewModel() {

    var alreadySelectedNumber = 0

    val gameAdapter: MutableLiveData<GameAdapter> by lazy {
        MutableLiveData<GameAdapter>()
    }

    val selected: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val alreadySelected: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val userCellsWithBoats: MutableLiveData<MutableList<Int>> by lazy {
        MutableLiveData<MutableList<Int>>()
    }
}