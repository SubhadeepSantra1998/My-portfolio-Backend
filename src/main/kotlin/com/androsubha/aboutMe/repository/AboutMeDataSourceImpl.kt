package com.androsubha.aboutMe.repository

import com.androsubha.aboutMe.data.AboutMe
import com.mongodb.client.result.UpdateResult
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class AboutMeDataSourceImpl(
    db: CoroutineDatabase
) :AboutMeDataSource {

    private val aboutMeCollection = db.getCollection<AboutMe>()

    override suspend fun insertAboutMe(aboutMe: AboutMe): Boolean {
        return aboutMeCollection.insertOne(aboutMe).wasAcknowledged()
    }

    override suspend fun getAboutMeById(id: String): AboutMe? {
        return aboutMeCollection.findOne(AboutMe::id eq id)
    }

    override suspend fun updateAboutMeById(aboutMe: AboutMe, id: String): UpdateResult {
        return aboutMeCollection.updateOne(AboutMe::id eq id, aboutMe)
    }

    override suspend fun deleteAboutMeById(id: String): Boolean {
        return aboutMeCollection.deleteOne(AboutMe::id eq id).wasAcknowledged()
    }

    override suspend fun getAllAboutMe(): List<AboutMe> = aboutMeCollection.find().toList()


}