package com.example.tojob.ui.searchresult

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tojob.JobsQuery
import com.example.tojob.R
import com.example.tojob.model.JobList
import com.example.tojob.ui.components.ContentBlock
import com.example.tojob.ui.theme.TOJobTheme

@Composable
fun SearchResultScreen(
    queryText: String,
    jobList: JobList,
    onDetail: (JobsQuery.Job) -> Unit,
    onBack: () -> Unit,
    state: LazyListState = rememberLazyListState()
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
                                    .fillMaxWidth()
                                    .wrapContentWidth(align = Alignment.CenterHorizontally)
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
                                    text = stringResource(
                                        id = R.string.search_in,
                                        queryText,
                                        jobList.jobs.size,
                                        jobList.companyNumber
                                    ),
                                    style = MaterialTheme.typography.subtitle2,
                                    color = LocalContentColor.current,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1.5f)
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_navigate_up),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        },
                        elevation = 4.dp,
                        backgroundColor = MaterialTheme.colors.surface
                    )
                }
            ) {
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    JobList(state = state, jobList = jobList.jobs, onDetail = onDetail)
                }
            }
        }
    }
}

@Composable
fun JobList(
    state: LazyListState = rememberLazyListState(),
    jobList: List<JobsQuery.Job>,
    onDetail: (JobsQuery.Job) -> Unit
) {
    LazyColumn(state = state) {
        items(jobList) { item ->
            Column {
                ContentBlock(modifier = Modifier.clickable { onDetail.invoke(item) }) {
                    JobItem(data = item)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun JobItem(data: JobsQuery.Job) {
    Row(
        modifier = Modifier.padding(all = 12.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = data.company?.logoUrl,
                error = rememberVectorPainter(image = Icons.Filled.BrokenImage)
            ),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = data.title,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = data.company?.name ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
