package com.androsubha.my_skill.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class SkillCategory(
    @BsonId
    val id: String = ObjectId().toString(),
    var title: String,
    var description: String,
    var status: Int,
    val skill: List<Skill>? = null,
    val createdAt: Long,
    var updatedAt: Long
)
