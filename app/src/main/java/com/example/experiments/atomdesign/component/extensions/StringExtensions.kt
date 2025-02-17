package com.virinchi.atomdesign.component.extensions

fun String.isPurelyEmpty(): Boolean {
    return isNullOrEmpty() || trim().isEmpty() || isNullOrBlank() ||
        equals(
            "null",
            ignoreCase = true,
        )
}