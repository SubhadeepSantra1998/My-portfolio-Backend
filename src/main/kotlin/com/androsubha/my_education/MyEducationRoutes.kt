package com.androsubha.my_education

import com.androsubha.my_education.data.Education
import com.androsubha.my_education.data.EducationCategory
import com.androsubha.my_education.data.request.EducationCategoryRequest
import com.androsubha.my_education.data.request.EducationRequest
import com.androsubha.my_education.repository.MyEduDataSourceImpl
import com.androsubha.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant

fun Route.addEducationCategory(
    myEduDataSource: MyEduDataSourceImpl
) {

    post(EndPoint.ADD) {

        val request = runCatching<EducationCategoryRequest?> { call.receiveNullable<EducationCategoryRequest>() }.getOrNull() ?: kotlin.run {
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

        val educationCategory = EducationCategory(
            title = request.title,
            description = request.description,
            status = request.status,
            createdAt = instant.toLong(),
            updatedAt = instant.toLong()
        )

        val wasAcknowledged = myEduDataSource.insertEducationCategory(educationCategory)
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
                data = educationCategory,
            )
        )
    }

}


 fun Route.getAllEducationCategories(
    myEduDataSource: MyEduDataSourceImpl
) {

     get(EndPoint.ALL) {
         try {
             val educationCategories = myEduDataSource.getAllEducationCategories()
             call.respond(
                 HttpStatusCode.OK,
                 ResponseMessage(
                     success = true,
                     data = educationCategories,
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


fun Route.fetchEducationCategoryById(
    myEduDataSource: MyEduDataSourceImpl
){

    get("{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val educationCategory = categoryId?.let { it1 -> myEduDataSource.getEducationCategoryById(it1) }
        if (educationCategory == null) {
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
                data = educationCategory,
            )
        )
        return@get
    }

}


fun Route.updateEducationCategoryById(
    myEduDataSource: MyEduDataSourceImpl
){

    put("${EndPoint.UPDATE}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val educationCategory = categoryId?.let { it1 -> myEduDataSource.getEducationCategoryById(it1) }
        if (educationCategory == null) {
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

        val request = runCatching<EducationCategoryRequest?> { call.receiveNullable<EducationCategoryRequest>() }.getOrNull() ?: kotlin.run {
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

        educationCategory.apply {
            title = request.title
            description = request.description
            status = request.status
            updatedAt = instant.toLong()
        }
        val isUpdated = myEduDataSource.updateEducationCategoryByCategoryId(educationCategory, categoryId)
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


fun Route.deleteEducationCategoryById(
    myEduDataSource: MyEduDataSourceImpl
){

    delete("${EndPoint.DELETE}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val educationCategory = categoryId?.let { it1 -> myEduDataSource.getEducationCategoryById(it1) }
        if (educationCategory == null) {
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

        val isDeleted = myEduDataSource.deleteEducationCategoryByCategoryId(categoryId)
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












fun Route.insertEducationByCategoryId(
    myEduDataSource: MyEduDataSourceImpl
){

    post("${EndPoint.ADD}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val educationCategory = categoryId?.let { it1 -> myEduDataSource.getEducationCategoryById(it1) }
        if (educationCategory == null) {
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

        val request = runCatching<EducationRequest?> { call.receiveNullable<EducationRequest>() }.getOrNull() ?: kotlin.run {
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

        val areFieldsBlank = request.institution.isBlank() || request.board.isBlank() || request.location.isBlank() ||
                request.grade.isBlank() || request.status.toString().isBlank()
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

        val education = Education(
            categoryId = categoryId,
            institution = request.institution,
            board = request.board,
            location = request.location,
            grade = request.grade,
            course = request.course,
            status = request.status,
            createdAt = instant.toLong(),
            updatedAt = instant.toLong()
        )

        val wasAcknowledged = myEduDataSource.insertEducationByCategory(education)
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
                data = education,
            )
        )

    }

}


fun Route.getAllEducationByCategoryId(
    myEduDataSource: MyEduDataSourceImpl
){

    get("${EndPoint.ALL}/{${Constants.CATEGORYID}}") {
        val categoryId = call.parameters[Constants.CATEGORYID]
        val educationCategory = categoryId?.let { it1 -> myEduDataSource.getEducationCategoryById(it1) }
        if (educationCategory == null) {
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
            val educations = myEduDataSource.getAllEducationByCategoryId(categoryId)
            call.respond(
                HttpStatusCode.OK,
                ResponseMessage(
                    success = true,
                    data = educations,
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


fun Route.getAllEducation(
    myEduDataSource: MyEduDataSourceImpl
){

    get(EndPoint.ALL) {
        try {
            val educationCategories = myEduDataSource.getAllEducationCategories()
            val educationsWithCategories = educationCategories.map { category ->
                val educations = myEduDataSource.getAllEducationByCategoryId(category.id)
                category.copy(education = educations)
            }


            call.respond(
                HttpStatusCode.OK,
                ResponseMessage(
                    success = true,
                    data = educationsWithCategories,
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


fun Route.fetchEducationByEducationId(
    myEduDataSource: MyEduDataSourceImpl
){

    get("{${Constants.EDUCATIONID}}") {
        val educationId = call.parameters[Constants.EDUCATIONID]
        val education = educationId?.let { it1 -> myEduDataSource.getEducationByEducationId(it1) }
        if (education == null) {
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
                data = education,
            )
        )
        return@get
    }

}


fun Route.updateEducationByEducationId(
    myEduDataSource: MyEduDataSourceImpl
){

    put("${EndPoint.UPDATE}/{${Constants.EDUCATIONID}}") {
        val educationId = call.parameters[Constants.EDUCATIONID]
        val education = educationId?.let { it1 -> myEduDataSource.getEducationByEducationId(it1) }
        if (education == null) {
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

        val request = runCatching<EducationRequest?> { call.receiveNullable<EducationRequest>() }.getOrNull() ?: kotlin.run {
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

        val areFieldsBlank = request.institution.isBlank() || request.board.isBlank() || request.location.isBlank() ||
                request.grade.isBlank() || request.status.toString().isBlank()
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

        education.apply {
            institution = request.institution
            board = request.board
            location = request.location
            grade = request.grade
            course = request.course
            status = request.status
            updatedAt = instant.toLong()
        }

        val isUpdated = myEduDataSource.updateEducationByEducationId(education, educationId)
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


fun Route.deleteEducationByEducationId(
    myEduDataSource: MyEduDataSourceImpl
){

    delete("${EndPoint.DELETE}/{${Constants.EDUCATIONID}}") {
        val educationId = call.parameters[Constants.EDUCATIONID]
        val education = educationId?.let { it1 -> myEduDataSource.getEducationByEducationId(it1) }
        if (education == null) {
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

        val isDeleted = myEduDataSource.deleteEducationByEducationId(educationId)
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





