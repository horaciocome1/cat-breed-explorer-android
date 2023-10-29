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

package io.github.horaciocome1.catbreedexplorer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.horaciocome1.catbreedexplorer.data.db.entities.BreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(breedEntity: BreedEntity)

    @Query("SELECT * FROM breeds")
    fun getAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE name LIKE '%' || :name || '%'")
    suspend fun getAllBreedsLike(name: String): List<BreedEntity>
}
