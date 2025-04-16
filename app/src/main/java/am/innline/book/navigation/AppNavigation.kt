package am.innline.book.navigation

import am.innline.book.details.presentation.BookDetailsScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val appNavController = rememberNavController()
    val bookNavController = rememberNavController()

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = appNavController,
        startDestination = AppDestination.Book
    ) {
        composable<AppDestination.Book> {
            BookNavigation(
                modifier = Modifier.fillMaxSize(),
                navController = bookNavController,
                navigateToDetails = { bookId, loadLocally ->
                    appNavController.navigate(AppDestination.Details(bookId, loadLocally))
                }
            )
        }
        composable<AppDestination.Details> {
            val details = it.toRoute<AppDestination.Details>()
            BookDetailsScreen(
                bookId = details.bookId,
                loadLocally = details.loadLocally,
                onBackClick = {
                    appNavController.navigateUp()
                }
            )
        }
    }
}