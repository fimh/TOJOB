package com.example.tojob.ui.home

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tojob.JobsQuery
import com.example.tojob.data.Result
import com.example.tojob.data.appconfigs.AppConfigsRepository
import com.example.tojob.data.jobs.JobsRepository
import com.example.tojob.model.ErrorMessage
import com.example.tojob.model.JobList
import com.example.tojob.model.RecentSearch
import com.example.tojob.util.Constants
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * UI state for the Home route.
 *
 * This is derived from [HomeViewModelState], but split into two possible subclasses to more
 * precisely represent the state available to render the UI.
 */
sealed interface HomeUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val searchInput: TextFieldValue
    val recentSearch: RecentSearch

    val searchEnable: Boolean
        get() = searchInput.text.isNotBlank()

    /**
     * There are no posts to render.
     *
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class NoJobs(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: TextFieldValue,
        override val recentSearch: RecentSearch
    ) : HomeUiState

    /**
     * There are posts to render, as contained in [jobList].
     *
     * There is guaranteed to be a [selectedJob], which is one of the posts from [jobList].
     */
    data class HasJobList(
        val jobList: JobList,
        val selectedJob: JobsQuery.Job,
        val isJobOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: TextFieldValue,
        override val recentSearch: RecentSearch
    ) : HomeUiState
}

/**
 * An internal representation of the Home route state, in a raw form
 */
private data class HomeViewModelState(
    val jobList: JobList? = null,
    // val selectedJobId: String? = null, // FIXME specified graphql don't include job id?
    val selectedJob: JobsQuery.Job? = null,
    val isJobOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: TextFieldValue = TextFieldValue(""),
    val recentSearch: RecentSearch = RecentSearch()
) {

    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving
     * the ui.
     */
    fun toUiState(): HomeUiState =
        if (jobList == null || jobList.jobs.isEmpty()) {
            HomeUiState.NoJobs(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput,
                recentSearch = recentSearch
            )
        } else {
            HomeUiState.HasJobList(
                jobList = jobList,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput,
                recentSearch = recentSearch,
                // Determine the selected job. This will be the job the user last selected.
                // If there is none, default the first one
                selectedJob = selectedJob ?: jobList.jobs[0],
                isJobOpen = isJobOpen
            )
        }
}

/**
 * ViewModel that handles the business logic of the Home screen
 */
class HomeViewModel(
    private val jobsRepository: JobsRepository,
    private val appConfigsRepository: AppConfigsRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = false))

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        // Observe for recent search changes in the repo layer
        viewModelScope.launch {
            appConfigsRepository.recentSearch.collect { recentSearch ->
                viewModelState.update { it.copy(recentSearch = recentSearch) }
            }
        }
    }

    /**
     * Refresh posts and update the UI state accordingly
     */
    fun getJobs(type: String) {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = jobsRepository.getJobs(type = type)
            viewModelState.update {
                Log.d(HomeViewModel::class.java.name, "$result")

                when (result) {
                    is Result.Success -> {
                        var originalHistory = it.recentSearch.searchHistory.toMutableList()
                        val searchText = it.searchInput.text
                        // Handle duplicated cases
                        if (originalHistory.contains(searchText)) { // Descending order
                            originalHistory.remove(searchText)
                        }
                        originalHistory.add(0, searchText)
                        if (originalHistory.size > Constants.MAX_SEARCH_HISTORY) {     // TODO make max search history configurable, 5 currently
                            originalHistory =
                                originalHistory.subList(0, Constants.MAX_SEARCH_HISTORY)
                        }

                        // FIXME: Save to data store, it's better to use db if data grows
                        val newHistory = RecentSearch(originalHistory.toList())
                        appConfigsRepository.updateRecentSearch(
                            Gson().toJson(newHistory).toString()
                        )

                        it.copy(
                            jobList = JobList(result.data),
                            isLoading = false
                        )
                    }
                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            message = result.exception.message
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    /**
     * Notify that the user click a job
     */
    fun selectJob(job: JobsQuery.Job) {
        viewModelState.update {
            it.copy(selectedJob = job, isJobOpen = true)
        }
    }

    /**
     * Notify that the user interacted with the job
     */
    fun interactedWithJob() {
        viewModelState.update {
            it.copy(isJobOpen = false)
        }
    }

    /**
     * Notify that the user interacted with job list
     */
    fun interactedWithJobList() {
        viewModelState.update {
            it.copy(jobList = null)
        }
    }

    /**
     * Notify that the user acknowledge the error message.
     */
    fun clearErrorMessages() {
        viewModelState.update {
            it.copy(errorMessages = emptyList())
        }
    }

    /**
     * Notify that the user updated the search query
     */
    fun updateSearchInput(searchInput: TextFieldValue) {
        viewModelState.update {
            it.copy(searchInput = searchInput)
        }
    }

    /**
     * Factory for HomeViewModel that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            postsRepository: JobsRepository,
            appConfigsRepository: AppConfigsRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(postsRepository, appConfigsRepository) as T
            }
        }
    }
}
