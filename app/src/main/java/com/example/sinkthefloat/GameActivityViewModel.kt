package com.example.sinkthefloat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameActivityViewModel: ViewModel() {

    var positions = 0
    var shotDownShipsModify = 0

    val firstTime: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }

    val playerOneBoard: MutableLiveData<IntArray> by lazy {
        MutableLiveData<IntArray>()
    }

    val shotDownShips: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    val realIaBoard: MutableLiveData<IntArray> by lazy {
        MutableLiveData<IntArray>()
    }

    val actualTurn: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val visibleIaBoard: MutableLiveData<IntArray> by lazy {
        MutableLiveData<IntArray>(IntArray(positions * positions) {R.drawable.boardbox})
    }

    val userCellsWithBoats: MutableLiveData<IntArray> by lazy {
        MutableLiveData<IntArray>(IntArray(positions * positions) {R.drawable.boardbox})
    }
}