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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.horaciocome1.catbreedexplorer.data.repository.BreedsRepository
import io.github.horaciocome1.catbreedexplorer.data.repository.FavoriteBreedsRepository
import io.github.horaciocome1.catbreedexplorer.domain.mappers.toDetailsModel
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedDetailsModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val breedsRepository: BreedsRepository,
    private val favoriteBreedsRepository: FavoriteBreedsRepository,
) : ViewModel() {

    private lateinit var id: String

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var setAsFavoriteJob: Job? = null
    private var unsetAsFavoriteJob: Job? = null
    private var watchFavoriteJob: Job? = null

    fun setId(id: String) {
        this.id = id
        getBreed()
        watchFavorite()
    }

    fun setAsFavorite() {
        if (setAsFavoriteJob?.isActive == true) {
            Timber.w("setAsFavorite job is active")
            return
        }
        setAsFavoriteJob = viewModelScope.launch {
            favoriteBreedsRepository.setAsFavorite(id)
        }
    }

    fun unsetAsFavorite() {
        if (unsetAsFavoriteJob?.isActive == true) {
            Timber.w("unsetAsFavorite job is active")
            return
        }
        unsetAsFavoriteJob = viewModelScope.launch {
            favoriteBreedsRepository.unsetAsFavorite(id)
        }
    }

    private fun getBreed() {
        _uiState.update { currentState ->
            currentState.copy(
                loading = false,
                breed = breedsRepository.getBreed(id)?.toDetailsModel()
                    ?: BreedDetailsModel.EMPTY,
            )
        }
    }

    private fun watchFavorite() {
        watchFavoriteJob?.cancel()
        watchFavoriteJob = viewModelScope.launch {
            favoriteBreedsRepository.isFavorite(id).collectLatest { favorite ->
                _uiState.update { currentState ->
                    currentState.copy(
                        favorite = favorite,
                    )
                }
            }
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val breed: BreedDetailsModel = BreedDetailsModel.EMPTY,
        val favorite: Boolean = false,
    )
}
