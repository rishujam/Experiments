package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.experiments.R
import com.example.experiments.atomdesign.component.uistates.SocialBarViewUiState
import com.example.experiments.databinding.SocialBarViewLayoutBinding
import com.virinchi.atomdesign.component.constants.DownloadState
import com.virinchi.atomdesign.component.constants.DrawablePosition
import com.virinchi.atomdesign.component.constants.SocialBarViewOptions
import com.virinchi.atomdesign.component.extensions.makeGone
import com.virinchi.atomdesign.component.extensions.makeVisible
import com.virinchi.atomdesign.component.extensions.setDrawable
import com.virinchi.atomdesign.component.extensions.setDrawableTintColor
import com.virinchi.atomdesign.component.extensions.textColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SocialBarView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private val binding by lazy { SocialBarViewLayoutBinding.inflate(LayoutInflater.from(context)) }
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        private val _uiStates = MutableSharedFlow<SocialBarViewUiState>()
        val uiStates: SharedFlow<SocialBarViewUiState> = _uiStates

        var likeText: String = ""
            set(value) {
                binding.tvSocialUserActionLike.text = value
                field = value
            }

        var commentText: String = ""
            set(value) {
                binding.tvSocialUserActionComment.text = value
                field = value
            }

        var repostText: String = ""
            set(value) {
                binding.tvSocialUserActionRepost.text = value
                field = value
            }

        var bookmarkText: String = ""
            set(value) {
                binding.tvSocialUserActionBookmark.text = value
                field = value
            }

        var shareText: String = ""
            set(value) {
                binding.tvSocialUserActionShare.text = value
                field = value
            }

        var downloadText: String = ""
            set(value) {
                binding.tvSocialUserActionDownload.text = value
                field = value
            }

        private var optionsMap =
            mapOf(
                SocialBarViewOptions.LIKE to binding.tvSocialUserActionLike,
                SocialBarViewOptions.COMMENT to binding.tvSocialUserActionComment,
                SocialBarViewOptions.SHARE to binding.tvSocialUserActionShare,
                SocialBarViewOptions.BOOKMARK to binding.tvSocialUserActionBookmark,
                SocialBarViewOptions.DOWNLOAD to binding.tvSocialUserActionDownload,
                SocialBarViewOptions.REPOST to binding.tvSocialUserActionRepost,
            )

        init {
            initView(attrs)
            initClickListeners()
        }

        private fun initClickListeners() {
            val likeClickListener =
                OnClickListener {
                    if (isLiked) return@OnClickListener
                    updateLikeState(!isLiked)
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.LikeClicked(!isLiked))
                    }
                }
            val commentClickListener =
                OnClickListener {
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.CommentClicked)
                    }
                }
            val shareClickListener =
                OnClickListener {
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.ShareClicked)
                    }
                }
            val repostClickListener =
                OnClickListener {
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.RepostClicked)
                    }
                }

            val bookmarkClickListener =
                OnClickListener {
                    updateBookmarkState(!isBookMarked)
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.BookmarkClicked)
                    }
                }

            val downloadClickListener =
                OnClickListener {
                    updateDownloadState(downloadState)
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.DownloadClicked)
                    }
                }

            binding.tvSocialUserActionLike.setOnClickListener(likeClickListener)
            binding.tvSocialUserActionComment.setOnClickListener(commentClickListener)
            binding.tvSocialUserActionShare.setOnClickListener(shareClickListener)
            binding.tvSocialUserActionRepost.setOnClickListener(repostClickListener)
            binding.tvSocialUserActionBookmark.setOnClickListener(bookmarkClickListener)
            binding.tvSocialUserActionDownload.setOnClickListener(downloadClickListener)
        }

        private fun initView(attrs: AttributeSet?) {
            val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            addView(binding.root, layoutParams)
            initListeners()
        }

        private fun updateLikeState(isLiked: Boolean) {
            binding.tvSocialUserActionLike.apply {
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_primary_black,
                    ),
                )
//                setDrawableToggle(
//                    isLiked,
//                    R.drawable.ic_liked_filled_red_2_0,
//                    R.drawable.ic_like_2_0,
//                    getCurrentTextViewDrawablePosition(),
//                )
            }
        }

        private fun updateBookmarkState(isBookmarked: Boolean) {
            binding.tvSocialUserActionBookmark.apply {
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_primary_black,
                    ),
                )
