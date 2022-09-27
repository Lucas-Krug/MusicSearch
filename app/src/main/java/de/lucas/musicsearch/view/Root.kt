package de.lucas.musicsearch.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.lucas.musicsearch.R
import de.lucas.musicsearch.model.NavigationItem.*
import de.lucas.musicsearch.view.theme.roundedShape
import de.lucas.musicsearch.viewmodel.RootViewModel
import timber.log.Timber

@Composable
fun Root() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var title by remember { mutableStateOf("") }
    val rootViewModel: RootViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = 0.dp
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        shadowElevation = 15f
                    }
                    .semantics { contentDescription = "bottomNav" }
            ) {
                // Create an BottomNavigationItem for every provided NavigationItem
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
                    // Creates an empty Item between second and third, to keep the middle section empty for fab
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
        },
        floatingActionButton = {
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
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = SONGLIST.route,
            Modifier.padding(innerPadding)
        ) {
            composable(SONGLIST.route) { stackEntry ->
                title = SONGLIST.title
                SongListScreen(rootViewModel.chartState) {
                    // TODO: Only allow clicking (searching for top20) when chartState false
                    rootViewModel.chartState = !rootViewModel.chartState
                }
            }
            composable(FAVORITES.route) {
                title = FAVORITES.title
            }
        }
    }
}

@Preview
@Composable
fun RootPreview() {
    Root()
}