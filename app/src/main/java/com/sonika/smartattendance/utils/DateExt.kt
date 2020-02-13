package com.sonika.smartattendance.utils

import android.text.TextUtils
import java.lang.Exception
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Standard date format we follow will be yyyy-MM-dd
 */

/**
 * get todays date in yyyy-MM-dd format to get txn history
 */
fun getTodaysDate(): String? {
    val cal = Calendar.getInstance()
    return getFormattedDate(cal.time)
}

fun getCurrentTime(): String? {
    val cal = Calendar.getInstance()
    return getFormattedTime(cal.time)
}

/**
 * format date to yyyy/MM/dd
 */
fun getFormattedDate(date: Date?): String? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(date)
}


fun getFormattedTime(date: Date?): String? {
    val dateFormat = SimpleDateFormat("HH:mm:ss a")
    return dateFormat.format(date)
}

/**
 * get from date in yyyy/MM/dd format to get txn history
 */
fun getFromDate(): String? {
    val cal = Calendar.getInstance()
    val fromDate = Date(cal.timeInMillis - 2592000000L)// for 30 days = 30*24*60*60*1000
    return getFormattedDate(fromDate)
}

fun getTxnTime(dateTime: String?): String? {//2017-07-26T12:39:16.69
    val timeFormat = SimpleDateFormat("HH:mm")
    return timeFormat.format(convertTxnResponseStringToDate(dateTime!!))
}


fun convertTxnResponseStringToDate(dateTime: String?): Date? {
    val responseDateFromat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    return responseDateFromat.parse(dateTime)
}

fun getTxnDate(dateTime: String?): String? {
    val outputFormat = "MMMM dd"
    val parsedDateFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
    return parsedDateFormat.format(convertTxnResponseStringToDate(dateTime))
}

fun getTxnDateForRecentTransaction(dateTime: String?): String? {
    val outputFormat = "MMM dd"
    val parsedDateFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
    return parsedDateFormat.format(convertTxnResponseStringToDate(dateTime))
}


fun CompareDates(startDate: String?, endDate: String?): Boolean {
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    val _startDate = sdf.parse(startDate) as Date
    val _endDate = sdf.parse(endDate) as Date
    return _startDate.compareTo(_endDate) <= 0
}

fun Long.toDateString(): String {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy")
    return dateFormat.format(this)
}

fun Long.toDateStringDDMMYYYY(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(this)
}

fun getCurrDay(): Int {
    val cal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd")
    return dateFormat.format(cal.timeInMillis).toInt()
}

fun getCurrMonth(): Int {
    val cal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MM")
    return dateFormat.format(cal.timeInMillis).toInt() - 1
}

fun getCurrYear(): Int {
    val cal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy")
    return dateFormat.format(cal.timeInMillis).toInt()
}

fun getMaxYear(): Int {
    return getCurrYear() + 1
}

fun getDateInCorrectFormat(from: String): String? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    var sourceDate: Date? = null
    try {
        sourceDate = dateFormat.parse(from)
    } catch (e: ParseException) {
        e.printStackTrace();
    }

    val targetFormat = SimpleDateFormat("dd/MM/yyyy");
    return targetFormat.format(sourceDate);
}


fun convertDateFormat(inputPattern: String, outputPattern: String, time: String): String? {
    val inputFormat = SimpleDateFormat(inputPattern)
    val outputFormat = SimpleDateFormat(outputPattern)

    var date: Date? = null
    var str: String? = null

    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return str
}

fun getDoubleDigitFormattedTime(timeInMS: Long): String {
    var second = (timeInMS / 1000) % 60
    var minute = (timeInMS / 1000) / 60
    return minute.toDoubleDigitFormat() + ":" + second.toDoubleDigitFormat()
}

fun Long.toDoubleDigitFormat(): String {
    var decimalFormat = DecimalFormat("00")
    return decimalFormat.format(this)
}


fun splitStringEvery(s: String, interval: Int): Array<String?> {
    val arrayLength = Math.ceil(s.length / interval.toDouble()).toInt()
    val result = arrayOfNulls<String>(arrayLength)

    var j = 0
    val lastIndex = result.size - 1
    for (i in 0 until lastIndex) {
        result[i] = s.substring(j, j + interval)
        j += interval
    } //Add the last bit
    result[lastIndex] = s.substring(j)

    return result
}

fun getConvertedDate(year: String?, month: String?, day: String?): Date {
    val stringDate = "$day/$month/$year"
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.parse(stringDate)
}
