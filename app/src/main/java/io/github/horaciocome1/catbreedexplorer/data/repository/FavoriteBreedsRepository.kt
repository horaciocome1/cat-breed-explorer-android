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
import io.github.horaciocome1.catbreedexplorer.data.db.entities.FavoriteBreedEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class FavoriteBreedsRepository @Inject constructor(
    private val favoriteBreedDao: FavoriteBreedDao,
    private val dispatcher: CoroutineDispatcher,
) {

    val breeds = favoriteBreedDao.getAllBreeds()
        .mapLatest { breeds ->
            breeds.map { it.id }
        }
        .flowOn(dispatcher)

    suspend fun setAsFavorite(id: String) {
        withContext(dispatcher) {
            favoriteBreedDao.insert(FavoriteBreedEntity(id))
        }
    }

    suspend fun unsetAsFavorite(id: String) {
        withContext(dispatcher) {
            favoriteBreedDao.delete(id)
        }
    }

    fun isFavorite(id: String) = favoriteBreedDao.getBreed(id)
        .mapLatest { it != null }
        .flowOn(dispatcher)
}
