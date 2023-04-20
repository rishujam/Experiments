package com.virinchi.logstore

data class LogEntity(
    val level: String,
    val tag: String,
    val message: String,
    val timestamp: Long
)