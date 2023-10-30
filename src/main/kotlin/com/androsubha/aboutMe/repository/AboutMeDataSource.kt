package com.androsubha.aboutMe.repository

import com.androsubha.aboutMe.data.AboutMe
import com.mongodb.client.result.UpdateResult

interface AboutMeDataSource {
    suspend fun insertAboutMe(test: AboutMe): Boolean?
    suspend fun getAboutMeById(id: String) :AboutMe?
    suspend fun updateAboutMeById(aboutMe: AboutMe, id: String): UpdateResult?
    suspend fun deleteAboutMeById(id: String): Boolean?
    suspend fun getAllAboutMe(): List<AboutMe>?
}