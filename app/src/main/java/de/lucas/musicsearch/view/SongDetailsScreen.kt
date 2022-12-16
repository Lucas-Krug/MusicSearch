package de.lucas.musicsearch.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.lucas.musicsearch.R
import de.lucas.musicsearch.model.SongDetails

@Composable
fun SongDetailsScreen(
    song: SongDetails,
    isFavorite: Boolean,
    onClickFavorite: (SongDetails) -> Unit,
    goToYoutube: (String) -> Unit
) {
    var label = ""
    var released = ""
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            AsyncImage(
                model = song.images?.imageUrl,
                contentDescription = "",
                modifier = Modifier.weight(1f)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .weight(3f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .testTag("Title")
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onClickFavorite(song) }) {
                            Icon(
                                painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite_added else R.drawable.ic_favorite),
                                contentDescription = ""
                            )
                        }
                    }
                    Text(
                        text = song.subtitle,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    song.sections.forEach { section ->
                        if (section.metaData != null) {
                            label = section.metaData[1].text
                            released = section.metaData[2].text
                            Column {
                                Text(
                                    text = "Album:",
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = section.metaData[0].text,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = "Genre:",
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = song.genres.genre,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )
                            }
                        }
                    }
                    /*
                    * If handled in one forEach and the data is coming from the database,
                    * the if statement for youtubeSection finishes before the one for the metaData,
                    * which messes up the intended ui
                    */
                    song.sections.forEach { section ->
                        if (section.youtubeSection != null) {
                            Text(
                                text = "Musicvideo",
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold
                            )
                            AsyncImage(
                                model = section.youtubeSection.thumbnail.url,
                                contentDescription = "",
                                modifier = Modifier
                                    .clickable {
                                        goToYoutube(section.youtubeSection.actions[0].youtubeUrl)
                                    }
                                    .testTag("youtubeImage")
                            )
                            Text(
                                text = section.youtubeSection.caption,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                    Text(
                        text = released,
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        text = "Label: $label",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SongDetailsScreenPreview() {
    SongDetailsScreen(
        SongDetails(
            key = "",
            title = "Title",
            subtitle = "Artist",
            images = SongDetails.Images(imageUrl = ""),
            genres = SongDetails.Genre("N/A"),
            sections = listOf(),
        ),
        isFavorite = true,
        onClickFavorite = {},
        goToYoutube = {}
    )
}