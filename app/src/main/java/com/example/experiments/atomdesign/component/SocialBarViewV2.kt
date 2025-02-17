package com.example.experiments.atomdesign.component

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.example.experiments.R
import com.virinchi.atomdesign.component.constants.DownloadState
import com.virinchi.atomdesign.component.constants.DrawablePosition
import com.virinchi.atomdesign.component.constants.SocialBarViewOptions
import com.virinchi.atomdesign.component.extensions.setDrawableTintColor
import com.virinchi.atomdesign.component.extensions.textColor
import com.example.experiments.atomdesign.component.uistates.SocialBarViewUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

private const val NO_OF_ACTION_BUTTONS = 4

class SocialBarViewV2
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : LinearLayout(context, attrs, defStyleAttr) {
        private val textViewsMap = mutableMapOf<SocialBarViewOptions, TextView>()

        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        private var areViewsInitialised = false

        private val _uiStates = MutableSharedFlow<SocialBarViewUiState>()
        val uiStates: SharedFlow<SocialBarViewUiState> = _uiStates

        var likeText: String = ""
            set(value) {
                textViewsMap[SocialBarViewOptions.LIKE]?.text = value
                field = value
            }

        var commentText: String = ""
            set(value) {
                textViewsMap[SocialBarViewOptions.COMMENT]?.text = value
                field = value
            }

        var repostText: String = ""
            set(value) {
                textViewsMap[SocialBarViewOptions.REPOST]?.text = value
                field = value
            }

        var bookmarkText: String = ""
            set(value) {
                textViewsMap[SocialBarViewOptions.BOOKMARK]?.text = value
                field = value
            }

        var shareText: String = ""
            set(value) {
                textViewsMap[SocialBarViewOptions.SHARE]?.text = value
                field = value
            }

        var downloadText: String = ""
            set(value) {
                textViewsMap[SocialBarViewOptions.DOWNLOAD]?.text = value
                field = value
            }

        init {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setLayoutParams()
        }

        private fun initClickListeners() {
            setLikeClickListener()
            setCommentClickListener()
            setShareClickListener()
            setRepostClickListener()
            setBookmarkClickListener()
            setDownloadClickListener()
        }

        private fun setDownloadClickListener() {
            val textView = textViewsMap[SocialBarViewOptions.DOWNLOAD]
            val clickListener =
                OnClickListener {
                    updateDownloadState(downloadState)
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.DownloadClicked)
                    }
                }
            textView?.setOnClickListener(clickListener)
        }

        private fun setBookmarkClickListener() {
            val textView = textViewsMap[SocialBarViewOptions.BOOKMARK]
            val clickListener =
                OnClickListener {
                    updateBookmarkState(!isBookMarked)
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.BookmarkClicked)
                    }
                }

            textView?.setOnClickListener(clickListener)
        }

        private fun setRepostClickListener() {
            val textView = textViewsMap[SocialBarViewOptions.REPOST]
            val clickListener =
                OnClickListener {
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.RepostClicked)
                    }
                }
            textView?.setOnClickListener(clickListener)
        }

        private fun setShareClickListener() {
            val textView = textViewsMap[SocialBarViewOptions.SHARE]
            val clickListener =
                OnClickListener {
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.ShareClicked)
                    }
                }

            textView?.setOnClickListener(clickListener)
        }

        private fun setCommentClickListener() {
            val textView = textViewsMap[SocialBarViewOptions.COMMENT]
            val commentClickListener =
                OnClickListener {
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.CommentClicked)
                    }
                }

            textView?.setOnClickListener(commentClickListener)
        }

        private fun setLikeClickListener() {
            val likeTextView = textViewsMap[SocialBarViewOptions.LIKE]
            val likeClickListener =
                OnClickListener {
                    if (isLiked) return@OnClickListener
                    updateLikeState(!isLiked)
                    scope.launch {
                        _uiStates.emit(SocialBarViewUiState.LikeClicked(!isLiked))
                    }
                }

            likeTextView?.setOnClickListener(likeClickListener)
        }

        private fun setLayoutParams() {
            val params =
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                )
            layoutParams = params
        }

        fun initTextViews(vararg options: SocialBarViewOptions) {
            if (areViewsInitialised) {
                return
            }

            for (option in options.take(NO_OF_ACTION_BUTTONS)) {
                val textView = createTextView()
                setTextViewProperties(textView, option)
                textViewsMap[option] = textView
                addView(textView)
            }
            initClickListeners()
            areViewsInitialised = true
        }

        private fun setTextViewProperties(
            textView: TextView,
            option: SocialBarViewOptions,
        ) {
            textView.gravity = Gravity.CENTER
            textView.maxLines = 1
            TextViewCompat.setTextAppearance(textView, R.style.Typography_Docquity_ParagraphRegular)
            textView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                getOptionDrawable(option),
                0,
                0,
            )
        }

        private fun createTextView(): TextView {
            val textView = TextView(context)
            val params =
                LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    1f,
                )
            textView.layoutParams = params
            return textView
        }

        fun setVisibleOptions(vararg options: SocialBarViewOptions) {
            for (option in SocialBarViewOptions.values()) {
                textViewsMap[option]?.visibility = if (options.contains(option)) VISIBLE else GONE
            }
        }

        fun setOptionText(
            option: SocialBarViewOptions,
            text: String,
        ) {
            textViewsMap[option]?.text = text
        }

        fun hideOption(option: SocialBarViewOptions) {
            textViewsMap[option]?.visibility = GONE
        }

        private fun getOptionDrawable(option: SocialBarViewOptions): Int {
            return 1
//            return when (option) {
//                SocialBarViewOptions.LIKE -> R.drawable.ic_like_2_0
//                SocialBarViewOptions.COMMENT -> R.drawable.ic_comment_2_0
//                SocialBarViewOptions.SHARE -> R.drawable.ic_share_2_0
//                SocialBarViewOptions.BOOKMARK -> R.drawable.ic_bookmark_2_0
//                SocialBarViewOptions.REPOST -> R.drawable.ic_repost_2_0
//                SocialBarViewOptions.DOWNLOAD -> R.drawable.ic_download_2_0
//            }
        }

        private var isLiked = false

        fun setLikedState(isLiked: Boolean) {
            this@SocialBarViewV2.isLiked = isLiked
            updateLikeState(isLiked)
        }

        private fun updateLikeState(isLiked: Boolean) {
            val likeTextView = textViewsMap[SocialBarViewOptions.LIKE]
            likeTextView?.apply {
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

        fun updateLikeColorState(
            enabledColor: Int,
            disabledColor: Int,
        ) {
            val likeTextView = textViewsMap[SocialBarViewOptions.LIKE]
            likeTextView?.apply {
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
            textViewsMap[option]?.apply {
                setDrawableTintColor(tint)
                setTextColor(tint)
            }
        }

        private var isBookMarked = false

        fun setBookmarkState(isBookmarked: Boolean) {
            this@SocialBarViewV2.isBookMarked = isBookmarked
            updateBookmarkState(isBookmarked)
        }

        private fun updateBookmarkState(isBookmarked: Boolean) {
            val textView = textViewsMap[SocialBarViewOptions.BOOKMARK]
            textView?.apply {
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

        private var downloadState = DownloadState.IDLE

        fun setDownloadState(state: DownloadState) {
            this@SocialBarViewV2.downloadState = state
            updateDownloadState(state)
        }

        private var isDownloadEnabled = true

        fun setDownloadEnabled(isEnabled: Boolean) {
            this@SocialBarViewV2.isDownloadEnabled = isEnabled
            updateDownloadEnableState(isEnabled)
        }

        private fun updateDownloadEnableState(isEnabled: Boolean) {
            val textView = textViewsMap[SocialBarViewOptions.DOWNLOAD]
            textView?.apply {
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

        private fun updateDownloadState(state: DownloadState) {
            val textView = textViewsMap[SocialBarViewOptions.DOWNLOAD]
            textView?.apply {
                when (state) {
                    DownloadState.IDLE, DownloadState.FAILED -> {
//                        setDrawableWithResourceId(
//                            R.drawable.ic_download_2_0,
//                            getCurrentTextViewDrawablePosition(),
//                        )
                        textColor(R.color.color_primary_black)
                    }

                    DownloadState.LOADING -> {
//                        setDrawableWithResourceId(
//                            R.drawable.ic_download_2_0,
//                            getCurrentTextViewDrawablePosition(),
//                        )
                        R.color.color_secondary_warning_success_green.apply {
                            setDrawableTintColor(ContextCompat.getColor(context, this))
                            textColor(this)
                        }
                    }

                    DownloadState.COMPLETED -> {
//                        setDrawableWithResourceId(
//                            R.drawable.ic_done_2_0,
//                            getCurrentTextViewDrawablePosition(),
//                        )
                        textColor(R.color.color_secondary_deep_green)
                    }
                }
            }
        }

        private fun getCurrentTextViewDrawablePosition() = DrawablePosition.TOP
    }