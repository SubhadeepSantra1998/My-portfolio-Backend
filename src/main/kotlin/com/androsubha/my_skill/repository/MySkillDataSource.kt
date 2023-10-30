package com.androsubha.my_skill.repository

import com.androsubha.my_skill.data.Skill
import com.androsubha.my_skill.data.SkillCategory
import com.mongodb.client.result.UpdateResult

interface MySkillDataSource {
    suspend fun insertSkillCategory(skillCategory: SkillCategory): Boolean?
    suspend fun getAllSkillCategories(): List<SkillCategory>?
    suspend fun getSkillCategoryById(id: String): SkillCategory?
    suspend fun updateSkillCategoryByCategoryId(skillCategory: SkillCategory, categoryId: String): UpdateResult?
    suspend fun deleteSkillCategoryByCategoryId(categoryId: String): Boolean?
    suspend fun insertSkillByCategory(skill: Skill): Boolean?
    suspend fun getAllSkillsByCategoryId(categoryId: String): List<Skill>?
    suspend fun getSkillBySkillId(skillId: String): Skill?
    suspend fun updateSkillBySkillId(skill: Skill, skillId: String): UpdateResult?
    suspend fun deleteSkillBySkillId(skillId: String): Boolean?
}