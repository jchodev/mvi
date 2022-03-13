package com.jerry.mvi.base


sealed class ViewState<out T> where T : Any? {
    object Initial : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Failure(val errorAny: Any) : ViewState<Nothing>()
}