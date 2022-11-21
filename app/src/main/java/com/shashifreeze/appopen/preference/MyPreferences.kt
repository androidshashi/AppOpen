package com.shashifreeze.appopen.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shashifreeze.appopen.apputils.Constants.PREFERENCE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


//top level
val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

/**
 *Singleton class
 *
 */
@Singleton
object MyPreferences {

    //Constants
    private val KEY_TNC_ACCEPTED= booleanPreferencesKey("KEY_TNC_ACCEPTED_2")
    private val KEY_AD_SEEN_ON= stringPreferencesKey("KEY_AD_SEEN_ON")
    private val KEY_AD_INFO= booleanPreferencesKey("KEY_AD_INFO")
    private val KEY_DS= stringPreferencesKey("KEY_DS")


    /**set whether user has accepted the condition or not*/
    suspend fun setTNCAccepted(context: Context, accepted: Boolean) {
        context.appDataStore.apply {
            edit { it[KEY_TNC_ACCEPTED] = accepted }
        }
    }

    /**
     * Get whether user has accepted TNC or not
     */
    fun isTNCAccepted(context: Context):Flow<Boolean> = context.appDataStore.data.map {
        if (it[KEY_TNC_ACCEPTED] != null) it[KEY_TNC_ACCEPTED]!! else false
    }


    /**CLEAR current logged in user details from datastore*/
    suspend fun clear(context: Context) =
        context.appDataStore.edit {
            it.clear()
        }


    /**set date for last ad seen*/
    suspend fun setAdSeenDate(context: Context, date: String) {
        context.appDataStore.apply {
            edit { it[KEY_AD_SEEN_ON] = date }
        }
    }

    /**
     * Get Date for last ad seen
     */
    fun getAdSeenDate(context: Context):Flow<String?> = context.appDataStore.data.map {
        if (it[KEY_AD_SEEN_ON] != null) it[KEY_AD_SEEN_ON]!! else null
    }

    /**set if the user has seen the ad once a day alert*/
    suspend fun setAdInfoAlert(context: Context, seen: Boolean) {
        context.appDataStore.apply {
            edit { it[KEY_AD_INFO] = seen }
        }
    }

    /**
     * check if the user has already seen the ad once a day info or not
     */
    fun getAdInfoAlert(context: Context):Flow<Boolean?> = context.appDataStore.data.map {
        if (it[KEY_AD_INFO] != null) it[KEY_AD_INFO]!! else null
    }

}