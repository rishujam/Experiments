package com.example.logmate.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.logmate.util.Common
import kotlinx.coroutines.flow.first

/*
 * Created by Sudhanshu Kumar on 19/04/23.
 */

internal class Preferences(
    private val context: Context
) {

    private companion object {
        val KEY_CURR_FILE_NAME = stringPreferencesKey("key_curr_file_name")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "log_mate_pref")

    suspend fun readCurrFileName(): String {
        val preferences = context.dataStore.data.first()
        return preferences[KEY_CURR_FILE_NAME] ?: setCurrFileName()
    }

    suspend fun setCurrFileName(): String {
        val fileName = createFileName()
        context.dataStore.edit { data ->
            data[KEY_CURR_FILE_NAME] = fileName
        }
        return fileName
    }

    private fun createFileName(): String {
        val currentMillis = System.currentTimeMillis()
        return "log-$currentMillis.txt"
    }

}