package com.example.experiments.zoomview.zoomimage

/*
 * Created by Sudhanshu Kumar on 05/09/23.
 */

interface OnGestureListener {

    fun onScale(scaleFactor: Float, focusX: Float, focusY: Float)

    fun onScale(scaleFactor: Float, focusX: Float, focusY: Float, dx: Float, dy: Float)

}