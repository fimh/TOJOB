package com.example.tojob.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tojob.R
import com.example.tojob.ui.theme.TOJobTheme

@Composable
fun SplashScreen() {
    TOJobTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Branding()
        }
    }
}

@Composable
private fun Branding(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Logo(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 76.dp)
        )

        Slogan(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun Slogan(modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.app_slogan_start))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append(" ")
                append(stringResource(id = R.string.app_slogan_center))
                append(" ")
            }
            append(stringResource(id = R.string.app_slogan_end))
        },
        style = MaterialTheme.typography.subtitle1,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun Logo(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.ic_logo_white),
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
        contentDescription = null
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
