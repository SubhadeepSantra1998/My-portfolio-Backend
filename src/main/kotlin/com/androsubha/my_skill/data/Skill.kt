package com.androsubha.my_skill.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Skill(
    @BsonId
    val id: String = ObjectId().toString(),
    val categoryId: String,
    var name: String,
    var description: String,
    var proficiency: Int,
    var status: Int,
    val createdAt: Long,
    var updatedAt: Long
)
