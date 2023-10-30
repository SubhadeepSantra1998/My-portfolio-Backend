package com.androsubha.my_education.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Education(
    @BsonId
    val id: String = ObjectId().toString(),
    val categoryId: String,
    var institution: String,
    var board: String,
    var location: String,
    var course: String? = null,
    var grade: String,
    var status: Int,
    val createdAt: Long,
    var updatedAt: Long
)