package com.relateddigital.relateddigital_google.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.relateddigital.relateddigital_google.constants.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Jetpack DataStore işlemlerini yöneten merkezi sınıf.
 * İki ayrı dosyayı yönetir: 'push_payloads' ve 'notification_settings'.
 */
object DataStoreManager {
    
    private lateinit var payloadsDataStore: DataStore<Preferences>
    private lateinit var settingsDataStore: DataStore<Preferences>
    
    fun createDataStore(context: Context) {
        if (!this::payloadsDataStore.isInitialized && !this::settingsDataStore.isInitialized) {
            payloadsDataStore = context.createDataStore("push_payloads")
            settingsDataStore = context.createDataStore("notification_settings")
        }
    }

    
    /**
     * DataStore'da kullanılacak tüm anahtarları merkezi bir yerde topluyoruz.
     */
    object Keys {
        // Payload Anahtarları
        val PAYLOADS = stringPreferencesKey(Constants.PAYLOAD_SP_KEY)
        val PAYLOADS_BY_ID = stringPreferencesKey(Constants.PAYLOAD_SP_ID_KEY)
        val LOGIN_ID = stringPreferencesKey(Constants.NOTIFICATION_LOGIN_ID_KEY)

        // Ayar Anahtarları
        val RELATED_DIGITAL_MODEL = stringPreferencesKey(Constants.RELATED_DIGITAL_MODEL_KEY)
        val NOTIFICATION_SMALL_ICON = intPreferencesKey(Constants.NOTIFICATION_TRANSPARENT_SMALL_ICON)
        val NOTIFICATION_SMALL_ICON_DARK_MODE = intPreferencesKey(Constants.NOTIFICATION_TRANSPARENT_SMALL_ICON_DARK_MODE)
        val NOTIFICATION_USE_LARGE_ICON = booleanPreferencesKey(Constants.NOTIFICATION_USE_LARGE_ICON)
        val NOTIFICATION_LARGE_ICON = intPreferencesKey(Constants.NOTIFICATION_LARGE_ICON)
        val NOTIFICATION_LARGE_ICON_DARK_MODE = intPreferencesKey(Constants.NOTIFICATION_LARGE_ICON_DARK_MODE)
        val NOTIFICATION_INTENT = stringPreferencesKey(Constants.INTENT_NAME)
        val NOTIFICATION_CHANNEL_NAME = stringPreferencesKey(Constants.CHANNEL_NAME)
        val NOTIFICATION_COLOR = stringPreferencesKey(Constants.NOTIFICATION_COLOR)
        val NOTIFICATION_CHANNEL_ID = stringPreferencesKey(Constants.NOTIFICATION_CHANNEL_ID_KEY)
        val NOTIFICATION_PRIORITY = stringPreferencesKey(Constants.NOTIFICATION_PRIORITY_KEY)
    }

    // --- Push Payload Yönetimi ---

    /**
     * Ana payload listesini atomik olarak günceller.
     */
    suspend fun updatePayloads(updateAction: (currentPayload: String) -> String) {
        payloadsDataStore.edit { settings ->
            val currentPayload = settings[Keys.PAYLOADS] ?: ""
            val newPayload = updateAction(currentPayload)
            settings[Keys.PAYLOADS] = newPayload
        }
    }

    /**
     * Kullanıcı ID'sine özel payload listesini atomik olarak günceller.
     */
    suspend fun updatePayloadsById(updateAction: (currentPayload: String) -> String) {
        payloadsDataStore.edit { settings ->
            val currentPayload = settings[Keys.PAYLOADS_BY_ID] ?: ""
            val newPayload = updateAction(currentPayload)
            settings[Keys.PAYLOADS_BY_ID] = newPayload
        }
    }

    // ... Diğer payload okuma/yazma fonksiyonları (getPayloads, getLoginId vb.)
    // Not: Bu fonksiyonlar artık payloadsDataStore kullanmalı.
    suspend fun getPayloads(): String {
        return payloadsDataStore.data.map { it[Keys.PAYLOADS] ?: "" }.first()
    }

    suspend fun getPayloadsById(): String {
        return payloadsDataStore.data.map { it[Keys.PAYLOADS_BY_ID] ?: "" }.first()
    }

    suspend fun getLoginId(): String {
        return payloadsDataStore.data.map { it[Keys.LOGIN_ID] ?: "" }.first()
    }

    suspend fun saveLoginId(loginId: String) {
        payloadsDataStore.edit { it[Keys.LOGIN_ID] = loginId }
    }


    // --- Bildirim Ayarları Yönetimi ---

    /**
     * Verilen tüm bildirim ayarlarını 'notification_settings' dosyasına tek bir atomik işlemde kaydeder.
     */
    suspend fun saveNotificationPreferences(
        modelJson: String,
        smallIcon: Int?,
        smallIconDarkMode: Int?,
        useLargeIcon: Boolean,
        largeIcon: Int?,
        largeIconDarkMode: Int?,
        pushIntent: String?,
        channelName: String?,
        color: String?,
        priority: String
    ) {
        settingsDataStore.edit { settings ->
            settings[Keys.RELATED_DIGITAL_MODEL] = modelJson
            smallIcon?.let { settings[Keys.NOTIFICATION_SMALL_ICON] = it }
            smallIconDarkMode?.let { settings[Keys.NOTIFICATION_SMALL_ICON_DARK_MODE] = it }
            settings[Keys.NOTIFICATION_USE_LARGE_ICON] = useLargeIcon
            largeIcon?.let { settings[Keys.NOTIFICATION_LARGE_ICON] = it }
            largeIconDarkMode?.let { settings[Keys.NOTIFICATION_LARGE_ICON_DARK_MODE] = it }
            pushIntent?.let { settings[Keys.NOTIFICATION_INTENT] = it }
            channelName?.let { settings[Keys.NOTIFICATION_CHANNEL_NAME] = it }
            color?.let { settings[Keys.NOTIFICATION_COLOR] = it }
            settings[Keys.NOTIFICATION_PRIORITY] = priority
        }
    }

    /**
     * Ayarlar dosyasından belirtilen anahtara ait string veriyi okur.
     */
    suspend fun readStringFromSettings(key: Preferences.Key<String>): String {
        return settingsDataStore.data.map { it[key] ?: "" }.first()
    }

    /**
     * Ayarlar dosyasına belirtilen anahtara ait string veriyi yazar.
     */
    suspend fun writeStringToSettings(key: Preferences.Key<String>, value: String) {
        settingsDataStore.edit { it[key] = value }
    }
}