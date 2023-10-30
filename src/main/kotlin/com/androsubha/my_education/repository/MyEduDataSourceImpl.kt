package com.androsubha.my_education.repository

import com.androsubha.my_education.data.Education
import com.androsubha.my_education.data.EducationCategory
import com.androsubha.my_skill.data.Skill
import com.androsubha.my_skill.data.SkillCategory
import com.mongodb.client.result.UpdateResult
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MyEduDataSourceImpl(
    db: CoroutineDatabase
) :MyEduDataSource {

    private val categoryCollection = db.getCollection<EducationCategory>()
    private val educationCollection = db.getCollection<Education>()


    override suspend fun insertEducationCategory(educationCategory: EducationCategory): Boolean {
        return categoryCollection.insertOne(educationCategory).wasAcknowledged()
    }

    override suspend fun getAllEducationCategories(): List<EducationCategory> = categoryCollection.find().toList()

    override suspend fun getEducationCategoryById(id: String): EducationCategory? {
        return categoryCollection.findOne(EducationCategory::id eq id)
    }

    override suspend fun updateEducationCategoryByCategoryId(
        educationCategory: EducationCategory,
        categoryId: String
    ): UpdateResult {
        return categoryCollection.updateOne(EducationCategory::id eq categoryId, educationCategory)
    }

    override suspend fun deleteEducationCategoryByCategoryId(categoryId: String): Boolean {
        return categoryCollection.deleteOne(EducationCategory::id eq categoryId).wasAcknowledged()
    }

    override suspend fun insertEducationByCategory(education: Education): Boolean {
        return educationCollection.insertOne(education).wasAcknowledged()
    }

    override suspend fun getAllEducationByCategoryId(categoryId: String): List<Education> = educationCollection
        .find(Education::categoryId eq categoryId)
        .toList()

    override suspend fun getEducationByEducationId(educationId: String): Education? {
        return educationCollection.findOne(Education::id eq educationId)
    }

    override suspend fun updateEducationByEducationId(education: Education, educationId: String): UpdateResult {
        return educationCollection.updateOne(Education::id eq educationId, education)
    }

    override suspend fun deleteEducationByEducationId(educationId: String): Boolean {
        return educationCollection.deleteOne(Education::id eq educationId).wasAcknowledged()
    }
}