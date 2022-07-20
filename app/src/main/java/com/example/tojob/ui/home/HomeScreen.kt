package com.example.tojob.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tojob.R
import com.example.tojob.ui.components.ContentBlock
import com.example.tojob.ui.components.JobsAlertDialog
import com.example.tojob.ui.theme.Gray
import com.example.tojob.ui.theme.TOJobTheme
import com.google.accompanist.insets.statusBarsPadding
import java.util.*

@Composable
fun HomeScreen(
    onSearchResult: (type: String) -> Unit,
    onSearchUpdate: (input: TextFieldValue) -> Unit,
    onClearErrorMessage: () -> Unit,
    onRecentSearch: (String) -> Unit,
    uiState: HomeUiState
) {
    TOJobTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentWidth(align = Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_logo_white),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(36.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    style = MaterialTheme.typography.h6,
                                    color = LocalContentColor.current,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1.5f)
                                )
                            }
                        },
                        elevation = 4.dp,
                        backgroundColor = MaterialTheme.colors.surface,
                    )
                }
            ) {
                Column {
                    Spacer(modifier = Modifier.statusBarsPadding())
                    SearchBar(
                        query = uiState.searchInput,
                        onQueryChange = { onSearchUpdate(it) },
                        onSearchFocusChange = { },
                    )

                    Column(
                        horizontalAlignment = Alignment.End, modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp
                            )
                    ) {
                        Button(onClick = {
                            onSearchResult.invoke(uiState.searchInput.text)
                        }, enabled = uiState.searchEnable) {
                            Text(text = stringResource(id = R.string.search))
                        }
                    }

                    Divider(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp))

                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(
                            text = stringResource(id = R.string.recent_search),
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RecentSearch(
                            jobList = uiState.recentSearch.searchHistory,
                            onRecentSearch = onRecentSearch
                        )
                    }
                }

                if (uiState.isLoading) {
                    LoadingDialog()
                }

                if (uiState.errorMessages.isNotEmpty()) {
                    JobsAlertDialog(
                        onDismiss = onClearErrorMessage,
                        bodyText = uiState.errorMessages.last().message
                            ?: stringResource(id = R.string.error_occurred),
                        buttonText = stringResource(id = R.string.dismiss).uppercase(Locale.getDefault())
                    )
                }
            }
        }
    }
}

@Composable
fun RecentSearch(
    state: LazyListState = rememberLazyListState(),
    jobList: List<String>,
    onRecentSearch: (String) -> Unit
) {
    LazyColumn(state = state) {
        items(jobList) { item ->
            Column {
                ContentBlock(
                    modifier = Modifier.clickable { onRecentSearch(item) },
                    horizontalPadding = 0.dp
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Gray,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        }
                )
            }
        }
    }
}

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = {},
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(White, shape = RoundedCornerShape(12.dp))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Text(text = stringResource(id = R.string.loading), Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
//    HomeScreen()
}
