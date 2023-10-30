package com.androsubha.my_skill.repository

import com.androsubha.my_skill.data.Skill
import com.androsubha.my_skill.data.SkillCategory
import com.mongodb.client.result.UpdateResult
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MySkillDataSourceImpl(
    db: CoroutineDatabase
) :MySkillDataSource {

    private val categoryCollection = db.getCollection<SkillCategory>()
    private val skillCollection = db.getCollection<Skill>()


    //category implementation
    override suspend fun insertSkillCategory(skillCategory: SkillCategory): Boolean {
        return categoryCollection.insertOne(skillCategory).wasAcknowledged()
    }

    override suspend fun getAllSkillCategories(): List<SkillCategory> = categoryCollection.find().toList()
    override suspend fun getSkillCategoryById(id: String): SkillCategory? {
        return categoryCollection.findOne(SkillCategory::id eq id)
    }

    override suspend fun updateSkillCategoryByCategoryId(
        skillCategory: SkillCategory,
        categoryId: String
    ): UpdateResult {
        return categoryCollection.updateOne(SkillCategory::id eq categoryId, skillCategory)
    }

    override suspend fun deleteSkillCategoryByCategoryId(categoryId: String): Boolean {
        return categoryCollection.deleteOne(SkillCategory::id eq categoryId).wasAcknowledged()
    }

    override suspend fun insertSkillByCategory(skill: Skill): Boolean {
        return skillCollection.insertOne(skill).wasAcknowledged()
    }

    override suspend fun getAllSkillsByCategoryId(categoryId: String): List<Skill> = skillCollection
        .find(Skill::categoryId eq categoryId)
        .toList()

    override suspend fun getSkillBySkillId(skillId: String): Skill? {
        return skillCollection.findOne(Skill::id eq skillId)
    }

    override suspend fun updateSkillBySkillId(skill: Skill, skillId: String): UpdateResult {
        return skillCollection.updateOne(Skill::id eq skillId, skill)
    }

    override suspend fun deleteSkillBySkillId(skillId: String): Boolean {
        return skillCollection.deleteOne(Skill::id eq skillId).wasAcknowledged()
    }
}