package com.example.experiments.zoomview

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.experiments.zoomview.zoomimage.OnMatrixChangedListener
import com.example.experiments.zoomview.zoomimage.OnOutsidePhotoTapListener
import com.example.experiments.zoomview.zoomimage.OnPhotoTapListener
import com.example.experiments.zoomview.zoomimage.OnScaleChangedListener
import com.example.experiments.zoomview.zoomimage.OnSingleFlingListener
import com.example.experiments.zoomview.zoomimage.OnViewDragListener
import com.example.experiments.zoomview.zoomimage.OnViewTapListener
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