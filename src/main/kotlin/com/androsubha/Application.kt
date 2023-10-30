package com.androsubha

import com.androsubha.db.DbConnection
import com.androsubha.plugins.*
import com.androsubha.aboutMe.repository.AboutMeDataSourceImpl
import com.androsubha.my_education.repository.MyEduDataSourceImpl
import com.androsubha.my_skill.repository.MySkillDataSourceImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    //io.ktor.server.netty.EngineMain.main(args)
    embeddedServer(Netty, port = 8080, host = "192.168.0.114", module = Application::module).start(wait = true)
}

fun Application.module() {

    val database = DbConnection.connect()

    val aboutMeDataSource = AboutMeDataSourceImpl(database)
    val mySkillDataSource = MySkillDataSourceImpl(database)
    val myEduDataSource = MyEduDataSourceImpl(database)

    configureSerialization()
    configureSecurity()
    configureRouting(aboutMeDataSource, mySkillDataSource, myEduDataSource)
    configureMonitoring()
}
