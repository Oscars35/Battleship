package com.example.sinkthefloat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout

class GameAdapter(context: Context, images: IntArray): BaseAdapter() {

    private val context: Context = context
    private val images: IntArray = images
    private lateinit var layoutInflater: LayoutInflater


    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(p0: Int): Any {
        return images[p0]
    }

    override fun getItemId(p0: Int): Long {
        //Not necessary
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var p1 = p1
        if(!::layoutInflater.isInitialized)
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (p1 == null)
            p1 = layoutInflater.inflate(R.layout.grid_item, null)

        val imageView: ImageView? = p1?.findViewById(R.id.gridBoxImage)
        imageView?.setImageResource(images[p0])

        return p1!!
    }
}