package com.example.tojob.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.TextFieldValue
import com.example.tojob.JobsQuery
import com.example.tojob.ui.home.HomeScreenType.*
import com.example.tojob.ui.seachdetail.SearchDetailScreen
import com.example.tojob.ui.searchresult.SearchResultScreen
import kotlinx.coroutines.launch

/**
 * Displays the Home route.
 */
@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
) {
    // UiState of the HomeScreen
    val uiState by homeViewModel.uiState.collectAsState()

    HomeRoute(
        uiState = uiState,
        onSearchResult = { type ->
            homeViewModel.getJobs(type)
        },
        onSearchUpdate = { input ->
            homeViewModel.updateSearchInput(input)
        },
        onInteractWithJob = { homeViewModel.interactedWithJob() },
        onInteractWithJobList = { homeViewModel.interactedWithJobList() },
        onClearErrorMessage = { homeViewModel.clearErrorMessages() },
        onDetailClick = { homeViewModel.selectJob(it) },
        onRecentSearch = { homeViewModel.updateSearchInput(TextFieldValue(it)) }
    )
}

/**
 * Displays the Home route.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeRoute(
    onSearchResult: (type: String) -> Unit,
    onSearchUpdate: (input: TextFieldValue) -> Unit,
    onInteractWithJob: () -> Unit,
    onInteractWithJobList: () -> Unit,
    onClearErrorMessage: () -> Unit,
    onDetailClick: (JobsQuery.Job) -> Unit,
    onRecentSearch: (String) -> Unit,
    uiState: HomeUiState
) {
    // Construct the lazy list states for the list and the details outside of deciding which one to
    // show. This allows the associated state to survive beyond that decision, and therefore
    // we get to preserve the scroll throughout any changes to the content.
    val searchListLazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    when (getHomeScreenType(uiState)) {
        Home -> {
            HomeScreen(
                onSearchResult = onSearchResult,
                onSearchUpdate = onSearchUpdate,
                onClearErrorMessage = onClearErrorMessage,
                onRecentSearch = onRecentSearch,
                uiState = uiState,
            )
            coroutineScope.launch {
                searchListLazyListState.scrollToItem(0)
            }

        }
        SearchResult -> {
            // Guaranteed by above condition for home screen type
            check(uiState is HomeUiState.HasJobList)

            SearchResultScreen(
                onDetail = onDetailClick,
                onBack = onInteractWithJobList,
                jobList = uiState.jobList,
                state = searchListLazyListState,
                queryText = uiState.searchInput.text
            )
            BackHandler {
                onInteractWithJobList()     // Handle back event in search result screen
            }
        }
        SearchDetail -> {
            // Guaranteed by above condition for home screen type
            check(uiState is HomeUiState.HasJobList)

            SearchDetailScreen(
                job = uiState.selectedJob,
                onBack = onInteractWithJob
            )

            BackHandler {
                onInteractWithJob()     // Handle back event in detail screen
            }
        }
    }
}

/**
 * A precise enumeration of which type of screen to display at the home route.
 *
 * There are 3 options:
 * - [Home], which displays the home page where you can conduct job search with input keywords.
 * - [SearchResult], which displays just the list of search result
 * - [SearchDetail], which displays just a specific job.
 */
private enum class HomeScreenType {
    Home,
    SearchResult,
    SearchDetail
}

/**
 * Returns the current [HomeScreenType] to display, based on the [HomeUiState].
 */
@Composable
private fun getHomeScreenType(
    uiState: HomeUiState
): HomeScreenType = when (uiState) {
    is HomeUiState.HasJobList -> {
        if (uiState.isJobOpen) {
            SearchDetail
        } else {
            SearchResult
        }
    }
    is HomeUiState.NoJobs -> Home
}
