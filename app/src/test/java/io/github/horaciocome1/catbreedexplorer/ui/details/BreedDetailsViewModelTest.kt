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

import io.github.horaciocome1.catbreedexplorer.data.repository.BreedsRepository
import io.github.horaciocome1.catbreedexplorer.data.repository.FavoriteBreedsRepository
import io.github.horaciocome1.catbreedexplorer.domain.mappers.toDetailsModel
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedDetailsModel
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
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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

@ExperimentalCoroutinesApi
class BreedDetailsViewModelTest {

    @MockK
    lateinit var breedsRepository: BreedsRepository

    @MockK
    lateinit var favoriteBreedsRepository: FavoriteBreedsRepository

    private lateinit var dispatcher: CoroutineDispatcher

    private lateinit var viewModel: BreedDetailsViewModel

    private val breed by lazy { BreedFakeUtil.getBreed() }

    @Before
    fun setUp() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = BreedDetailsViewModel(breedsRepository, favoriteBreedsRepository)
    }

    @After
    fun tearDown() {
        dispatcher.cancel()
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state should be correct`() {
        assertThat(viewModel.uiState.value.loading, equalTo(true))
        assertThat(viewModel.uiState.value.breed.id.isBlank(), equalTo(true))
        assertThat(viewModel.uiState.value.favorite, equalTo(false))
    }

    @Test
    fun `ensure setId gets breed and its favorite state`() {
        runTest {
            every { breedsRepository.getBreed(breed.id) } returns breed
            every { favoriteBreedsRepository.isFavorite(breed.id) } returns flowOf(true)

            viewModel.setId(breed.id)

            // waits until the breed and favorite state are set
            launch {
                viewModel.uiState.collect { state ->
                    if (state.favorite) {
                        cancel()
                    }
                }
            }.join()

            assertThat(viewModel.uiState.value.loading, equalTo(false))
            assertThat(viewModel.uiState.value.breed, equalTo(breed.toDetailsModel()))
            assertThat(viewModel.uiState.value.favorite, equalTo(true))

            verify { breedsRepository.getBreed(breed.id) }
            verify { favoriteBreedsRepository.isFavorite(breed.id) }
        }
    }

    @Test
    fun `ensure setId for non existent breed returns empty`() {
        runTest {
            every { breedsRepository.getBreed(breed.id) } returns null
            every { favoriteBreedsRepository.isFavorite(breed.id) } returns flowOf(true)

            viewModel.setId(breed.id)

            // waits until the breed and favorite state are set
            launch {
                viewModel.uiState.collect { state ->
                    if (state.favorite) {
                        cancel()
                    }
                }
            }.join()

            assertThat(viewModel.uiState.value.loading, equalTo(false))
            assertThat(viewModel.uiState.value.breed, equalTo(BreedDetailsModel.EMPTY))
            assertThat(viewModel.uiState.value.favorite, equalTo(true))
        }
    }

    @Test
    fun `ensure setAsFavorite passes correct id to repository`() {
        runTest {
            every { breedsRepository.getBreed(breed.id) } returns breed
            every { favoriteBreedsRepository.isFavorite(breed.id) } returns flowOf(true)

            viewModel.setId(breed.id)

            coEvery { favoriteBreedsRepository.setAsFavorite(breed.id) } just Runs

            viewModel.setAsFavorite()

            advanceUntilIdle()

            coVerify { favoriteBreedsRepository.setAsFavorite(breed.id) }
        }
    }

    @Test
    fun `ensure setAsFavorite doesn't span multiple jobs at a given time`() {
        runTest {
            every { breedsRepository.getBreed(breed.id) } returns breed
            coEvery { favoriteBreedsRepository.setAsFavorite(breed.id) } just Awaits
            every { favoriteBreedsRepository.isFavorite(breed.id) } returns flowOf(true)

            viewModel.setId(breed.id)

            viewModel.setAsFavorite()

            for (index in 0..1) {
                launch {
                    viewModel.setAsFavorite()
                }
            }

            advanceUntilIdle()

            coVerify(exactly = 1) { favoriteBreedsRepository.setAsFavorite(any()) }
        }
    }

    @Test
    fun `ensure unsetAsFavorite passes correct id to repository`() {
        runTest {
            every { breedsRepository.getBreed(breed.id) } returns breed
            every { favoriteBreedsRepository.isFavorite(breed.id) } returns flowOf(true)

            viewModel.setId(breed.id)

            coEvery { favoriteBreedsRepository.unsetAsFavorite(breed.id) } just Runs

            viewModel.unsetAsFavorite()

            advanceUntilIdle()

            coVerify { favoriteBreedsRepository.unsetAsFavorite(breed.id) }
        }
    }

    @Test
    fun `ensure unsetAsFavorite doesn't span multiple jobs at a given time`() {
        runTest {
            every { breedsRepository.getBreed(breed.id) } returns breed
            coEvery { favoriteBreedsRepository.unsetAsFavorite(breed.id) } just Awaits
            every { favoriteBreedsRepository.isFavorite(breed.id) } returns flowOf(true)

            viewModel.setId(breed.id)

            viewModel.unsetAsFavorite()

            for (index in 0..1) {
                launch {
                    viewModel.unsetAsFavorite()
                }
            }

            advanceUntilIdle()

            coVerify(exactly = 1) { favoriteBreedsRepository.unsetAsFavorite(any()) }
        }
    }
}
