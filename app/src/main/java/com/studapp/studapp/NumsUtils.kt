package com.studapp.studapp


fun Double.format(digits: Int = 6) =
    "%.${digits}f".format(this).replace(",", ".").removeTrailingZeros()

fun Double.round(digits: Int = 6) =
    format(digits).toDoubleOrNull() ?: this

fun String.removeTrailingZeros(): String {
    return if (!contains(".")) {
        this
    } else {
        replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
    }
}

fun Int.getTime(): List<Int> {
    val timeSec: Int = this // Json output
    val hours = timeSec / 3600
    var temp = timeSec - hours * 3600
    val mins = temp / 60
    temp -= mins * 60
    val secs = temp

    return listOf(hours, mins, secs)
}

fun getStrTime(time: Int): String {
    return if (time < 10) {
        "0$time"
    } else {
        time.toString()
    }
}