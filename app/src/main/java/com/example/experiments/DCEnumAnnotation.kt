package com.example.experiments

import androidx.annotation.IntDef
import com.example.experiments.DCConstant.ALIGN_CENTER
import com.example.experiments.DCConstant.ALIGN_LEFT
import com.example.experiments.DCConstant.ALIGN_RIGHT
import com.example.experiments.DCConstant.BACKGROUND_BLACK
import com.example.experiments.DCConstant.BACKGROUND_BLACK_TRANSPARENT_80
import com.example.experiments.DCConstant.BACKGROUND_ERROR
import com.example.experiments.DCConstant.BACKGROUND_GRAY_25
import com.example.experiments.DCConstant.BACKGROUND_GRAY_50
import com.example.experiments.DCConstant.BACKGROUND_GRAY_75
import com.example.experiments.DCConstant.BACKGROUND_GRAY_LIGHT
import com.example.experiments.DCConstant.BACKGROUND_SECONDARY
import com.example.experiments.DCConstant.BACKGROUND_SEPERATOR_COLOR
import com.example.experiments.DCConstant.BACKGROUND_TRANSPARENT
import com.example.experiments.DCConstant.BACKGROUND_TRANSPARENT_BLACK
import com.example.experiments.DCConstant.BACKGROUND_WHITE
import com.example.experiments.DCConstant.BUTTON_MODE_DISABLED
import com.example.experiments.DCConstant.BUTTON_MODE_GRAY_75
import com.example.experiments.DCConstant.BUTTON_MODE_GRAY_FILLED_ENABLE
import com.example.experiments.DCConstant.BUTTON_MODE_PRIMARY
import com.example.experiments.DCConstant.BUTTON_MODE_PRIMARY_BLANK
import com.example.experiments.DCConstant.BUTTON_MODE_PRIMARY_STROKED_ONLY
import com.example.experiments.DCConstant.BUTTON_MODE_SECONDARY
import com.example.experiments.DCConstant.BUTTON_MODE_SECONDARY_BLANK
import com.example.experiments.DCConstant.BUTTON_MODE_SECONDARY_STROKED_ONLY
import com.example.experiments.DCConstant.BUTTON_MODE_STROKE_ONLY
import com.example.experiments.DCConstant.BUTTON_MODE_TEXT_DISABLED
import com.example.experiments.DCConstant.BUTTON_MODE_WHITE_TEXT_STROKE_ONLY
import com.example.experiments.DCConstant.BUTTON_MODE_YELLOW_BLANK
import com.example.experiments.DCConstant.CORNER_ALL
import com.example.experiments.DCConstant.CORNER_ONLY_LEFT
import com.example.experiments.DCConstant.CORNER_ONLY_RIGHT
import com.example.experiments.DCConstant.CORNER_SKIP_BOTTOM_RIGHT
import com.example.experiments.DCConstant.CORNER_SKIP_TOP_LEFT
import com.example.experiments.DCConstant.CORNER_SKIP_TOP_RIGHT
import com.example.experiments.DCConstant.DRAWABLE_BOTTOM
import com.example.experiments.DCConstant.DRAWABLE_LEFT
import com.example.experiments.DCConstant.DRAWABLE_RIGHT
import com.example.experiments.DCConstant.DRAWABLE_TOP
import com.example.experiments.DCConstant.EDIT_MODE_ERROR
import com.example.experiments.DCConstant.EDIT_MODE_LARGE
import com.example.experiments.DCConstant.EDIT_MODE_MEDIUM
import com.example.experiments.DCConstant.EDIT_MODE_SMALL
import com.example.experiments.DCConstant.FLOATING_TYPE_CIRCLE
import com.example.experiments.DCConstant.FLOATING_TYPE_RECTANGLE
import com.example.experiments.DCConstant.GRAVITY_CENTER
import com.example.experiments.DCConstant.GRAVITY_LEFT
import com.example.experiments.DCConstant.GRAVITY_RIGHT
import com.example.experiments.DCConstant.INPUT_TYPE_EMAIL
import com.example.experiments.DCConstant.INPUT_TYPE_NUMBER
import com.example.experiments.DCConstant.INPUT_TYPE_TEXT
import com.example.experiments.DCConstant.REQUESTING_SERVER_4_1
import com.example.experiments.DCConstant.REQUESTING_SERVER_ANALYTICS
import com.example.experiments.DCConstant.REQUESTING_SERVER_BASE
import com.example.experiments.DCConstant.REQUESTING_SERVER_BASE_LATEST
import com.example.experiments.DCConstant.REQUESTING_SERVER_LOCATION
import com.example.experiments.DCConstant.REQUESTING_SERVER_NOTIFY_OLD
import com.example.experiments.DCConstant.REQUESTING_SERVER_PUBLICATION
import com.example.experiments.DCConstant.REQUESTING_SERVER_PUBLICATION_OTHER
import com.example.experiments.DCConstant.REQUESTING_SERVER_SOCKET
import com.example.experiments.DCConstant.REQUEST_TYPE_GET
import com.example.experiments.DCConstant.REQUEST_TYPE_POST
import com.example.experiments.DCConstant.SEPARATOR_MODE_LARGE
import com.example.experiments.DCConstant.SEPARATOR_MODE_MEDIUM
import com.example.experiments.DCConstant.SEPARATOR_MODE_NORMAL
import com.example.experiments.DCConstant.SEPARATOR_MODE_TINY
import com.example.experiments.DCConstant.STATE_SCREEN_ERROR
import com.example.experiments.DCConstant.STATE_SCREEN_EXIT_APP
import com.example.experiments.DCConstant.STATE_SCREEN_LOADING_BOTTOM
import com.example.experiments.DCConstant.STATE_SCREEN_LOADING_TOP
import com.example.experiments.DCConstant.STATE_SCREEN_NODATA
import com.example.experiments.DCConstant.STATE_SCREEN_NONE
import com.example.experiments.DCConstant.STATE_SCREEN_NO_INTERNET_TOAST
import com.example.experiments.DCConstant.STATE_SCREEN_READ_ONLY_DIALOG
import com.example.experiments.DCConstant.STATE_SCREEN_SUCCESS
import com.example.experiments.DCConstant.TAG_MODE_GREY
import com.example.experiments.DCConstant.TAG_MODE_GREY_50
import com.example.experiments.DCConstant.TAG_MODE_GREY_75
import com.example.experiments.DCConstant.TAG_MODE_WHITE
import com.example.experiments.DCConstant.TAG_MODE_YELLOW
import com.example.experiments.DCConstant.TEXT_MODE_HEADING_MEDIUM
import com.example.experiments.DCConstant.TEXT_MODE_PARAGRAPH_BOLD
import com.example.experiments.DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_ERROR
import com.example.experiments.DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_GRAY_75
import com.example.experiments.DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_SECONDARY
import com.example.experiments.DCConstant.TEXT_MODE_PARAGRAPH_MEDIUM_WHITE
import com.example.experiments.DCConstant.TEXT_MODE_PARAGRAPH_REGULAR_GRAY_50
import com.example.experiments.DCConstant.TEXT_MODE_SMALL_MEDIUM_GRAY_75
import com.example.experiments.DCConstant.TEXT_MODE_SUB_HEADING_BOLD
import com.example.experiments.DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_GRAY_75
import com.example.experiments.DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_SECONDARY
import com.example.experiments.DCConstant.TEXT_MODE_SUB_HEADING_MEDIUM_WHITE
import com.example.experiments.DCConstant.TEXT_MODE_SUB_HEADING_REGULAR
import com.example.experiments.DCConstant.TEXT_MODE_SUB_HEADING_REGULAR_GRAY_50
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class DCEnumAnnotation(var state: Int) {

    @IntDef(DCConstant.STATE_SCREEN_LOADING, STATE_SCREEN_SUCCESS, STATE_SCREEN_EXIT_APP, STATE_SCREEN_READ_ONLY_DIALOG, STATE_SCREEN_ERROR,STATE_SCREEN_LOADING_BOTTOM, STATE_SCREEN_LOADING_TOP)
    @Retention(RetentionPolicy.SOURCE)
    annotation class BaseState


    @IntDef(GRAVITY_CENTER, GRAVITY_LEFT, GRAVITY_RIGHT)
    @Retention(RetentionPolicy.SOURCE)
    annotation class GravityMode


    @IntDef(FLOATING_TYPE_CIRCLE, FLOATING_TYPE_RECTANGLE)
    @Retention(RetentionPolicy.SOURCE)
    annotation class FloatingMode

    @IntDef(SEPARATOR_MODE_LARGE, SEPARATOR_MODE_TINY, SEPARATOR_MODE_NORMAL, SEPARATOR_MODE_MEDIUM)
    @Retention(RetentionPolicy.SOURCE)
    annotation class SeparatorMode


    @IntDef(REQUEST_TYPE_GET, REQUEST_TYPE_POST)
    @Retention(RetentionPolicy.SOURCE)
    annotation class RequestType


    @IntDef(TAG_MODE_GREY, TAG_MODE_WHITE, TAG_MODE_GREY_75,TAG_MODE_YELLOW,TAG_MODE_GREY_50)
    @Retention(RetentionPolicy.SOURCE)
    annotation class TagMode

    @IntDef(REQUESTING_SERVER_BASE, REQUESTING_SERVER_BASE_LATEST, REQUESTING_SERVER_PUBLICATION,
            REQUESTING_SERVER_PUBLICATION_OTHER, REQUESTING_SERVER_NOTIFY_OLD, REQUESTING_SERVER_ANALYTICS,
            REQUESTING_SERVER_4_1,REQUESTING_SERVER_SOCKET, REQUESTING_SERVER_LOCATION)
    @Retention(RetentionPolicy.SOURCE)
    annotation class RequestingServer

    @IntDef(BUTTON_MODE_PRIMARY, BUTTON_MODE_SECONDARY, BUTTON_MODE_DISABLED, BUTTON_MODE_PRIMARY_STROKED_ONLY,
            BUTTON_MODE_SECONDARY_STROKED_ONLY, BUTTON_MODE_PRIMARY_BLANK, BUTTON_MODE_SECONDARY_BLANK, BUTTON_MODE_YELLOW_BLANK,
            BUTTON_MODE_STROKE_ONLY, BUTTON_MODE_TEXT_DISABLED, BUTTON_MODE_WHITE_TEXT_STROKE_ONLY, BUTTON_MODE_GRAY_75,BUTTON_MODE_GRAY_FILLED_ENABLE)
    @Retention(RetentionPolicy.SOURCE)
    annotation class ButtonMode

    @IntDef(TEXT_MODE_HEADING_MEDIUM, TEXT_MODE_SUB_HEADING_BOLD, TEXT_MODE_SUB_HEADING_REGULAR, TEXT_MODE_SUB_HEADING_MEDIUM_WHITE,
            TEXT_MODE_SUB_HEADING_MEDIUM_GRAY_75, TEXT_MODE_SUB_HEADING_MEDIUM_SECONDARY, TEXT_MODE_SUB_HEADING_REGULAR_GRAY_50,
            TEXT_MODE_PARAGRAPH_BOLD, TEXT_MODE_PARAGRAPH_REGULAR_GRAY_50, TEXT_MODE_PARAGRAPH_MEDIUM_GRAY_75, TEXT_MODE_PARAGRAPH_MEDIUM_WHITE,
            TEXT_MODE_PARAGRAPH_MEDIUM_SECONDARY, TEXT_MODE_PARAGRAPH_MEDIUM_ERROR, TEXT_MODE_SMALL_MEDIUM_GRAY_75)
    @Retention(RetentionPolicy.SOURCE)
    annotation class TextMode

    @IntDef(EDIT_MODE_LARGE, EDIT_MODE_MEDIUM, EDIT_MODE_SMALL, EDIT_MODE_ERROR)
    @Retention(RetentionPolicy.SOURCE)
    annotation class EditMode


    @IntDef(ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT)
    @Retention(RetentionPolicy.SOURCE)
    annotation class ViewAlignment

    @IntDef(DRAWABLE_LEFT, DRAWABLE_RIGHT, DRAWABLE_TOP, DRAWABLE_BOTTOM)
    @Retention(RetentionPolicy.SOURCE)
    annotation class ViewDrawable


    @IntDef(DCConstant.STATE_SCREEN_ERROR, STATE_SCREEN_NODATA, DCConstant.STATE_SCREEN_LOADING, STATE_SCREEN_NONE, DCConstant.STATE_SCREEN_SUCCESS, STATE_SCREEN_NO_INTERNET_TOAST)
    @Retention(RetentionPolicy.SOURCE)
    annotation class StateMode


    @IntDef(INPUT_TYPE_TEXT, INPUT_TYPE_NUMBER, INPUT_TYPE_EMAIL)
    @Retention(RetentionPolicy.SOURCE)
    annotation class InputType

    @IntDef(CORNER_ALL, CORNER_SKIP_TOP_LEFT, CORNER_SKIP_TOP_RIGHT,CORNER_SKIP_BOTTOM_RIGHT,CORNER_ONLY_RIGHT,CORNER_ONLY_LEFT)
    @Retention(RetentionPolicy.SOURCE)
    annotation class CornerType


    @IntDef(BACKGROUND_WHITE, BACKGROUND_SECONDARY, BACKGROUND_ERROR, BACKGROUND_BLACK, BACKGROUND_GRAY_75, BACKGROUND_GRAY_50, BACKGROUND_GRAY_25, BACKGROUND_GRAY_LIGHT, BACKGROUND_TRANSPARENT_BLACK,
            BACKGROUND_TRANSPARENT,BACKGROUND_BLACK_TRANSPARENT_80,BACKGROUND_SEPERATOR_COLOR)
    @Retention(RetentionPolicy.SOURCE)
    annotation class BackgroundType


    //    public DCEnumAnnotation(@RequestType int state) {
    //        this.state = state;
    //    }
}
