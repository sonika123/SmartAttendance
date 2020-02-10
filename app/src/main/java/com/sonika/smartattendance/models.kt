package com.sonika.smartattendance

import com.chibatching.kotpref.KotprefModel

object UserInfo : KotprefModel() {
    var loginStatus by booleanPref(false)
    var userToken by stringPref()
    var userId by stringPref()
    var refreshToken by stringPref()
    var email by stringPref()
}