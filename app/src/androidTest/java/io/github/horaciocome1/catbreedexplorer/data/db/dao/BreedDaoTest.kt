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
import io.github.horaciocome1.catbreedexplorer.data.mappers.toEntity
import io.github.horaciocome1.catbreedexplorer.data.remote.model.Breed
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
class BreedDaoTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase
    private lateinit var breedDao: BreedDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
        ).build()

        breedDao = db.breedDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun breedIsCached() {
        runTest {
            val id = "breed"
            val breed = Breed(id = id)

            breedDao.insert(breed.toEntity())

            val storedBreeds = breedDao.getAllBreeds().first()

            assertThat(storedBreeds.size, equalTo(1))
            assertThat(storedBreeds[0].id, equalTo(id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun breedIsSearchable() {
        runTest {
            val id = "breed"
            val name = "My Breed"
            val breed = Breed(id = id, name = name)

            breedDao.insert(breed.toEntity())

            val storedBreeds = breedDao.getAllBreedsLike(name)

            assertThat(storedBreeds.size, equalTo(1))
            assertThat(storedBreeds[0].id, equalTo(id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun emptyTableReturnsEmptyList() {
        runTest {
            val storedBreeds = breedDao.getAllBreeds().first()

            assertThat(storedBreeds.size, equalTo(0))
        }
    }
}
