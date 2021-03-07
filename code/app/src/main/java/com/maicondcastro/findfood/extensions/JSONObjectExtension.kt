package com.maicondcastro.findfood.extensions

import org.json.JSONObject
import java.lang.Exception

fun JSONObject.getStringOrNull(name: String): String? {
    return try {
        getString(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getDoubleOrNull(name: String): Double? {
    return try {
        getDouble(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getIntOrNull(name: String): Int? {
    return try {
        getInt(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getJSONObjectOrNull(name: String): JSONObject? {
    return try {
        getJSONObject(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getBooleanOrNull(name: String): Boolean? {
    return try {
        getBoolean(name)
    } catch (_: Exception) {
        null
    }
}