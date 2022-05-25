package com.example.sinkthefloat.gameactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sinkthefloat.R
import java.io.Serializable

class GameActivityViewModel: ViewModel(), Serializable {

    var positions = 0
    var shotDownShipsModify = 0
    var userHitBoatsModify = 0
    var iaCellsWithBoatsModify = 0

    val logsArray: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>(arrayListOf())
    }

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
        MutableLiveData<IntArray>(IntArray(positions * positions) { R.drawable.boardbox })
    }

    val userCellsWithBoats: MutableLiveData<IntArray> by lazy {
        MutableLiveData<IntArray>(IntArray(positions * positions) { R.drawable.boardbox })
    }

    val userHitBoats: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    val iaCellsWithBoats: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    val logsText: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
}