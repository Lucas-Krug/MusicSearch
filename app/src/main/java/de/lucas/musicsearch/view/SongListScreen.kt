package de.lucas.musicsearch.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.musicsearch.R
import de.lucas.musicsearch.view.theme.Gray200
import de.lucas.musicsearch.view.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongListScreen(chartState: Boolean, onClickChart: () -> Unit) {
    Column {
        Button(
            onClick = {
                onClickChart()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = if (chartState) Gray200 else White),
            border = BorderStroke(1.dp, Gray200),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.top20))
        }
        SongItem()
    }
}


@ExperimentalMaterialApi
@Composable
internal fun SongList() {

}

@Preview
@Composable
fun SongListScreenPreview() {
    SongListScreen(false) {}
}