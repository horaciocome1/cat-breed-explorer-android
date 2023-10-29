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

import android.os.Build
import io.github.horaciocome1.catbreedexplorer.data.db.AppDataStore
import io.github.horaciocome1.catbreedexplorer.data.db.dao.BreedDao
import io.github.horaciocome1.catbreedexplorer.data.mappers.toBreed
import io.github.horaciocome1.catbreedexplorer.data.mappers.toEntity
import io.github.horaciocome1.catbreedexplorer.data.remote.BreedsService
import io.github.horaciocome1.catbreedexplorer.data.remote.model.Breed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val LIMIT = 10
private const val KEY_LAST_PAGE = "last_page"
private const val FIRST_PAGE = -1

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class BreedsRepository @Inject constructor(
    private val breedDao: BreedDao,
    private val dataStore: AppDataStore,
    private val breedsService: BreedsService,
    private val dispatcher: CoroutineDispatcher,
) {

    private val runtimeCache = mutableMapOf<String, Breed>()

    val breeds = breedDao.getAllBreeds()
        .mapLatest { breeds ->
            breeds.map { it.toBreed() }
        }
        .onEach { breeds ->
            breeds.forEach { breed ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    runtimeCache.putIfAbsent(breed.id, breed)
                } else {
                    runtimeCache[breed.id] = breed
                }
            }
        }
        .flowOn(dispatcher)

    suspend fun saveBreed(breed: Breed) {
        withContext(dispatcher) {
            breedDao.insert(breed.toEntity())
        }
    }

    suspend fun fetchMoreBreeds() {
        withContext(dispatcher) {
            val nextPage = dataStore.read(KEY_LAST_PAGE, FIRST_PAGE) + 1

            breedsService.getBreeds(
                limit = LIMIT,
                page = nextPage,
            ).forEach { breed ->
                saveBreed(breed)
            }

            dataStore.save(KEY_LAST_PAGE, nextPage)
        }
    }

    suspend fun searchByName(name: String) =
        withContext(dispatcher) {
            val breeds = breedsService.getBreeds(name)

            breeds.forEach { breed ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    runtimeCache.putIfAbsent(breed.id, breed)
                } else {
                    runtimeCache[breed.id] = breed
                }
            }

            return@withContext breeds
        }

    suspend fun searchByNameLocally(name: String) =
        withContext(dispatcher) {
            return@withContext breedDao.getAllBreedsLike(name).map { entity ->
                entity.toBreed()
            }
        }

    fun getBreed(id: String) = runtimeCache[id]
}
