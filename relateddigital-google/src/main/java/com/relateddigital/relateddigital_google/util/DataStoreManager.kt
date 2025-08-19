package com.relateddigital.relateddigital_google.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.relateddigital.relateddigital_google.constants.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataStoreManager {

    private val Context.dataStore: DataStore<Preferences>
        get() = CustomDataStoreFactory.create(this, name = "push_payloads")

    private val PAYLOADS_KEY = stringPreferencesKey(Constants.PAYLOAD_SP_KEY)
    private val PAYLOADS_BY_ID_KEY = stringPreferencesKey(Constants.PAYLOAD_SP_ID_KEY)
    private val LOGIN_ID_KEY = stringPreferencesKey(Constants.NOTIFICATION_LOGIN_ID_KEY)

    private suspend fun updatePayload(context: Context, key: Preferences.Key<String>, updateAction: (currentPayload: String) -> String) {
        context.dataStore.edit { settings ->
            val currentPayload = settings[key] ?: ""
            val newPayload = updateAction(currentPayload)
            settings[key] = newPayload
        }
    }

    suspend fun updatePayloads(context: Context, updateAction: (currentPayload: String) -> String) {
        updatePayload(context, PAYLOADS_KEY, updateAction)
    }

    suspend fun updatePayloadsById(context: Context, updateAction: (currentPayload: String) -> String) {
        updatePayload(context, PAYLOADS_BY_ID_KEY, updateAction)
    }

    suspend fun getPayloads(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[PAYLOADS_KEY] ?: ""
        }.first()
    }

    suspend fun getPayloadsById(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[PAYLOADS_BY_ID_KEY] ?: ""
        }.first()
    }

    suspend fun getLoginId(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[LOGIN_ID_KEY] ?: ""
        }.first()
    }

    suspend fun saveLoginId(context: Context, loginId: String) {
        context.dataStore.edit { settings ->
            settings[LOGIN_ID_KEY] = loginId
        }
    }
}