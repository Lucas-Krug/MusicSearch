package de.lucas.musicsearch.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.musicsearch.R
import de.lucas.musicsearch.model.api.SongList.Track
import de.lucas.musicsearch.view.theme.White

@ExperimentalMaterialApi
@Composable
internal fun SongItem(song: Track) {
    Card(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        true,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = White,
        contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
        border = null,
        elevation = 4.dp,
        interactionSource = remember { MutableInteractionSource() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.architects),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .weight(1f)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(4f)
            ) {
                Text(text = song.title, style = MaterialTheme.typography.body1)
                Text(text = song.subtitle, style = MaterialTheme.typography.body2)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun SongItemPreview() {
    SongItem(
        Track(
            key = "",
            title = "Title",
            subtitle = "Artist",
            images = Track.Images(image = "")
        )
    )
}