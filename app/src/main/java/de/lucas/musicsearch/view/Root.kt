package de.lucas.musicsearch.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.lucas.musicsearch.R
import de.lucas.musicsearch.model.NavigationItem.*
import de.lucas.musicsearch.model.NavigationItem.Companion.SONGDETAILS
import de.lucas.musicsearch.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Root() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val rootViewModel = hiltViewModel<RootViewModel>()
    val songListViewModel = hiltViewModel<SongListViewModel>()
    val context = LocalContext.current
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            backgroundColor = Color.White, contentColor = Color.Black, elevation = 0.dp
        ) {
            if (!rootViewModel.showBottomNav) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = ""
                    )
                }
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    text = rootViewModel.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            if (rootViewModel.showSearchView) {
                Spacer(modifier = Modifier.weight(1f))
                SearchView(onClickSearch = { searchText ->
                    songListViewModel.chartState = false
                    songListViewModel.loadSearchedSong(
                        searchedText = searchText,
                        onLoading = { rootViewModel.loadingState = LoadingState.LOADING },
                        onFinished = { rootViewModel.loadingState = LoadingState.DEFAULT },
                        onError = { rootViewModel.loadingState = LoadingState.ERROR }
                    )
                })
            }
        }
    }, bottomBar = {
        if (rootViewModel.showBottomNav) {
            BottomNavigation(modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    shadowElevation = 15f
                }
                .semantics { contentDescription = "bottomNav" }) {
                values().forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                    if (item.ordinal == 0) {
                        BottomNavigationItem(
                            icon = { },
                            label = { },
                            selected = false,
                            onClick = { },
                            enabled = false
                        )
                    }
                }
            }
        }
    }) { innerPadding ->
        NavHost(navController, startDestination = SONGLIST.route, Modifier.padding(innerPadding)) {
            composable(SONGLIST.route) { stackEntry ->
                rootViewModel.showBottomNav = true
                rootViewModel.showSearchView = true
                if (songListViewModel.songList.tracks.isEmpty()) {
                    LaunchedEffect(songListViewModel) {
                        songListViewModel.loadChartSongs(
                            onLoading = { rootViewModel.loadingState = LoadingState.LOADING },
                            onFinished = { rootViewModel.loadingState = LoadingState.DEFAULT },
                            onError = { rootViewModel.loadingState = LoadingState.ERROR })
                    }
                }
                rootViewModel.title = SONGLIST.title
                SongListScreen(
                    songs = songListViewModel.songList,
                    chartState = songListViewModel.chartState,
                    loadingState = rootViewModel.loadingState,
                    onLoadingCharts = {
                        songListViewModel.chartState = true
                        songListViewModel.loadChartSongs(
                            onLoading = { rootViewModel.loadingState = LoadingState.LOADING },
                            onFinished = { rootViewModel.loadingState = LoadingState.DEFAULT },
                            onError = { rootViewModel.loadingState = LoadingState.ERROR })
                    },
                    onClickSong = { key ->
                        navController.navigate("$SONGDETAILS/$key")
                    }
                )
            }
            composable(FAVORITES.route) {
                rootViewModel.showSearchView = false
                rootViewModel.showBottomNav = true
                rootViewModel.title = FAVORITES.title
                val viewModel = hiltViewModel<FavoriteViewModel>()
                LaunchedEffect(songListViewModel) {
                    viewModel.getFavorites()
                }
                FavoriteScreen(
                    songs = viewModel.favoritesList,
                    onClickSong = { key -> navController.navigate("$SONGDETAILS/$key") }
                )
            }
            composable(
                "$SONGDETAILS/{key}",
                arguments = listOf(navArgument("key")
                { type = NavType.StringType })
            ) { backStackEntry ->
                val viewModel = hiltViewModel<SongDetailsViewModel>()
                rootViewModel.showSearchView = false
                rootViewModel.showBottomNav = false
                rootViewModel.title = stringResource(id = R.string.details)
                LaunchedEffect(viewModel) {
                    viewModel.isSongFavorite(backStackEntry.arguments?.getString("key") ?: "")
                    if (viewModel.isFavorite.value) {
                        viewModel.getFavoriteSong(backStackEntry.arguments?.getString("key") ?: "")
                    } else {
                        viewModel.loadSongDetails(
                            key = backStackEntry.arguments?.getString("key") ?: "",
                            onLoading = { rootViewModel.loadingState = LoadingState.LOADING },
                            onFinished = { rootViewModel.loadingState = LoadingState.DEFAULT },
                            onError = { rootViewModel.loadingState = LoadingState.ERROR })
                    }
                }
                if (rootViewModel.loadingState == LoadingState.DEFAULT) {
                    SongDetailsScreen(
                        song = viewModel.songDetails,
                        isFavorite = viewModel.isFavorite.value,
                        onClickFavorite = {
                            viewModel.isFavorite.value = !viewModel.isFavorite.value
                            if (viewModel.isFavorite.value) {
                                viewModel.setSongAsFavorite(viewModel.songDetails)
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Added to Favorites",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else {
                                viewModel.removeSongFromFavorite(
                                    backStackEntry.arguments?.getString(
                                        "key"
                                    ) ?: ""
                                )
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Removed from Favorites",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        goToYoutube = { youtubeUrl ->
                            viewModel.goToYoutube(url = youtubeUrl, context = context)
                        }
                    )
                }
            }
        }
        if (rootViewModel.loadingState == LoadingState.LOADING) {
            LoadingIndicator(id = R.string.loading)
        }
    }
}

@Preview
@Composable
fun RootPreview() {
    Root()
}