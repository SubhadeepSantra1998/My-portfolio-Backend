package com.androsubha.utils

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage<T>(
    val success: Boolean,
    val message: String? = null,
    val errorCode: Int? = null,
    val data: T,
)