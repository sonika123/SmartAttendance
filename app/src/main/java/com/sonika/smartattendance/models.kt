package com.sonika.smartattendance

import com.chibatching.kotpref.KotprefModel
import com.google.gson.annotations.SerializedName

object UserInfo : KotprefModel() {
    var loginStatus by booleanPref(false)
    var userToken by stringPref()
    var userId by stringPref()
    var refreshToken by stringPref()
    var email by stringPref()
}

data class AttendanceRecordResponse(
    @SerializedName("CheckInTime") var checkInTime: String?,
    @SerializedName("CheckOutTime") var checkOutTime: String?
)