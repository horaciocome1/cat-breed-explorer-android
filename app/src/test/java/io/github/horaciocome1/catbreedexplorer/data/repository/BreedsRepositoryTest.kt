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

import io.github.horaciocome1.catbreedexplorer.data.db.AppDataStore
import io.github.horaciocome1.catbreedexplorer.data.db.dao.BreedDao
import io.github.horaciocome1.catbreedexplorer.data.mappers.toEntity
import io.github.horaciocome1.catbreedexplorer.data.remote.BreedsService
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
class BreedsRepositoryTest {

    private lateinit var repository: BreedsRepository

    @MockK
    lateinit var breedsService: BreedsService

    @MockK
    lateinit var breedsDao: BreedDao

    @MockK
    lateinit var dataStore: AppDataStore

    private lateinit var dispatcher: CoroutineDispatcher

    private val breeds by lazy { BreedFakeUtil.getBreeds() }

    private val entities by lazy { BreedFakeUtil.getBreedEntities() }

    @Before
    fun setUp() {
        dispatcher = StandardTestDispatcher()

        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)

        every { breedsDao.getAllBreeds() } returns flowOf(entities)

        repository = BreedsRepository(
            breedDao = breedsDao,
            dataStore = dataStore,
            breedsService = breedsService,
            dispatcher = dispatcher,
        )
    }

    @After
    fun tearDown() {
        dispatcher.cancel()
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should return a list of breeds`() {
        runTest {
            val breeds = repository.breeds.first()

            assertThat(breeds.size, equalTo(entities.size))
            assertThat(breeds[0].id, equalTo(entities[0].id))
        }
    }

    @Test
    fun `saveBreed should persist the breed`() {
        runTest {
            val breed = BreedFakeUtil.getBreed()

            coEvery { breedsDao.insert(breed.toEntity()) } just Runs

            repository.saveBreed(breed)

            coVerify { breedsDao.insert(breed.toEntity()) }
        }
    }

    @Test
    fun `getMoreBreads should call service with incremented page`() {
        runTest {
            val page = -1
            val incrementedPage = page + 1

            coEvery { dataStore.read(any(), any()) } returns page
            coEvery { breedsService.getBreeds(any(), incrementedPage) } returns breeds
            coEvery { breedsDao.insert(any()) } just Runs
            coEvery { dataStore.save(any(), any()) } just Runs

            repository.fetchMoreBreeds()

            coVerify { breedsService.getBreeds(any(), incrementedPage) }
        }
    }

    @Test
    fun `getMoreBreads should save incremented page`() {
        runTest {
            val page = -1
            val incrementedPage = page + 1

            coEvery { dataStore.read(any(), any()) } returns page
            coEvery { breedsService.getBreeds(any(), any()) } returns breeds
            coEvery { breedsDao.insert(any()) } just Runs
            coEvery { dataStore.save(any(), incrementedPage) } just Runs

            repository.fetchMoreBreeds()

            coVerify { dataStore.save(any(), incrementedPage) }
        }
    }

    @Test
    fun `search should parse name correctly`() {
        runTest {
            val name = "name"
            val expected = listOf(BreedFakeUtil.getBreed())

            coEvery { breedsService.getBreeds(name) } returns expected

            repository.searchByName(name)

            coVerify { breedsService.getBreeds(name) }
        }
    }

    @Test
    fun `search locally should parse name correctly`() {
        runTest {
            val name = "name"
            val expected = BreedFakeUtil.getBreedEntities()

            coEvery { breedsDao.getAllBreedsLike(name) } returns expected

            repository.searchByNameLocally(name)

            coVerify { breedsDao.getAllBreedsLike(name) }
        }
    }

    @Test
    fun `search should parse result correctly`() {
        runTest {
            val name = "name"
            val expected = listOf(BreedFakeUtil.getBreed())

            coEvery { breedsService.getBreeds(name) } returns expected

            val result = repository.searchByName(name)

            assertThat(result.size, equalTo(expected.size))
            assertThat(result[0].id, equalTo(expected[0].id))
        }
    }

    @Test
    fun `search locally should parse result correctly`() {
        runTest {
            val name = "name"
            val expected = BreedFakeUtil.getBreedEntities()

            coEvery { breedsDao.getAllBreedsLike(name) } returns expected

            val result = repository.searchByNameLocally(name)

            assertThat(result.size, equalTo(expected.size))
            assertThat(result[0].id, equalTo(expected[0].id))
        }
    }

    @Test
    fun `get existent breed should return it`() {
        runTest {
            repository.breeds.first()

            val result = repository.getBreed(BreedFakeUtil.getBreed().id)

            assertThat(result != null, equalTo(true))
        }
    }

    @Test
    fun `get non existent breed should return null`() {
        runTest {
            repository.breeds.first()

            val result = repository.getBreed("expected non existent id")

            assertThat(result != null, equalTo(false))
        }
    }
}
