package com.example.data

import android.content.Context
import android.content.SharedPreferences

class ApiKeyStorage(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("voxora_settings", Context.MODE_PRIVATE)

    fun saveApiKey(apiKey: String) {
        prefs.edit().putString("api_key", apiKey).apply()
    }

    fun getApiKey(): String {
        return prefs.getString("api_key", "") ?: ""
    }

    fun isRealMode(): Boolean {
        val key = getApiKey()
        return key.isNotEmpty()
    }
}
