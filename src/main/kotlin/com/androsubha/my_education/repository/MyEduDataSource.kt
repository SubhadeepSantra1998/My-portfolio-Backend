package com.androsubha.my_education.repository

import com.androsubha.my_education.data.Education
import com.androsubha.my_education.data.EducationCategory
import com.mongodb.client.result.UpdateResult

interface MyEduDataSource {
    suspend fun insertEducationCategory(educationCategory: EducationCategory): Boolean?
    suspend fun getAllEducationCategories(): List<EducationCategory>?
    suspend fun getEducationCategoryById(id: String) : EducationCategory?
    suspend fun updateEducationCategoryByCategoryId(educationCategory: EducationCategory, categoryId: String): UpdateResult?
    suspend fun deleteEducationCategoryByCategoryId(categoryId: String): Boolean?
    suspend fun insertEducationByCategory(education: Education): Boolean?
    suspend fun getAllEducationByCategoryId(categoryId: String): List<Education>?
    suspend fun getEducationByEducationId(educationId: String) : Education?
    suspend fun updateEducationByEducationId(education: Education, educationId: String): UpdateResult?
    suspend fun deleteEducationByEducationId(educationId: String): Boolean?

}