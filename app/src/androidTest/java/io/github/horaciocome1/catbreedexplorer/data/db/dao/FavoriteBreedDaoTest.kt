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

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.horaciocome1.catbreedexplorer.data.db.AppDatabase
import io.github.horaciocome1.catbreedexplorer.data.db.entities.FavoriteBreedEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FavoriteBreedDaoTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase
    private lateinit var favoriteBreedDao: FavoriteBreedDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
        ).build()

        favoriteBreedDao = db.favoriteBreedDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun favoriteBreedIsCached() {
        runTest {
            val id = "breed"
            val entity = FavoriteBreedEntity(id = id)
            val createdAt = entity.createdAt

            favoriteBreedDao.insert(entity)

            val storedBreeds = favoriteBreedDao.getAllBreeds().first()

            assertThat(storedBreeds.size, equalTo(1))
            assertThat(storedBreeds[0].id, equalTo(id))
            assertThat(storedBreeds[0].createdAt, equalTo(createdAt))
        }
    }

    @Test
    @Throws(Exception::class)
    fun emptyTableReturnsEmptyList() {
        runTest {
            val storedBreeds = favoriteBreedDao.getAllBreeds().first()

            assertThat(storedBreeds.size, equalTo(0))
        }
    }

    @Test
    @Throws(Exception::class)
    fun favoriteBreedIsDeleted() {
        runTest {
            val id = "breed"
            val entity = FavoriteBreedEntity(id = id)
            val createdAt = entity.createdAt

            favoriteBreedDao.insert(entity)

            val storedBreeds = favoriteBreedDao.getAllBreeds().first()

            assertThat(storedBreeds.size, equalTo(1))
            assertThat(storedBreeds[0].id, equalTo(id))
            assertThat(storedBreeds[0].createdAt, equalTo(createdAt))

            favoriteBreedDao.delete(entity.id)

            val storedBreedsAfterDelete = favoriteBreedDao.getAllBreeds().first()

            assertThat(storedBreedsAfterDelete.size, equalTo(0))
        }
    }

    @Test
    @Throws(Exception::class)
    fun existentBreedIsReturned() {
        runTest {
            val id = "breed"
            val entity = FavoriteBreedEntity(id = id)
            val createdAt = entity.createdAt

            favoriteBreedDao.insert(entity)

            val storedBreed = favoriteBreedDao.getBreed(id).first()

            assertThat(storedBreed?.id, equalTo(id))
            assertThat(storedBreed?.createdAt, equalTo(createdAt))
        }
    }

    @Test
    @Throws(Exception::class)
    fun nonExistentBreedIsReturned() {
        runTest {
            val storedBreed = favoriteBreedDao.getBreed("id").first()

            assertThat(storedBreed, equalTo(null))
        }
    }

    @Test
    @Throws(Exception::class)
    fun sortingIsByCreationDateDescending() {
        runTest {
            val breeds = buildList {
                for (index in 0..3) {
                    val createdAt = System.currentTimeMillis() + index
                    add(
                        element = FavoriteBreedEntity(
                            id = "$index",
                            createdAt = createdAt,
                        ),
                    )
                }
            }
            breeds.forEach { favoriteBreedDao.insert(it) }

            val storedBreeds = favoriteBreedDao.getAllBreeds().first()

            assertThat(storedBreeds.size, equalTo(breeds.size))

            for (index in 0..3) {
                assertThat(storedBreeds[index].id, equalTo("${3 - index}"))
            }
        }
    }
}
