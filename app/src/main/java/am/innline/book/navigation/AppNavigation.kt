package am.innline.book.navigation

import am.innline.book.book_details.presentation.BookDetailsScreen
import am.innline.book.favorites.presentation.FavoritesScreen
import am.innline.book.favorites.presentation.FavoritesViewModel
import am.innline.book.search.presentation.SearchScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    favoriteViewModel: FavoritesViewModel = koinViewModel(),
) {
    val navController = rememberNavController()

    val destinations = listOf(
        BottomItem("Search", Destination.Search, Icons.Rounded.Search),
        BottomItem("Favorites", Destination.Favorites, Icons.Rounded.Favorite),
    )

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = navController,
            startDestination = Destination.Search
        ) {
            composable<Destination.Search> {
                SearchScreen(
                    favoritesViewModel = favoriteViewModel,
                    navigateToDetails = { bookId, loadLocally ->
                        navController.navigateOnce(
                            Destination.Details(
                                bookId = bookId, loadLocally
                            )
                        )
                    })
            }
            composable<Destination.Favorites> {
                FavoritesScreen(
                    viewModel = favoriteViewModel, navigateToDetails = { bookId, loadLocally ->
                        navController.navigateOnce(Destination.Details(bookId, loadLocally))
                    })
            }
            composable<Destination.Details> {
                val details = it.toRoute<Destination.Details>()
                BookDetailsScreen(
                    bookId = details.bookId,
                    loadLocally = details.loadLocally,
                    onBackClick = { navController.popBackStack() })
            }
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Destination.Details::class.java.canonicalName
        currentDestination?.route?.javaClass

        AnimatedVisibility(
            visible = currentDestination?.route !== Destination.Details::class.java.canonicalName
        ) {

            NavigationBar(
                modifier = Modifier.fillMaxWidth(), containerColor = White
            ) {
                destinations.forEach { bottomItem ->
                    NavigationBarItem(
                        label = { Text(text = bottomItem.name) },
                        selected = currentDestination?.route == bottomItem.destination.javaClass.canonicalName,
                        onClick = { navController.navigateOnce(bottomItem.destination) },
                        icon = {
                            Icon(
                                imageVector = bottomItem.icon,
                                contentDescription = bottomItem::class.java.simpleName
                            )
                        })
                }
            }
        }
    }
}

fun NavController.navigateOnce(destination: Destination) {
    val current = currentBackStackEntry?.destination
    val targetRoute = destination::class.java.name

    val isSameScreen = current?.route == targetRoute

    if (!isSameScreen) {
        navigate(destination) {
            launchSingleTop = true
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
            restoreState = true
        }
    }
}