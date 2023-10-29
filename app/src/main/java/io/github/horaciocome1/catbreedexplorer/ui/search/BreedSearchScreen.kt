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

package io.github.horaciocome1.catbreedexplorer.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedListModel
import io.github.horaciocome1.catbreedexplorer.ui.components.BreedListItem
import io.github.horaciocome1.catbreedexplorer.ui.theme.CatBreedExplorerTheme
import io.github.horaciocome1.catbreedexplorer.ui.theme.size

@Destination
@Composable
fun BreedSearchScreen(
    navigateUp: () -> Unit = { },
    keyword: String = "",
    onKeywordChange: (String) -> Unit = { },
    breeds: List<BreedListModel> = emptyList(),
    onClick: (String) -> Unit = { },
    onFavorite: (String, Boolean) -> Unit = { _, _ -> },
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    paddingValues: PaddingValues = PaddingValues(),
) {
    Scaffold(
        topBar = {
            OutlinedTextField(
                value = keyword,
                onValueChange = onKeywordChange,
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(text = "Search")
                },
                singleLine = true,
                leadingIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "navigate up",
                        )
                    }
                },
                shape = CircleShape,
                modifier = Modifier
                    .padding(size.small)
                    .fillMaxWidth(),
            )
        },
        modifier = Modifier.padding(paddingValues),
    ) { values ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(size.staggeredGridItemMaxWidth),
            state = state,
            contentPadding = PaddingValues(size.medium),
            verticalItemSpacing = size.medium,
            horizontalArrangement = Arrangement.spacedBy(size.medium),
            modifier = Modifier.padding(values),
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
                        onFavorite(breed.id, breed.favorite)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BreedSearchScreenPreview() {
    CatBreedExplorerTheme {
        BreedSearchScreen(
            navigateUp = { },
            keyword = "",
            onKeywordChange = { },
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
        )
    }
}
