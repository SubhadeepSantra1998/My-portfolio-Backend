package com.androsubha.plugins

import com.androsubha.aboutMe.*
import com.androsubha.aboutMe.repository.AboutMeDataSourceImpl
import com.androsubha.my_education.*
import com.androsubha.my_education.repository.MyEduDataSourceImpl
import com.androsubha.my_skill.*
import com.androsubha.my_skill.repository.MySkillDataSourceImpl
import com.androsubha.utils.EndPoint
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    aboutMeDataSource: AboutMeDataSourceImpl,
    mySkillDataSource: MySkillDataSourceImpl,
    myEduDataSource: MyEduDataSourceImpl
) {
    routing {

        // personal information
        route(EndPoint.ABOUT_ME){
            addAboutMe(aboutMeDataSource)
            getAboutMeById(aboutMeDataSource)
            getAllAboutMe(aboutMeDataSource)
            updateAboutMeById(aboutMeDataSource)
            deleteAboutMeById(aboutMeDataSource)
        }

        //skill
        route(EndPoint.SKILL){

            //category routes
            route(EndPoint.CATEGORY){
                addSkillCategory(mySkillDataSource)
                getAllSkillCategories(mySkillDataSource)
                fetchSkillCategoryById(mySkillDataSource)
                updateSkillCategoryById(mySkillDataSource)
                deleteSkillCategoryById(mySkillDataSource)
            }

            //skills
            insertSkillByCategoryId(mySkillDataSource)
            getAllSkillsByCategoryId(mySkillDataSource)
            fetchSkillBySkillId(mySkillDataSource)
            getAllSkills(mySkillDataSource)
            updateSkillBySkillId(mySkillDataSource)
            deleteSkillBySkillId(mySkillDataSource)
        }

        route(EndPoint.EDUCATION){

            route(EndPoint.CATEGORY){
                addEducationCategory(myEduDataSource)
                getAllEducationCategories(myEduDataSource)
                fetchEducationCategoryById(myEduDataSource)
                updateEducationCategoryById(myEduDataSource)
                deleteEducationCategoryById(myEduDataSource)
            }
            insertEducationByCategoryId(myEduDataSource)
            getAllEducationByCategoryId(myEduDataSource)
            getAllEducation(myEduDataSource)
            fetchEducationByEducationId(myEduDataSource)
            updateEducationByEducationId(myEduDataSource)
            deleteEducationByEducationId(myEduDataSource)


        }



    }
}
