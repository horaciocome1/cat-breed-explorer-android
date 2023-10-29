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

package io.github.horaciocome1.catbreedexplorer.data.repository

import io.github.horaciocome1.catbreedexplorer.data.db.dao.FavoriteBreedDao
import io.github.horaciocome1.catbreedexplorer.util.BreedFakeUtil
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteBreedsRepositoryTest {

    private lateinit var repository: FavoriteBreedsRepository

    @MockK
    lateinit var favoriteBreedsDao: FavoriteBreedDao

    private lateinit var dispatcher: CoroutineDispatcher

    private val favoriteBreedEntities by lazy { BreedFakeUtil.getFavoriteBreedEntities() }

    @Before
    fun setUp() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)

        every { favoriteBreedsDao.getAllBreeds() } returns flowOf(favoriteBreedEntities)

        repository = FavoriteBreedsRepository(favoriteBreedsDao, dispatcher)
    }

    @After
    fun tearDown() {
        dispatcher.cancel()
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should return favorite breeds`() {
        runTest {
            val favoriteBreeds = repository.breeds.first()

            assertThat(favoriteBreeds.size, equalTo(favoriteBreedEntities.size))
        }
    }

    @Test
    fun `should return favorite breeds as list of ids`() {
        runTest {
            val favoriteBreeds = repository.breeds.first()

            assertThat(favoriteBreeds[0], equalTo(favoriteBreedEntities[0].id))
        }
    }

    @Test
    fun `unsetAsFavorite should pass correct id to DAO`() {
        runTest {
            val id = BreedFakeUtil.getFavoriteBreedEntities().first().id

            coEvery { favoriteBreedsDao.delete(id) } just Runs

            repository.unsetAsFavorite(id)

            coVerify { favoriteBreedsDao.delete(id) }
        }
    }

    @Test
    fun `isFavorite should emit true for existing favorite`() {
        runTest {
            val entity = BreedFakeUtil.getFavoriteBreedEntities().first()

            every { favoriteBreedsDao.getBreed(entity.id) } returns flowOf(entity)

            val result = repository.isFavorite(entity.id).first()

            assertThat(result, equalTo(true))
        }
    }

    @Test
    fun `isFavorite should emit false for non existing favorite`() {
        runTest {
            val id = "non existent"

            every { favoriteBreedsDao.getBreed(id) } returns flowOf(null)

            val result = repository.isFavorite(id).first()

            assertThat(result, equalTo(false))
        }
    }
}
