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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.catbreedexplorer.data.remote.model.Breed
import io.github.horaciocome1.catbreedexplorer.data.repository.BreedsRepository
import io.github.horaciocome1.catbreedexplorer.data.repository.FavoriteBreedsRepository
import io.github.horaciocome1.catbreedexplorer.domain.mappers.toListModel
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedListModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

private const val KEYSTROKE_DEBOUNCE_MILLIS = 1350L

@OptIn(FlowPreview::class)
@HiltViewModel
class BreedSearchViewModel @Inject constructor(
    private val breedsRepository: BreedsRepository,
    private val favoriteBreedsRepository: FavoriteBreedsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val searchResult = MutableStateFlow<List<Breed>>(emptyList())

    var keyword by mutableStateOf("")
        private set
    private val keywordFlow = snapshotFlow { keyword }

    private var setAsFavoriteJob: Job? = null
    private var unsetAsFavoriteJob: Job? = null

    init {
        watchKeyword()
        watchSearchResult()
    }

    fun updateKeyword(keyword: String) {
        this.keyword = keyword
    }

    fun setAsFavorite(id: String) {
        if (setAsFavoriteJob?.isActive == true) {
            Timber.w("setAsFavorite job is active")
            return
        }
        setAsFavoriteJob = viewModelScope.launch {
            launch {
                favoriteBreedsRepository.setAsFavorite(id)
            }
            launch {
                val breed = searchResult.value.find { it.id == id }
                if (breed != null) {
                    breedsRepository.saveBreed(breed)
                }
            }
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

    private fun watchKeyword() {
        viewModelScope.launch {
            keywordFlow
                .onEach { keyword ->
                    searchResult.update { emptyList() }

                    _uiState.update { currentState ->
                        currentState.copy(
                            loading = keyword.isNotBlank(),
                            breeds = emptyList(),
                        )
                    }
                }
                .debounce(KEYSTROKE_DEBOUNCE_MILLIS)
                .collectLatest { keyword ->
                    if (keyword.isBlank()) {
                        return@collectLatest
                    }

                    try {
                        searchResult.update {
                            breedsRepository.searchByName(keyword)
                        }
                    } catch (e: UnknownHostException) {
                        Timber.e("watchKeyword.searchByName", e)
                        val breeds = breedsRepository.searchByNameLocally(keyword)
                        if (breeds.isEmpty()) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    error = true,
                                    errorMessage = e.message ?: "",
                                )
                            }
                        } else {
                            searchResult.update { breeds }
                        }
                    } catch (e: Exception) {
                        Timber.e("watchKeyword.searchByName", e)
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
    }

    private fun watchSearchResult() {
        combine(searchResult, favoriteBreedsRepository.breeds) { breeds, favoriteBreeds ->
            _uiState.update { currentState ->
                currentState.copy(
                    loading = false,
                    breeds = breeds.map { breed ->
                        breed.toListModel(favorite = favoriteBreeds.contains(breed.id))
                    },
                )
            }
        }.launchIn(viewModelScope)
    }

    data class UiState(
        val loading: Boolean = false,
        val breeds: List<BreedListModel> = emptyList(),
        val error: Boolean = false,
        val errorMessage: String = "",
    )
}
