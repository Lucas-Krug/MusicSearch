package de.lucas.musicsearch.view

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
import de.lucas.musicsearch.view.theme.roundedShape
import de.lucas.musicsearch.viewmodel.LoadingState
import de.lucas.musicsearch.viewmodel.RootViewModel
import de.lucas.musicsearch.viewmodel.SongDetailsViewModel
import de.lucas.musicsearch.viewmodel.SongListViewModel

@Composable
fun Root() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var title by remember { mutableStateOf("") }
    var showBottomNav by remember { mutableStateOf(true) }
    val rootViewModel: RootViewModel = hiltViewModel()
    val context = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = Color.White, contentColor = Color.Black, elevation = 0.dp
        ) {
            if (!showBottomNav) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = ""
                    )
                }
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }, bottomBar = {
        if (showBottomNav) {
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
    }, floatingActionButton = {
        if (showBottomNav) {
            FloatingActionButton(
                onClick = { /* TODO: open dialog to filter search */ },
                backgroundColor = MaterialTheme.colors.primary,
                shape = roundedShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = ""
                )
            }
        }
    }, isFloatingActionButtonDocked = true, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(navController, startDestination = SONGLIST.route, Modifier.padding(innerPadding)) {
            composable(SONGLIST.route) { stackEntry ->
                val viewModel = hiltViewModel<SongListViewModel>()
                showBottomNav = true
                if (viewModel.charts.tracks.isEmpty()) {
                    LaunchedEffect(viewModel) {
                        viewModel.loadChartSongs(
                            onLoading = { rootViewModel.loadingState = LoadingState.LOADING },
                            onFinished = { rootViewModel.loadingState = LoadingState.DEFAULT },
                            onError = { rootViewModel.loadingState = LoadingState.ERROR })
                    }
                }
                title = SONGLIST.title
                SongListScreen(
                    songs = viewModel.charts,
                    chartState = viewModel.chartState,
                    loadingState = rootViewModel.loadingState,
                    onLoadingCharts = {
                        viewModel.loadChartSongs(
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
                title = FAVORITES.title
            }
            composable(
                "$SONGDETAILS/{key}",
                arguments = listOf(navArgument("key")
                { type = NavType.StringType })
            ) { backStackEntry ->
                val viewModel = hiltViewModel<SongDetailsViewModel>()
                showBottomNav = false
                title = stringResource(id = R.string.details)
                LaunchedEffect(viewModel) {
                    viewModel.loadSongDetails(
                        key = backStackEntry.arguments?.getString("key") ?: "",
                        onLoading = { rootViewModel.loadingState = LoadingState.LOADING },
                        onFinished = { rootViewModel.loadingState = LoadingState.DEFAULT },
                        onError = { rootViewModel.loadingState = LoadingState.ERROR })
                }
                if (rootViewModel.loadingState == LoadingState.DEFAULT) {
                    SongDetailsScreen(
                        song = viewModel.songDetails,
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