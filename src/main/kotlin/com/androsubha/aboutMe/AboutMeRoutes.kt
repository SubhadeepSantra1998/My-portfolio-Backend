package com.androsubha.aboutMe

import com.androsubha.aboutMe.data.AboutMe
import com.androsubha.aboutMe.data.models.AboutMeRequest
import com.androsubha.aboutMe.repository.AboutMeDataSourceImpl
import com.androsubha.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant


fun Route.addAboutMe(
    aboutMeDataSource: AboutMeDataSourceImpl
) {
    post(EndPoint.ADD) {

        val request = runCatching<AboutMeRequest?> { call.receiveNullable<AboutMeRequest>() }.getOrNull() ?: kotlin.run {
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

        val areFieldsBlank = request.name.isBlank() || request.designation.isBlank() || request.title.isBlank() || request.description.isBlank() ||
                request.dob.toString().isBlank() || request.location.isBlank() || request.mail.isBlank() ||
                request.phNo.isBlank() || request.githubUrl.isBlank() || request.linkedinUrl.isBlank() || request.playStoreUrl.isBlank()
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

        val aboutme = AboutMe(
            name = request.name,
            designation = request.designation,
            title = request.title,
            description = request.description,
            dob = request.dob,
            location = request.location,
            mail = request.mail,
            phNo = request.phNo,
            githubUrl = request.githubUrl,
            linkedinUrl = request.linkedinUrl,
            playStoreUrl = request.playStoreUrl,
            createdAt = instant.toLong(),
            updatedAt = instant.toLong()
        )

        val wasAcknowledged = aboutMeDataSource.insertAboutMe(aboutme)
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
                data = aboutme,
            )
        )
    }
}


fun Route.getAboutMeById(
    aboutMeDataSource: AboutMeDataSourceImpl
) {

    get("{${Constants.MYID}}") {
        val myId = call.parameters[Constants.MYID]
        val aboutMe = myId?.let { it1 -> aboutMeDataSource.getAboutMeById(it1) }
        if (aboutMe == null) {
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
                data = aboutMe,
            )
        )
        return@get
    }

}

fun Route.getAllAboutMe(
    aboutMeDataSource: AboutMeDataSourceImpl
) {

    get(EndPoint.ALL) {
        try {
            val allAboutMe = aboutMeDataSource.getAllAboutMe()
            call.respond(
                HttpStatusCode.OK,
                ResponseMessage(
                    success = true,
                    data = allAboutMe,
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





fun Route.updateAboutMeById(
    aboutMeDataSource: AboutMeDataSourceImpl
) {

    put("${EndPoint.UPDATE}/{${Constants.MYID}}") {
        val id = call.parameters[Constants.MYID]
        val aboutMe = id?.let { it1 -> aboutMeDataSource.getAboutMeById(it1) }
        if (aboutMe == null) {
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

        val request = runCatching<AboutMeRequest?> { call.receiveNullable<AboutMeRequest>() }.getOrNull() ?: kotlin.run {
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

        val areFieldsBlank = request.name.isBlank() || request.designation.isBlank() || request.title.isBlank() || request.description.isBlank() ||
                request.dob.toString().isBlank() || request.location.isBlank() || request.mail.isBlank() ||
                request.phNo.isBlank() || request.githubUrl.isBlank() || request.linkedinUrl.isBlank() || request.playStoreUrl.isBlank()
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

        aboutMe.apply {
            name = request.name
            designation = request.designation
            title = request.title
            description = request.description
            dob = request.dob
            location = request.location
            mail= request.mail
            phNo = request.phNo
            githubUrl = request.githubUrl
            linkedinUrl = request.linkedinUrl
            playStoreUrl = request.playStoreUrl
            updatedAt = instant.toLong()
        }
        val isUpdated = aboutMeDataSource.updateAboutMeById(aboutMe, id)
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


fun Route.deleteAboutMeById(
    aboutMeDataSource: AboutMeDataSourceImpl
){

    delete("${EndPoint.DELETE}/{${Constants.MYID}}") {
        val id = call.parameters[Constants.MYID]
        val aboutMe = id?.let { it1 -> aboutMeDataSource.getAboutMeById(it1) }
        if (aboutMe == null) {
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

        val isDeleted = aboutMeDataSource.deleteAboutMeById(id)
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