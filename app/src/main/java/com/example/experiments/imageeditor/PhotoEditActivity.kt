package com.example.experiments.imageeditor

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.experiments.R
import com.example.experiments.databinding.ActivityPhotoEditBinding
import com.example.experiments.userstorynew.utils.hide
import com.example.experiments.userstorynew.utils.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy


class PhotoEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoEditBinding

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stubView = binding.viewStub.inflate()
        val imageView = stubView.findViewById<ImageView>(R.id.imageViewStub)
        scope.launch {
            delay(2000L)
            withContext(Dispatchers.Main) {
                imageView.hide()
                withContext(Dispatchers.IO) {
                    delay(2000L)
                    withContext(Dispatchers.Main) {
                        changeConstraint(stubView, 2)
                        imageView.show()
                        withContext(Dispatchers.IO) {
                            delay(2000L)
                            withContext(Dispatchers.Main) {
                                imageView.hide()
                                withContext(Dispatchers.IO) {
                                    delay(2000L)
                                    withContext(Dispatchers.Main) {
                                        changeConstraint(stubView, 1)
                                        imageView.show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun changeConstraint(stubView: View, type: Int) {
        when(type) {
            1 -> {
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.root)
                constraintSet.clear(stubView.id, ConstraintSet.TOP)
                constraintSet.clear(stubView.id, ConstraintSet.BOTTOM)
                constraintSet.connect(
                    stubView.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    stubView.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                constraintSet.applyTo(binding.root)
            }
            2 -> {
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.root)
                constraintSet.clear(stubView.id, ConstraintSet.BOTTOM)
                constraintSet.connect(
                    stubView.id,
                    ConstraintSet.BOTTOM,
                    binding.bottomView.id,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(binding.root)
            }
        }
    }
}