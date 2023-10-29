/*
 *    Copyright 2023 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.horaciocome1.catbreedexplorer.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedListModel
import io.github.horaciocome1.catbreedexplorer.ui.components.BreedListItem
import io.github.horaciocome1.catbreedexplorer.ui.theme.CatBreedExplorerTheme
import io.github.horaciocome1.catbreedexplorer.ui.theme.size

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun BreedListScreen(
    breeds: List<BreedListModel> = emptyList(),
    onClick: (String) -> Unit = {},
    onFavorite: (String, Boolean) -> Unit = { _, _ -> },
    onSearchClick: () -> Unit = { },
    filterFavorites: Boolean = false,
    onFilterFavoritesClick: () -> Unit = { },
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    paddingValues: PaddingValues = PaddingValues(),
) {
    val animatedFilterIconColor by animateColorAsState(
        targetValue = if (filterFavorites) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            LocalContentColor.current
        },
        label = "animation for favorite icon color",
    )

    val animatedFilterIconBackgroundColor by animateColorAsState(
        targetValue = if (filterFavorites) {
            MaterialTheme.colorScheme.primary
        } else {
            Transparent
        },
        label = "animation for favorite icon background",
    )

    Column(modifier = Modifier.padding(paddingValues)) {
        TopAppBar(
            title = {
            },
            actions = {
                IconButton(
                    onClick = onSearchClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "favorite",
                    )
                }
                IconButton(
                    onClick = onFilterFavoritesClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = animatedFilterIconBackgroundColor,
                        contentColor = animatedFilterIconColor,
                    ),
                ) {
                    Icon(
                        imageVector = if (filterFavorites) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Filled.FavoriteBorder
                        },
                        contentDescription = "favorite",
                    )
                }
            },
        )
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(size.staggeredGridItemMaxWidth),
            state = state,
            contentPadding = PaddingValues(size.medium),
            verticalItemSpacing = size.medium,
            horizontalArrangement = Arrangement.spacedBy(size.medium),
        ) {
            items(
                items = breeds,
                key = { it.id },
            ) { breed ->
                BreedListItem(
                    name = breed.name,
                    origin = breed.origin,
                    imageUrl = breed.imageUrl,
                    favorite = breed.favorite,
                    onClick = {
                        onClick(breed.id)
                    },
                    onFavoriteClick = {
                        onFavorite(breed.id, !breed.favorite)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BreedListScreenPreview() {
    CatBreedExplorerTheme {
        BreedListScreen(
            breeds = listOf(
                BreedListModel(
                    id = "1",
                    name = "Abyssinian",
                    origin = "Egypt",
                    imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
                    favorite = true,
                ),
            ),
            onClick = { },
            onFavorite = { _, _ -> },
            onSearchClick = { },
            filterFavorites = false,
            onFilterFavoritesClick = { },
        )
    }
}
