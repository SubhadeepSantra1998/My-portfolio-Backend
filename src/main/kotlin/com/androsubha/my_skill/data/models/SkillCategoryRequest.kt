package com.androsubha.my_skill.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SkillCategoryRequest(
    val title: String,
    val description: String,
    val status: Int // 0->Hide, 1->Visible
)
