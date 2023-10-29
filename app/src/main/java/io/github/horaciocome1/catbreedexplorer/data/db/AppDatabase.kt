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

package io.github.horaciocome1.catbreedexplorer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.horaciocome1.catbreedexplorer.data.db.dao.BreedDao
import io.github.horaciocome1.catbreedexplorer.data.db.dao.FavoriteBreedDao
import io.github.horaciocome1.catbreedexplorer.data.db.entities.BreedEntity
import io.github.horaciocome1.catbreedexplorer.data.db.entities.FavoriteBreedEntity

private const val DATABASE_VERSION = 1

@Database(
    entities = [BreedEntity::class, FavoriteBreedEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDao

    abstract fun favoriteBreedDao(): FavoriteBreedDao
}
