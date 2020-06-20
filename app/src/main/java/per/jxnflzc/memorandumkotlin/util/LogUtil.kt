package per.jxnflzc.memorandumkotlin.util

import android.util.Log

class LogUtil(level: Int) {
    companion object {
        const val VERBOSE = 1

        const val DEBUG = 2

        const val INFO = 3

        const val WARN = 4

        const val ERROR = 5

        const val NONE = 6
    }

    private var level = VERBOSE

    init {
        this.level = level
    }

    fun v(tag: String, msg: String) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }
}