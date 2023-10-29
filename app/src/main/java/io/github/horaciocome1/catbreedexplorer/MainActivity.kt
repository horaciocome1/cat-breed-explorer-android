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

package io.github.horaciocome1.catbreedexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint
import io.github.horaciocome1.catbreedexplorer.ui.NavGraphs
import io.github.horaciocome1.catbreedexplorer.ui.destinations.BreedDetailsScreenDestination
import io.github.horaciocome1.catbreedexplorer.ui.destinations.BreedListScreenDestination
import io.github.horaciocome1.catbreedexplorer.ui.destinations.BreedSearchScreenDestination
import io.github.horaciocome1.catbreedexplorer.ui.details.BreedDetailsScreen
import io.github.horaciocome1.catbreedexplorer.ui.details.BreedDetailsViewModel
import io.github.horaciocome1.catbreedexplorer.ui.list.BreedListScreen
import io.github.horaciocome1.catbreedexplorer.ui.list.BreedListViewModel
import io.github.horaciocome1.catbreedexplorer.ui.search.BreedSearchScreen
import io.github.horaciocome1.catbreedexplorer.ui.search.BreedSearchViewModel
import io.github.horaciocome1.catbreedexplorer.ui.theme.CatBreedExplorerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatBreedExplorerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable(BreedListScreenDestination) {
                            val viewModel = hiltViewModel<BreedListViewModel>()

                            val state by viewModel.uiState.collectAsState()

                            val listState = rememberLazyStaggeredGridState()

                            val snackbarHostState = remember { SnackbarHostState() }

                            LaunchedEffect(listState.canScrollForward) {
                                if (!listState.canScrollForward && !viewModel.filterFavorites) {
                                    viewModel.fetchBreeds()
                                }
                            }

                            LaunchedEffect(state.error) {
                                if (state.error) {
                                    snackbarHostState.showSnackbar(
                                        message = state.errorMessage,
                                        duration = if (state.breeds.isEmpty()) {
                                            SnackbarDuration.Indefinite
                                        } else {
                                            SnackbarDuration.Long
                                        },
                                    )
                                }
                            }

                            LaunchedEffect(state.loading) {
                                if (state.loading) {
                                    snackbarHostState.showSnackbar(
                                        message = "Loading...",
                                        duration = SnackbarDuration.Indefinite,
                                    )
                                } else {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                }
                            }

                            Scaffold(
                                snackbarHost = {
                                    SnackbarHost(hostState = snackbarHostState)
                                },
                            ) { paddingValues ->
                                BreedListScreen(
                                    state = listState,
                                    breeds = state.breeds,
                                    onClick = { breedId ->
                                        destinationsNavigator.navigate(BreedDetailsScreenDestination(id = breedId))
                                    },
                                    onFavorite = { breedId, favorite ->
                                        if (favorite) {
                                            viewModel.setAsFavorite(breedId)
                                        } else {
                                            viewModel.unsetAsFavorite(breedId)
                                        }
                                    },
                                    onSearchClick = {
                                        destinationsNavigator.navigate(BreedSearchScreenDestination.route)
                                    },
                                    filterFavorites = viewModel.filterFavorites,
                                    onFilterFavoritesClick = {
                                        viewModel.filterFavorites = !viewModel.filterFavorites
                                    },
                                    paddingValues = paddingValues,
                                )
                            }
                        }

                        composable(BreedSearchScreenDestination) {
                            val viewModel = hiltViewModel<BreedSearchViewModel>()

                            val state by viewModel.uiState.collectAsState()

                            val listState = rememberLazyStaggeredGridState()

                            val snackbarHostState = remember { SnackbarHostState() }

                            LaunchedEffect(state.error) {
                                if (state.error) {
                                    snackbarHostState.showSnackbar(
                                        message = state.errorMessage,
                                        duration = SnackbarDuration.Long,
                                    )
                                }
                            }

                            LaunchedEffect(state.loading) {
                                if (state.loading) {
                                    snackbarHostState.showSnackbar(
                                        message = "Loading...",
                                        duration = SnackbarDuration.Indefinite,
                                    )
                                } else {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                }
                            }

                            Scaffold(
                                snackbarHost = {
                                    SnackbarHost(hostState = snackbarHostState)
                                },
                            ) { paddingValues ->
                                BreedSearchScreen(
                                    navigateUp = destinationsNavigator::navigateUp,
                                    keyword = viewModel.keyword,
                                    onKeywordChange = viewModel::updateKeyword,
                                    state = listState,
                                    breeds = state.breeds,
                                    onClick = { breedId ->
                                        destinationsNavigator.navigate(BreedDetailsScreenDestination(id = breedId))
                                    },
                                    onFavorite = { breedId, favorite ->
                                        if (favorite) {
                                            viewModel.unsetAsFavorite(breedId)
                                        } else {
                                            viewModel.setAsFavorite(breedId)
                                        }
                                    },
                                    paddingValues = paddingValues,
                                )
                            }
                        }

                        composable(BreedDetailsScreenDestination) {
                            val viewModel = hiltViewModel<BreedDetailsViewModel>()

                            val state by viewModel.uiState.collectAsState()

                            LaunchedEffect(navArgs.id) {
                                viewModel.setId(navArgs.id)
                            }

                            BreedDetailsScreen(
                                id = navArgs.id,
                                navigateUp = destinationsNavigator::navigateUp,
                                name = state.breed.name,
                                description = state.breed.description,
                                temperament = state.breed.temperament,
                                origin = state.breed.origin,
                                lifeSpan = state.breed.lifeSpan,
                                imageUrl = state.breed.imageUrl,
                                favorite = state.favorite,
                                onFavoriteClick = {
                                    if (state.favorite) {
                                        viewModel.unsetAsFavorite()
                                    } else {
                                        viewModel.setAsFavorite()
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
