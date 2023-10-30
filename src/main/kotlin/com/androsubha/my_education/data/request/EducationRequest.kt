package com.androsubha.my_education.data.request

import kotlinx.serialization.Serializable

@Serializable
data class EducationRequest(
    val institution: String,
    val board: String,
    val location: String,
    val course: String? = null,
    val grade: String,
    val status: Int
)
