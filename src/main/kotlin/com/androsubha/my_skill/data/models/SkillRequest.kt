package com.androsubha.my_skill.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SkillRequest(
    val name: String,
    val description: String,
    val proficiency: Int,
    val status: Int// 0->Hide, 1->Visible
)
