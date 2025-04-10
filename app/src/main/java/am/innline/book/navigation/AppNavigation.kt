package am.innline.book.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.Search
    ) {
        composable<Destination.Search> {  }
        composable<Destination.Favorites> {  }
        composable<Destination.Details> {  }

    }
}