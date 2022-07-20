package com.example.tojob.ui.seachdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tojob.JobsQuery
import com.example.tojob.R
import com.example.tojob.ui.components.ContentBlock
import com.example.tojob.ui.theme.TOJobTheme

@Composable
fun SearchDetailScreen(job: JobsQuery.Job, onBack: () -> Unit = {}) {
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
                                    painter = rememberAsyncImagePainter(
                                        model = job.company?.logoUrl,
                                        error = rememberVectorPainter(image = Icons.Filled.BrokenImage)
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(36.dp)
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.job_name,
                                        job.title
                                    ),
                                    style = MaterialTheme.typography.subtitle2,
                                    color = LocalContentColor.current,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1.5f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
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
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    val blockMinHeight = 80.dp
                    val verticalMargin = 16.dp
                    Spacer(modifier = Modifier.height(verticalMargin))
                    // Job Title
                    ContentBlock(minHeight = blockMinHeight) {
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(verticalMargin))
                    // Company Logo & Company Name
                    ContentBlock(minHeight = blockMinHeight) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = job.company?.logoUrl,
                                    error = rememberVectorPainter(image = Icons.Filled.BrokenImage)
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = job.company?.name
                                    ?: stringResource(id = R.string.company_name),
                                style = MaterialTheme.typography.h6,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(verticalMargin))
                    // Description
                    ContentBlock(minHeight = blockMinHeight) {
                        Text(
                            text = job.description ?: stringResource(id = R.string.description),
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(verticalMargin))
                }
            }
        }
    }
}
