package com.example.tojob.data.appconfigs

import com.example.tojob.model.RecentSearch
import kotlinx.coroutines.flow.Flow

interface AppConfigsRepository {
    val isFirstLaunch: Flow<Boolean>
    val recentSearch: Flow<RecentSearch>
    suspend fun setFirstLaunch(firstLaunch: Boolean)
    suspend fun updateRecentSearch(recentSearchJsonStr: String)
}
