package com.androsubha.utils

object EndPoint {

    const val AUTH = "${Constants.API_VERSION}/auth"
    const val USER = "$AUTH/user"
    const val ADMIN = "$AUTH/admin"

    const val SIGNIN = "$ADMIN/signin"
    const val SIGNUP = "$ADMIN/signup"
    const val SECRET = "$ADMIN/secret"
    const val SENDOTP = "$ADMIN/send-otp"
    const val VERIFYOTP = "$ADMIN/verify-otp"
    const val FORGETPASSWORD = "$ADMIN/forget-password"
    const val UPDATEPASSWORD = "$ADMIN/update-password"

    //users
    //const val ALL = "$ADMIN/all"

    const val ADD = "add"
    const val ALL = "all"
    const val UPDATE = "update"
    const val DELETE = "delete"

    const val CATEGORY = "category/"



    //about me
    const val ABOUT_ME = "${Constants.API_VERSION}/about-me/"
    const val SKILL = "${Constants.API_VERSION}/my-skill/"
    const val EDUCATION = "${Constants.API_VERSION}/my-education/"
}