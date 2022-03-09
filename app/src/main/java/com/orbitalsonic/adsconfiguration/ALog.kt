package com.orbitalsonic.adsconfiguration

import android.util.Log

object ALog {
    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg)
        }
    }
}