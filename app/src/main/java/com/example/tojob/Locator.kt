package com.example.tojob

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.apollographql.apollo3.ApolloClient
import com.example.tojob.data.appconfigs.AppConfigsRepository
import com.example.tojob.data.appconfigs.impl.AppConfigsLocalDataSource
import com.example.tojob.data.jobs.JobsRepository
import com.example.tojob.data.jobs.impl.JobsRemoteDataSource
import com.example.tojob.ui.home.HomeViewModel
import com.example.tojob.ui.splash.SplashViewModel

object Locator {
    private var application: Application? = null

    private inline val requireApplication
        get() = application ?: error("Missing call: initWith(application)")

    fun initWith(application: Application) {
        Locator.application = application
    }

    val splashViewModelFactory
        get() = SplashViewModel.provideFactory(
            appConfigsRepository = appConfigsRepository
        )


    val homeViewModelFactory
        get() = HomeViewModel.provideFactory(
            postsRepository = jobsRepository,
            appConfigsRepository = appConfigsRepository
        )

    private val apolloClient = ApolloClient.Builder()
        .serverUrl("https://api.graphql.jobs/")
        .build()

    private val Context.dataStore by preferencesDataStore(name = "app_configs")

    private val appConfigsRepository: AppConfigsRepository by lazy {
        AppConfigsLocalDataSource(requireApplication.dataStore)
    }

    private val jobsRepository: JobsRepository by lazy {
        JobsRemoteDataSource(apolloClient)
    }
}
