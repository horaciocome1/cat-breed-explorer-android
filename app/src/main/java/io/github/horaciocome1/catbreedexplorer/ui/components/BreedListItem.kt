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

package io.github.horaciocome1.catbreedexplorer.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import io.github.horaciocome1.catbreedexplorer.R
import io.github.horaciocome1.catbreedexplorer.ui.theme.CatBreedExplorerTheme
import io.github.horaciocome1.catbreedexplorer.ui.theme.LightGrey
import io.github.horaciocome1.catbreedexplorer.ui.theme.size

@Composable
fun BreedListItem(
    name: String,
    origin: String,
    imageUrl: String,
    favorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(size.one, LightGrey),
        onClick = onClick,
    ) {
        Box {
            Column {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "$name image",
                    filterQuality = FilterQuality.Low,
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(
                        top = size.large,
                        start = size.medium,
                        end = size.medium,
                    ),
                )
                Text(
                    text = stringResource(R.string.breed_list_item_origin, origin),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        top = size.small,
                        start = size.medium,
                        end = size.medium,
                        bottom = size.large,
                    ),
                )
            }
            IconButton(
                onClick = onFavoriteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .padding(size.small)
                    .align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = if (favorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = "favorite",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BreedItemPreview() {
    CatBreedExplorerTheme {
        BreedListItem(
            name = "Abyssinian",
            origin = "Egypt",
            imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
            favorite = false,
            onClick = { },
            onFavoriteClick = { },
        )
    }
}
