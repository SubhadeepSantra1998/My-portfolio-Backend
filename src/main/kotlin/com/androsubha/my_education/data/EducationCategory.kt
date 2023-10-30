package com.androsubha.my_education.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class EducationCategory(
    @BsonId
    val id: String = ObjectId().toString(),
    var title: String,
    var description: String,
    var status: Int,
    val education: List<Education>? = null,
    val createdAt: Long,
    var updatedAt: Long
)
