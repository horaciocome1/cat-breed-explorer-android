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

import io.github.horaciocome1.catbreedexplorer.data.repository.BreedsRepository
import io.github.horaciocome1.catbreedexplorer.data.repository.FavoriteBreedsRepository
import io.github.horaciocome1.catbreedexplorer.domain.mappers.toListModel
import io.github.horaciocome1.catbreedexplorer.util.BreedFakeUtil
import io.mockk.Awaits
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class BreedSearchViewModelTest {

    @MockK
    lateinit var breedsRepository: BreedsRepository

    @MockK
    lateinit var favoriteBreedsRepository: FavoriteBreedsRepository

    private lateinit var dispatcher: CoroutineDispatcher

    private lateinit var viewModel: BreedSearchViewModel

    private val breeds by lazy { BreedFakeUtil.getBreeds() }

    private val favoriteBreeds by lazy { BreedFakeUtil.getFavoriteBreeds() }

    @Before
    fun setUp() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)

        every { breedsRepository.breeds } returns flowOf(breeds)
        every { favoriteBreedsRepository.breeds } returns flowOf(favoriteBreeds)

        viewModel = BreedSearchViewModel(breedsRepository, favoriteBreedsRepository)
    }

    @After
    fun tearDown() {
        dispatcher.cancel()
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state should be correct`() {
        assertThat(viewModel.uiState.value.loading, equalTo(false))
        assertThat(viewModel.uiState.value.breeds.isEmpty(), equalTo(true))
    }

    @Test
    fun `ensure keyword is set`() {
        val keyword = "word"

        viewModel.updateKeyword(keyword)

        assertThat(viewModel.keyword, equalTo(keyword))
    }

    @Test
    fun `ensure keyword triggers search after debouncing`() {
        runTest {
            val keyword = "word"

            coEvery { breedsRepository.searchByName(keyword) } returns breeds

            viewModel.updateKeyword(keyword)

            launch {
                viewModel.uiState.collectLatest { state ->
                    if (state.breeds.isNotEmpty() && state.breeds[0].favorite) {
                        cancel()
                    }
                }
            }.join()

            assertThat(viewModel.uiState.value.loading, equalTo(false))
            assertThat(viewModel.uiState.value.breeds.size, equalTo(breeds.size))
            assertThat(viewModel.uiState.value.breeds[0], equalTo(breeds[0].toListModel(true)))

            coVerify { breedsRepository.searchByName(keyword) }
        }
    }

    @Test
    fun `ensure when offline falls back to local search`() {
        runTest {
            val keyword = "word"
            coEvery { breedsRepository.searchByName(keyword) } throws UnknownHostException()
            coEvery { breedsRepository.searchByNameLocally(keyword) } returns breeds

            viewModel.updateKeyword(keyword)

            launch {
                viewModel.uiState.collectLatest { state ->
                    if (state.breeds.isNotEmpty() && state.breeds[0].favorite) {
                        cancel()
                    }
                }
            }.join()

            assertThat(viewModel.uiState.value.loading, equalTo(false))
            assertThat(viewModel.uiState.value.breeds.size, equalTo(breeds.size))
            assertThat(viewModel.uiState.value.breeds[0], equalTo(breeds[0].toListModel(true)))

            coVerify { breedsRepository.searchByName(keyword) }
            coVerify { breedsRepository.searchByNameLocally(keyword) }
        }
    }

    @Test
    fun `ensure when offline falls back to local search and returns error if not found`() {
        runTest {
            val keyword = "word"
            val message = "Cannot resolve domain"

            coEvery { breedsRepository.searchByName(keyword) } throws UnknownHostException(message)
            coEvery { breedsRepository.searchByNameLocally(keyword) } returns emptyList()

            viewModel.updateKeyword(keyword)

            launch {
                viewModel.uiState.collectLatest { state ->
                    if (state.error) {
                        cancel()
                    }
                }
            }.join()

            assertThat(viewModel.uiState.value.loading, equalTo(false))
            assertThat(viewModel.uiState.value.error, equalTo(true))
            assertThat(viewModel.uiState.value.errorMessage, equalTo(message))
        }
    }

    @Test
    fun `ensure returns error if there are any exceptions on repository level`() {
        runTest {
            val keyword = "word"
            val message = "Cannot resolve domain"

            coEvery { breedsRepository.searchByName(keyword) } throws Exception(message)

            viewModel.updateKeyword(keyword)

            launch {
                viewModel.uiState.collectLatest { state ->
                    if (state.error) {
                        cancel()
                    }
                }
            }.join()

            assertThat(viewModel.uiState.value.loading, equalTo(false))
            assertThat(viewModel.uiState.value.error, equalTo(true))
            assertThat(viewModel.uiState.value.errorMessage, equalTo(message))
        }
    }

    @Test
    fun `ensure setAsFavorite passes correct id to repository`() {
        runTest {
            val id = breeds[0].id

            coEvery { favoriteBreedsRepository.setAsFavorite(id) } just Runs

            viewModel.setAsFavorite(id)

            advanceUntilIdle()

            coVerify { favoriteBreedsRepository.setAsFavorite(id) }
        }
    }

    @Test
    fun `ensure setAsFavorite caches the breed`() {
        runTest {
            val keyword = "word"
            val breed = breeds[0]
            val id = breeds[0].id

            coEvery { breedsRepository.searchByName(keyword) } returns breeds
            coEvery { breedsRepository.saveBreed(breed) } just Runs
            coEvery { favoriteBreedsRepository.setAsFavorite(id) } just Runs

            viewModel.updateKeyword(keyword)

            launch {
                viewModel.uiState.collectLatest { state ->
                    if (state.breeds.isNotEmpty() && state.breeds[0].favorite) {
                        cancel()
                    }
                }
            }.join()

            viewModel.setAsFavorite(id)

            advanceUntilIdle()

            coVerify { breedsRepository.saveBreed(breed) }
        }
    }

    @Test
    fun `ensure setAsFavorite doesn't span multiple jobs at a given time`() {
        runTest {
            coEvery { favoriteBreedsRepository.setAsFavorite(any()) } just Awaits
            coEvery { breedsRepository.saveBreed(any()) } just Awaits

            for (index in 0..2) {
                launch {
                    viewModel.setAsFavorite("id$index")
                }
            }

            advanceUntilIdle()

            coVerify(exactly = 1) { favoriteBreedsRepository.setAsFavorite(any()) }
        }
    }

    @Test
    fun `ensure unsetAsFavorite passes correct id to repository`() {
        runTest {
            val id = breeds[0].id

            coEvery { favoriteBreedsRepository.unsetAsFavorite(id) } just Runs

            viewModel.unsetAsFavorite(id)

            advanceUntilIdle()

            coVerify { favoriteBreedsRepository.unsetAsFavorite(id) }
        }
    }

    @Test
    fun `ensure unsetAsFavorite doesn't span multiple jobs at a given time`() {
        runTest {
            coEvery { favoriteBreedsRepository.unsetAsFavorite(any()) } just Awaits

            for (index in 0..2) {
                launch {
                    viewModel.unsetAsFavorite("id$index")
                }
            }

            advanceUntilIdle()

            coVerify(exactly = 1) { favoriteBreedsRepository.unsetAsFavorite(any()) }
        }
    }
}
