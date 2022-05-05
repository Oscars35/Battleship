package com.example.sinkthefloat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoatSelectorActivityViewModel: ViewModel() {

    var alreadySelectedNumber = 0
    var positions = 0

    val gameBoard: MutableLiveData<IntArray> by lazy {
        MutableLiveData<IntArray>(IntArray(positions * positions) {R.drawable.boardbox})
    }

    val selected: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val alreadySelected: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val userCellsWithBoats: MutableLiveData<MutableList<Int>> by lazy {
        MutableLiveData<MutableList<Int>>(mutableListOf())
    }
}