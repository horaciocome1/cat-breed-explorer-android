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

package io.github.horaciocome1.catbreedexplorer.ui.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import io.github.horaciocome1.catbreedexplorer.R
import io.github.horaciocome1.catbreedexplorer.ui.theme.CatBreedExplorerTheme
import io.github.horaciocome1.catbreedexplorer.ui.theme.LightGrey
import io.github.horaciocome1.catbreedexplorer.ui.theme.size

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    @Suppress("UNUSED_PARAMETER") id: String, // required for navigation auto generated args
    navigateUp: () -> Unit = { },
    name: String = "",
    imageUrl: String = "",
    description: String = "",
    temperament: String = "",
    origin: String = "",
    lifeSpan: String = "",
    favorite: Boolean = true,
    onFavoriteClick: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "navigate up",
                        )
                    }
                },
                title = {
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "$name image",
                    filterQuality = FilterQuality.High,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(),
                )
                IconButton(
                    onClick = onFavoriteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(size.medium)
                        .border(
                            border = BorderStroke(size.one, LightGrey),
                            shape = CircleShape,
                        ),
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
            Text(
                text = stringResource(R.string.breed_details_main_headline, name, origin, lifeSpan),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(
                    top = size.large,
                    end = size.medium,
                    start = size.medium,
                ),
            )
            Text(
                text = stringResource(R.string.breed_details_about_headline),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
                modifier = Modifier.padding(
                    top = size.large,
                    end = size.medium,
                    start = size.medium,
                ),
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    top = size.small,
                    end = size.medium,
                    start = size.medium,
                ),
            )
            Text(
                text = stringResource(R.string.breed_details_temperament_headline),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
                modifier = Modifier.padding(
                    top = size.large,
                    end = size.medium,
                    start = size.medium,
                ),
            )
            Text(
                text = temperament,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    top = size.small,
                    end = size.medium,
                    bottom = size.extraLarge,
                    start = size.medium,
                ),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BreedDetailsScreenPreview() {
    CatBreedExplorerTheme {
        BreedDetailsScreen(
            id = "abys",
            navigateUp = { },
            name = "Abyssinian",
            imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
            description = "A lovely Abyssinian",
            temperament = "Playful",
            origin = "Egypt",
            lifeSpan = "14 - 15",
            favorite = false,
            onFavoriteClick = { },
        )
    }
}
