package com.example.nbaapp.domain.util

sealed class DataException(message: String) : Exception(message) {
    class Network(message: String = "Network error") : DataException(message)
    class Server(val code: Int, message: String = "Server error: $code") : DataException(message)
}
