package com.example.nbaapp.ui.util

import androidx.annotation.StringRes

/**
 * Represents the state of a UI component that loads data.
 * This sealed class provides a type-safe way to handle loading, success, and error states.
 */
sealed class UiState<out T> {
    data object Initial : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(
        @StringRes val messageResId: Int,
        val formatArgs: List<Any> = emptyList()
    ) : UiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error
}
