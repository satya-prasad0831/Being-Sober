package com.example.beingsober.data.local

import android.content.Context

class PreferencesManager(
    context: Context
) {

    private val preferences = context.getSharedPreferences(
        "BeingSoberPrefs",
        Context.MODE_PRIVATE
    )

    fun saveHabitType(habitType: String) {

        preferences.edit()
            .putString("habit_type", habitType)
            .putBoolean("setup_complete", true)
            .apply()
    }

    fun isSetupComplete(): Boolean {

        return preferences.getBoolean(
            "setup_complete",
            false
        )
    }

    fun getHabitType(): String? {

        return preferences.getString(
            "habit_type",
            null
        )
    }
}