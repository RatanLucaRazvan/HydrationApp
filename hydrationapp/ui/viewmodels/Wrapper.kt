package com.example.hydrationapp.ui.viewmodels

enum class Status {
    SUCCESS, ERROR, LOADING
}

data class UIResource<out T>(
    val status: Status = Status.LOADING,
    val data: T? = null,
    val errMessage: String? = null
)