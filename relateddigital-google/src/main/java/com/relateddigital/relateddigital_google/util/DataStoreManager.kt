package com.relateddigital.relateddigital_google.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.relateddigital.relateddigital_google.constants.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Jetpack DataStore işlemlerini yöneten merkezi sınıf.
 * SharedPreferences yerine kullanılır ve asenkron, güvenli veri saklama sağlar.
 */
object DataStoreManager {

    // DataStore'u "push_payloads" adıyla singleton olarak oluşturuyoruz.
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "push_payloads")

    // SharedPreferences'teki anahtarlara karşılık gelen DataStore anahtarlarını tanımlıyoruz.
    private val PAYLOADS_KEY = stringPreferencesKey(Constants.PAYLOAD_SP_KEY)
    private val PAYLOADS_BY_ID_KEY = stringPreferencesKey(Constants.PAYLOAD_SP_ID_KEY)
    private val LOGIN_ID_KEY = stringPreferencesKey(Constants.NOTIFICATION_LOGIN_ID_KEY)

    /**
     * Belirtilen anahtara ait payload'u atomik (transactional) olarak günceller.
     * Bu yöntem, veri bozulmalarını önler.
     * @param context Context.
     * @param key Güncellenecek veri anahtarı.
     * @param updateAction Mevcut payload'u alıp yeni payload'u döndüren lambda fonksiyonu.
     */
    private suspend fun updatePayload(context: Context, key: Preferences.Key<String>, updateAction: (currentPayload: String) -> String) {
        context.dataStore.edit { settings ->
            val currentPayload = settings[key] ?: ""
            val newPayload = updateAction(currentPayload)
            settings[key] = newPayload
        }
    }

    /**
     * Ana (genel) payload listesini günceller.
     */
    suspend fun updatePayloads(context: Context, updateAction: (currentPayload: String) -> String) {
        updatePayload(context, PAYLOADS_KEY, updateAction)
    }

    /**
     * Kullanıcı ID'sine özel payload listesini günceller.
     */
    suspend fun updatePayloadsById(context: Context, updateAction: (currentPayload: String) -> String) {
        updatePayload(context, PAYLOADS_BY_ID_KEY, updateAction)
    }

    /**
     * Ana (genel) payload listesini bir kereliğine okur.
     */
    suspend fun getPayloads(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[PAYLOADS_KEY] ?: ""
        }.first() // .first() operatörü ile Flow'dan tek bir değer alırız.
    }

    /**
     * Kullanıcı ID'sine özel payload listesini bir kereliğine okur.
     */
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

    /**
     * Login ID'sini kaydeder.
     */
    suspend fun saveLoginId(context: Context, loginId: String) {
        context.dataStore.edit { settings ->
            settings[LOGIN_ID_KEY] = loginId
        }
    }
}