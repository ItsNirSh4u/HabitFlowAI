package com.habitflow.app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "habitflow_prefs")

class HabitFlowApp : Application() {
    companion object {
        lateinit var instance: HabitFlowApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

