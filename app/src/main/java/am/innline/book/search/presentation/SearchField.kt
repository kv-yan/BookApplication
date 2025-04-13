package am.innline.book.search.presentation

import am.inline.book.R
import am.innline.book.common_presentation.ui.theme.MainBlue
import am.innline.book.common_presentation.ui.theme.SubTitleColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    searchHistory: List<String> = emptyList(),
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSearchItemDelete: (String) -> Unit,
    onClearSearchHistory: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier.semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            expanded = expanded,
            shadowElevation = 4.dp,
            inputField = {
                SearchBarDefaults.InputField(
                    query = value,
                    onQueryChange = onValueChange,
                    onSearch = {
                        onSearch()
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search books") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search"
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = MainBlue,
                        unfocusedIndicatorColor = Color.Black,
                        cursorColor = MainBlue
                    )
                )
            },
            onExpandedChange = { expanded = it },
            colors = SearchBarDefaults.colors(
                containerColor = Color.White,
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (searchHistory.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                text = "No recent searches",
                                color = SubTitleColor
                            )
                        }
                    }
                    items(searchHistory) {
                        Column(
                            modifier
                                .fillMaxWidth()
                                .clickable {
                                    onValueChange(it)
                                    onSearch()
                                    expanded = false
                                }
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_recent),
                                    tint = SubTitleColor,
                                    contentDescription = "Recent",
                                )

                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = it,
                                    color = SubTitleColor
                                )

                                IconButton(
                                    onClick = { onSearchItemDelete(it) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        tint = SubTitleColor,
                                        contentDescription = "Delete",
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = SubTitleColor,
                                thickness = 1.dp
                            )

                        }
                    }
                }


                Text(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clickable { onClearSearchHistory() },
                    text = "Clear history",
                    color = MainBlue
                )
            }
        }
    }
}

