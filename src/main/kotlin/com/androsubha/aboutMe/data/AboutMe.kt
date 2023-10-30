package com.androsubha.aboutMe.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class AboutMe(
    @BsonId
    val id: String = ObjectId().toString(),
    var name: String,
    var designation: String,
    var title: String,
    var description: String,
    var dob: Long,
    var location: String,
    var mail: String,
    var phNo: String,
    var githubUrl: String,
    var linkedinUrl: String,
    var playStoreUrl: String,
    val createdAt: Long,
    var updatedAt: Long
)
