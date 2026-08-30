// location: app/src/main/java/com/example/data/ApiKeyStorage.kt
package com.example.data

import android.content.Context
import android.content.SharedPreferences

class ApiKeyStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("voxoai_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GEMINI_API = "gemini_api_key"
    }

    fun getApiKey(): String {
        return prefs.getString(KEY_GEMINI_API, "") ?: ""
    }

    fun saveApiKey(key: String) {
        prefs.edit().putString(KEY_GEMINI_API, key.trim()).apply()
    }

    fun isRealMode(): Boolean {
        return getApiKey().isNotBlank()
    }
}
