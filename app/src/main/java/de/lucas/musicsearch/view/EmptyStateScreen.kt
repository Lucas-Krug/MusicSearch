package de.lucas.musicsearch.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.lucas.musicsearch.R
import de.lucas.musicsearch.view.theme.Gray200
import de.lucas.musicsearch.view.theme.White

@Composable
fun NoInternetScreen(onClickRetry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_connection),
            contentDescription = "",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(64.dp)
        )
        Text(
            text = stringResource(id = R.string.no_connection),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onClickRetry,
            colors = ButtonDefaults.buttonColors(backgroundColor = White),
            border = BorderStroke(1.dp, Gray200),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .height(48.dp)
                .width(112.dp)
        ) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}