package com.example.tojob.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tojob.data.appconfigs.AppConfigsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val appConfigsRepository: AppConfigsRepository
) : ViewModel() {

    fun setFirstLaunchWithDelay(timeMillis: Long, firstLaunch: Boolean, onFinished: () -> Unit) {
        viewModelScope.launch {
            delay(timeMillis)
            appConfigsRepository.setFirstLaunch(firstLaunch)
            onFinished.invoke()
        }
    }

    val isFirstLaunch: Flow<Boolean> = appConfigsRepository.isFirstLaunch

    companion object {
        fun provideFactory(
            appConfigsRepository: AppConfigsRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SplashViewModel(appConfigsRepository) as T
            }
        }
    }
}
