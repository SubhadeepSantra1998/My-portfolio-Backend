package com.androsubha.aboutMe.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AboutMeRequest(
    val name: String,
    val designation: String,
    val title: String,
    val description: String,
    val dob: Long,
    val location: String,
    val mail: String,
    val phNo: String,
    val githubUrl: String,
    val linkedinUrl: String,
    val playStoreUrl: String
)
