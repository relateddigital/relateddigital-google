package com.relateddigital.relateddigital_google.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object CustomDataStoreFactory {

    private val instances = ConcurrentHashMap<String, DataStore<Preferences>>()

    fun create(context: Context, name: String): DataStore<Preferences> {
        val appContext = context.applicationContext ?: context
        return instances.getOrPut(name) {
            PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
                produceFile = { File(appContext.filesDir, "datastore/$name.preferences_pb") }
            )
        }
    }
}
