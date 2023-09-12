package com.example.experiments.zoomview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.experiments.zoomview.zoomimage.PhotoViewerAttacher

/*
 * Created by Sudhanshu Kumar on 07/09/23.
 */

internal class ZoomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var attacher: PhotoViewerAttacher? = null
    private var pendingScaleType: ImageView.ScaleType? = null

    init {
        attacher = PhotoViewerAttacher(this)
        super.setScaleType(ImageView.ScaleType.MATRIX)
        //apply the previously applied scale type
        if (pendingScaleType != null) {
            scaleType = pendingScaleType
            pendingScaleType = null
        }
    }

}