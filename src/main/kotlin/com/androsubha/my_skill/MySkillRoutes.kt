package com.androsubha.my_skill

import com.androsubha.my_skill.data.Skill
import com.androsubha.my_skill.data.SkillCategory
import com.androsubha.my_skill.data.models.SkillCategoryRequest
import com.androsubha.my_skill.data.models.SkillRequest
import com.androsubha.my_skill.repository.MySkillDataSourceImpl
import com.androsubha.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant


// my skill categories route
fun Route.addSkillCategory(
    mySkillDataSource: MySkillDataSourceImpl
) {

    post(EndPoint.ADD) {

        val request = runCatching<SkillCategoryRequest?> { call.receiveNullable<SkillCategoryRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@post
        }

        val areFieldsBlank = request.title.isBlank() || request.description.isBlank() || request.status.toString().isBlank()
        if (areFieldsBlank) {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.MISSING_FIELDS,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@post
        }

        val instant: Instant = generateTimestamp()

        val skillCategory = SkillCategory(
            title = request.title,
            description = request.description,
            status = request.status,
            createdAt = instant.toLong(),
            updatedAt = instant.toLong()
        )

        val wasAcknowledged = mySkillDataSource.insertSkillCategory(skillCategory)
        if (!wasAcknowledged) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@post
        }


        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                message = Message.INSERTED_SUCCESSFULLY,
                data = skillCategory,
            )
        )
    }

}

fun Route.getAllSkillCategories(
    mySkillDataSource: MySkillDataSourceImpl
) {

    get(EndPoint.ALL) {
        try {
            val skillCategories = mySkillDataSource.getAllSkillCategories()
            call.respond(
                HttpStatusCode.OK,
                ResponseMessage(
                    success = true,
                    data = skillCategories,
                )
            )
        }
        catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ResponseMessage(
                    success = false,
                    message = "Error retrieving: ${e.localizedMessage}",
                    errorCode = HttpStatusCode.InternalServerError.value,
                    data = null,
                )
            )
        }
    }

}

fun Route.fetchSkillCategoryById(
    mySkillDataSource: MySkillDataSourceImpl
) {

    get("{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val skillCategory = categoryId?.let { it1 -> mySkillDataSource.getSkillCategoryById(it1) }
        if (skillCategory == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@get
        }

        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                data = skillCategory,
            )
        )
        return@get
    }

}

fun Route.updateSkillCategoryById(
    mySkillDataSource: MySkillDataSourceImpl
) {

    put("${EndPoint.UPDATE}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val skillCategory = categoryId?.let { it1 -> mySkillDataSource.getSkillCategoryById(it1) }
        if (skillCategory == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@put
        }

        val request = runCatching<SkillCategoryRequest?> { call.receiveNullable<SkillCategoryRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@put
        }

        val areFieldsBlank = request.title.isBlank() || request.description.isBlank() || request.status.toString().isBlank()
        if (areFieldsBlank) {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.MISSING_FIELDS,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@put
        }

        val instant: Instant = generateTimestamp()

        skillCategory.apply {
            title = request.title
            description = request.description
            status = request.status
            updatedAt = instant.toLong()
        }
        val isUpdated = mySkillDataSource.updateSkillCategoryByCategoryId(skillCategory, categoryId)
        if (!isUpdated.wasAcknowledged()) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@put
        }

        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                message = Message.UPDATED_SUCCESSFULLY,
                data = null
            )
        )
    }

}

fun Route.deleteSkillCategoryById(
    mySkillDataSource: MySkillDataSourceImpl
) {

    delete("${EndPoint.DELETE}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val skillCategory = categoryId?.let { it1 -> mySkillDataSource.getSkillCategoryById(it1) }
        if (skillCategory == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@delete
        }

        val isDeleted = mySkillDataSource.deleteSkillCategoryByCategoryId(categoryId)
        if (!isDeleted) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@delete
        }

        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                message = Message.DELETED_SUCCESSFULLY,
                errorCode = HttpStatusCode.OK.value,
                data = null,
            )
        )
        return@delete
    }

}




//skills by category

fun Route.insertSkillByCategoryId(
    mySkillDataSource: MySkillDataSourceImpl
) {

    post("${EndPoint.ADD}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val skillCategory = categoryId?.let { it1 -> mySkillDataSource.getSkillCategoryById(it1) }
        if (skillCategory == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@post
        }

        val request = runCatching<SkillRequest?> { call.receiveNullable<SkillRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@post
        }

        val areFieldsBlank = request.name.isBlank() || request.description.isBlank() || request.proficiency.toString().isBlank() || request.status.toString().isBlank()
        if (areFieldsBlank) {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.MISSING_FIELDS,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@post
        }

        val instant: Instant = generateTimestamp()

        val skill = Skill(
            categoryId = categoryId,
            name = request.name,
            description = request.description,
            proficiency = request.proficiency,
            status = request.status,
            createdAt = instant.toLong(),
            updatedAt = instant.toLong()
        )

        val wasAcknowledged = mySkillDataSource.insertSkillByCategory(skill)
        if (!wasAcknowledged) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@post
        }


        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                message = Message.INSERTED_SUCCESSFULLY,
                data = skill,
            )
        )

    }

}


