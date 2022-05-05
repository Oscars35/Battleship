package com.example.sinkthefloat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameActivityViewModel: ViewModel() {

    val firstTime: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
}