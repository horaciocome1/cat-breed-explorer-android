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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.catbreedexplorer.data.repository.BreedsRepository
import io.github.horaciocome1.catbreedexplorer.data.repository.FavoriteBreedsRepository
import io.github.horaciocome1.catbreedexplorer.domain.mappers.toListModel
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedListModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val breedsRepository: BreedsRepository,
    private val favoriteBreedsRepository: FavoriteBreedsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var filterFavorites by mutableStateOf(false)
    private val filterFavoritesFlow = snapshotFlow { filterFavorites }

    private var fetchBreedsJob: Job? = null
    private var setAsFavoriteJob: Job? = null
    private var unsetAsFavoriteJob: Job? = null

    init {
        watchBreeds()
    }

    fun fetchBreeds() {
        if (fetchBreedsJob?.isActive == true) {
            Timber.w("fetchBreeds job is active")
            return
        }
        fetchBreedsJob = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    loading = true,
                    error = false,
                    errorMessage = "",
                )
            }

            try {
                breedsRepository.fetchMoreBreeds()
            } catch (e: Exception) {
                Timber.e("fetchBreeds", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        error = true,
                        errorMessage = e.message ?: "",
                    )
                }
            } finally {
                _uiState.update { currentState ->
                    currentState.copy(loading = false)
                }
            }
        }
    }

    fun setAsFavorite(id: String) {
        if (setAsFavoriteJob?.isActive == true) {
            Timber.w("setAsFavorite job is active")
            return
        }
        setAsFavoriteJob = viewModelScope.launch {
            favoriteBreedsRepository.setAsFavorite(id)
        }
    }

    fun unsetAsFavorite(id: String) {
        if (unsetAsFavoriteJob?.isActive == true) {
            Timber.w("unsetAsFavorite job is active")
            return
        }
        unsetAsFavoriteJob = viewModelScope.launch {
            favoriteBreedsRepository.unsetAsFavorite(id)
        }
    }

    private fun watchBreeds() {
        combine(
            breedsRepository.breeds,
            favoriteBreedsRepository.breeds,
            filterFavoritesFlow,
        ) { breeds, favoriteBreeds, filterFavorites ->
            _uiState.update { currentState ->
                currentState.copy(
                    loading = breeds.isEmpty(),
                    breeds = breeds.mapNotNull { breed ->
                        val favorite = favoriteBreeds.contains(breed.id)
                        if (filterFavorites && !favorite) return@mapNotNull null
                        breed.toListModel(favorite)
                    },
                )
            }

            if (breeds.isEmpty()) {
                fetchBreeds()
            }
        }.launchIn(viewModelScope)
    }

    data class UiState(
        val loading: Boolean = true,
        val breeds: List<BreedListModel> = emptyList(),
        val error: Boolean = false,
        val errorMessage: String = "",
    )
}