fun Route.getAllSkillsByCategoryId(
    mySkillDataSource: MySkillDataSourceImpl
) {

    get("${EndPoint.ALL}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val skillCategory = categoryId?.let { it1 -> mySkillDataSource.getSkillCategoryById(it1) }
        if (skillCategory == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@get
        }



        try {
            val skills = mySkillDataSource.getAllSkillsByCategoryId(categoryId)
            call.respond(
                HttpStatusCode.OK,
                ResponseMessage(
                    success = true,
                    data = skills,
                )
            )
        }
        catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ResponseMessage(
                    success = false,
                    message = "Error retrieving: ${e.localizedMessage}",
                    errorCode = HttpStatusCode.InternalServerError.value,
                    data = null,
                )
            )
        }
    }

}

fun Route.fetchSkillBySkillId(
    mySkillDataSource: MySkillDataSourceImpl
) {

    get("{${Constants.SKILLID}}") {
        val skillId = call.parameters[Constants.SKILLID]
        val skill = skillId?.let { it1 -> mySkillDataSource.getSkillBySkillId(it1) }
        if (skill == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@get
        }

        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                data = skill,
            )
        )
        return@get
    }

}


fun Route.getAllSkills(
    mySkillDataSource: MySkillDataSourceImpl
){

    get(EndPoint.ALL) {
        try {
            val skillCategories = mySkillDataSource.getAllSkillCategories()
            val skillsWithCategories = skillCategories.map { category ->
                val skills = mySkillDataSource.getAllSkillsByCategoryId(category.id)
                category.copy(skill = skills)
            }


            call.respond(
                HttpStatusCode.OK,
                ResponseMessage(
                    success = true,
                    data = skillsWithCategories,
                )
            )
        }
        catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ResponseMessage(
                    success = false,
                    message = "Error retrieving: ${e.localizedMessage}",
                    errorCode = HttpStatusCode.InternalServerError.value,
                    data = null,
                )
            )
        }
    }

}


fun Route.updateSkillBySkillId(
    mySkillDataSource: MySkillDataSourceImpl
) {

    put("${EndPoint.UPDATE}/{${Constants.SKILLID}}") {
        val skillId = call.parameters[Constants.SKILLID]
        val skill = skillId?.let { it1 -> mySkillDataSource.getSkillBySkillId(it1) }
        if (skill == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@put
        }

        val request = runCatching<SkillRequest?> { call.receiveNullable<SkillRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@put
        }

        val areFieldsBlank = request.name.isBlank() || request.description.isBlank() || request.proficiency.toString().isBlank() || request.status.toString().isBlank()
        if (areFieldsBlank) {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessage(
                    success = false,
                    message = Message.MISSING_FIELDS,
                    errorCode = HttpStatusCode.BadRequest.value,
                    data = null,
                )
            )
            return@put
        }

        val instant: Instant = generateTimestamp()

        skill.apply {
            name = request.name
            description = request.description
            proficiency = request.proficiency
            status = request.status
            updatedAt = instant.toLong()
        }

        val isUpdated = mySkillDataSource.updateSkillBySkillId(skill, skillId)
        if (!isUpdated.wasAcknowledged()) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@put
        }

        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                message = Message.UPDATED_SUCCESSFULLY,
                data = null
            )
        )
    }

}

fun Route.deleteSkillBySkillId(
    mySkillDataSource: MySkillDataSourceImpl
) {

    delete("${EndPoint.DELETE}/{${Constants.SKILLID}}") {
        val skillId = call.parameters[Constants.SKILLID]
        val skill = skillId?.let { it1 -> mySkillDataSource.getSkillBySkillId(it1) }
        if (skill == null) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.RECORD_NOT_EXISTS,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@delete
        }

        val isDeleted = mySkillDataSource.deleteSkillBySkillId(skillId)
        if (!isDeleted) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseMessage(
                    success = false,
                    message = Message.SOME_PROBLEM_OCCURRED,
                    errorCode = HttpStatusCode.Conflict.value,
                    data = null,
                )
            )
            return@delete
        }

        call.respond(
            HttpStatusCode.OK,
            ResponseMessage(
                success = true,
                message = Message.DELETED_SUCCESSFULLY,
                errorCode = HttpStatusCode.OK.value,
                data = null,
            )
        )
        return@delete

    }

}