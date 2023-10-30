package com.androsubha.my_education.data.request

import kotlinx.serialization.Serializable

@Serializable
data class EducationCategoryRequest(
    val title: String,
    val description: String,
    val status: Int
)
