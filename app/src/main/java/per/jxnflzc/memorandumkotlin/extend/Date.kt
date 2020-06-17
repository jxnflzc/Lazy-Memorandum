package per.jxnflzc.memorandumkotlin.extend

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleString(): String {
    return when {
        isThisYear() -> this.toDateStringWithoutYear() + " " + this.toTimeString()
        else -> this.toDateString() + " " + this.toTimeString()
    }
    //return this.toDateString() + " " + this.toTimeString()
}

fun Date.toDateString(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toDateStringWithoutYear(): String {
    val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toTimeString(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toIdString(): String {
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    var result =  sdf.format(this)
    for (i in 0..3) {
        result += (0..9).random()
    }

    return result
}

fun Date.isYesterday(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DATE, -1)
    return calendar.time.toDateString() == this.toDateString()
}

fun Date.isToday(): Boolean {
    return Date().toDateString() == this.toDateString()
}

fun Date.isThisYear(): Boolean {
    val calendar = Calendar.getInstance()
    val dateCalender = Calendar.getInstance()
    calendar.time = Date()
    dateCalender.time = this
    return calendar.get(Calendar.YEAR) == dateCalender.get(Calendar.YEAR)
}

//用于显示时间信息
fun Date.showDateInfo(): String {
    return when {
        isToday() -> this.toTimeString()
        isYesterday() -> "昨天"
        isThisYear() -> this.toDateStringWithoutYear()
        else -> this.toDateString()
    }
}