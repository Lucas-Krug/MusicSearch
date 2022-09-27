package de.lucas.musicsearch.model

import de.lucas.musicsearch.R

enum class NavigationItem(
    val route: String,
    val icon: Int,
    val title: String
) {
    SONGLIST("songList", R.drawable.ic_lyric, "Songs"),
    FAVORITES("favorites", R.drawable.ic_favorite, "Favorites");
}