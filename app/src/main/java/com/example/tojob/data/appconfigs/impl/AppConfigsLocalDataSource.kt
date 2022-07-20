package com.example.tojob.data.appconfigs.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.tojob.data.appconfigs.AppConfigsRepository
import com.example.tojob.model.RecentSearch
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

class AppConfigsLocalDataSource(
    private val dataStore: DataStore<Preferences>
) : AppConfigsRepository {

    private object Keys {
        val isFirstLaunch = booleanPreferencesKey("is_first_launch")
        val recentSearchKeyword = stringPreferencesKey("recent_search_keyword")
    }

    private inline val Preferences.isFirstLaunch
        get() = this[Keys.isFirstLaunch] ?: true

    private inline val Preferences.recentSearchKeyword
        get() = this[Keys.recentSearchKeyword] ?: ""

    override val isFirstLaunch: Flow<Boolean> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it.isFirstLaunch
        }.distinctUntilChanged()

    override val recentSearch: Flow<RecentSearch> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { parseRecentSearch(it) }
        .distinctUntilChanged()

    // Returns an empty search history if error happened
    private fun parseRecentSearch(pref: Preferences): RecentSearch {
        return try {
            Gson().fromJson(pref.recentSearchKeyword, RecentSearch::class.java)
        } catch (e: Exception) {
            RecentSearch()
        }
    }

    override suspend fun setFirstLaunch(firstLaunch: Boolean) {
        dataStore.edit { it[Keys.isFirstLaunch] = firstLaunch }
    }

    override suspend fun updateRecentSearch(recentSearchJsonStr: String) {
        dataStore.edit { it[Keys.recentSearchKeyword] = recentSearchJsonStr }
    }
}