//                setDrawableToggle(
//                    isBookmarked,
//                    R.drawable.ic_bookmarked_v_2_0,
//                    R.drawable.ic_bookmark_2_0,
//                    getCurrentTextViewDrawablePosition(),
//                )
            }
        }

        private fun updateDownloadState(state: DownloadState) {
            binding.tvSocialUserActionDownload.apply {
                when (state) {
                    DownloadState.IDLE, DownloadState.FAILED -> {
//                        setDrawableWithResourceId(R.drawable.ic_download_2_0, getCurrentTextViewDrawablePosition())
                        textColor(R.color.color_primary_black)
                    }
                    DownloadState.LOADING -> {
//                        setDrawableWithResourceId(R.drawable.ic_download_2_0, getCurrentTextViewDrawablePosition())
                        R.color.color_secondary_warning_success_green.apply {
                            setDrawableTintColor(ContextCompat.getColor(context, this))
                            textColor(this)
                        }
                    }
                    DownloadState.COMPLETED -> {
//                        setDrawableWithResourceId(R.drawable.ic_done_2_0, getCurrentTextViewDrawablePosition())
                        textColor(R.color.color_secondary_deep_green)
                    }
                }
            }
        }

        private fun updateDownloadEnableState(isEnabled: Boolean) {
            binding.tvSocialUserActionDownload.apply {
                if (isEnabled) {
                    setEnabled(true)
                    alpha = 1.0f
                } else {
                    setEnabled(false)
                    alpha = 0.5f
                    updateDownloadState(DownloadState.IDLE)
                }
            }
        }

        fun updateLikeColorState(
            enabledColor: Int,
            disabledColor: Int,
        ) {
            binding.tvSocialUserActionLike.apply {
                if (isLiked) {
                    setDrawableTintColor(enabledColor)
                    setTextColor(enabledColor)
                } else {
                    setDrawableTintColor(disabledColor)
                    setTextColor(disabledColor)
                }
            }
        }

        fun updateColorState(
            tint: Int,
            option: SocialBarViewOptions,
        ) {
            optionsMap[option]?.apply {
                setDrawableTintColor(tint)
                setTextColor(tint)
            }
        }

        private fun initListeners() {
            binding.flowSocialUserActions.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                adjustTextViewDrawablePosition()
            }
        }

        private var isLiked = false

        fun setLikedState(isLiked: Boolean) {
            this@SocialBarView.isLiked = isLiked
            updateLikeState(isLiked)
        }

        private var isBookMarked = false

        fun setBookmarkState(isBookmarked: Boolean) {
            this@SocialBarView.isBookMarked = isBookmarked
            updateBookmarkState(isBookmarked)
        }

        private var downloadState = DownloadState.IDLE

        fun setDownloadState(state: DownloadState) {
            this@SocialBarView.downloadState = state
            updateDownloadState(state)
        }

        private var isDownloadEnabled = true

        fun setDownloadEnabled(isEnabled: Boolean) {
            this@SocialBarView.isDownloadEnabled = isDownloadEnabled
            updateDownloadEnableState(isEnabled)
        }

        private var prevVisibleTextViewCount = 0

        private fun adjustTextViewDrawablePosition() {
            val visibleTextViews =
                binding.clSocialUserActionMain
                    .children
                    .filterIsInstance<TextView>()
                    .filter { it.isVisible }

            if (visibleTextViews.count() != prevVisibleTextViewCount) {
                val drawablePosition =
                    if (visibleTextViews.count() > 3) DrawablePosition.TOP else DrawablePosition.LEFT
                visibleTextViews.forEach {
                    val drawable = it.compoundDrawablesRelative[0] ?: it.compoundDrawablesRelative[3]
                    it.setDrawable(drawable, drawablePosition)
                }
                prevVisibleTextViewCount = visibleTextViews.count()
            }
        }

        private fun getCurrentTextViewDrawablePosition() = if (prevVisibleTextViewCount > 3) DrawablePosition.TOP else DrawablePosition.LEFT

        private fun hideAllOptions() {
            optionsMap.forEach { entry -> entry.value.makeGone() }
        }

        fun setVisibleOptions(vararg options: SocialBarViewOptions) {
            hideAllOptions()

            options.forEach {
                optionsMap[it]?.makeVisible()
            }
        }

        override fun onViewRemoved(view: View?) {
            super.onViewRemoved(view)
            scope.cancel()
        }
    }