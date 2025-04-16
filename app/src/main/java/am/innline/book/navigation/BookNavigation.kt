package am.innline.book.navigation

import am.innline.book.favorites.presentation.FavoritesScreen
import am.innline.book.favorites.presentation.FavoritesViewModel
import am.innline.book.search.presentation.SearchScreen
import am.innline.book.search.presentation.SearchViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookNavigation(
    modifier: Modifier = Modifier,
    favoriteViewModel: FavoritesViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(),
    navController: NavHostController,
    navigateToDetails: (String, Boolean) -> Unit = { _, _ -> },
) {


//    val navController = rememberNavController()

    val destinations = listOf(
        BottomItem("Search", BookDestination.Search, Icons.Rounded.Search),
        BottomItem("Favorites", BookDestination.Favorites, Icons.Rounded.Favorite),
    )

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = navController,
            startDestination = BookDestination.Search
        ) {
            composable<BookDestination.Search> {
                SearchScreen(
                    favoritesViewModel = favoriteViewModel,
                    searchViewModel = searchViewModel,
                    navigateToDetails = { bookId, loadLocally ->
                        navigateToDetails(
                            bookId, loadLocally
                        )
                    }
                )
            }
            composable<BookDestination.Favorites> {
                FavoritesScreen(
                    favoritesViewModel = favoriteViewModel,
                    navigateToDetails = { bookId, loadLocally ->
                        navigateToDetails(
                            bookId, loadLocally
                        )
                    }
                )
            }
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomAppBar(
            modifier = Modifier.fillMaxWidth(), containerColor = White
        ) {
            destinations.forEach { bottomItem ->
                NavigationBarItem(
                    label = { Text(text = bottomItem.name) },
                    selected = currentDestination?.route == bottomItem.bookDestination.javaClass.canonicalName,
                    onClick = { navController.navigateOnce(bottomItem.bookDestination) },
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


fun NavController.navigateOnce(bookDestination: BookDestination) {
    val current = currentBackStackEntry?.destination
    val targetRoute = bookDestination::class.java.name

    val isSameScreen = current?.route == targetRoute

    if (!isSameScreen) {
        navigate(bookDestination) {
            popUpTo(graph.startDestinationId)
        }
    }
}
