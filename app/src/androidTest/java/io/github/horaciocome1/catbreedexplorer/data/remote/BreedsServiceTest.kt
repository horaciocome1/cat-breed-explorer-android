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

package io.github.horaciocome1.catbreedexplorer.data.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.horaciocome1.catbreedexplorer.data.remote.impl.BreedsServiceImpl
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BreedsServiceTest {

    private lateinit var breedsService: BreedsService

    @Before
    fun setup() {
        breedsService = BreedsServiceImpl()
    }

    @Test
    @Throws(Exception::class)
    fun apiReturnsData() {
        runTest {
            val result = breedsService.getBreeds(
                limit = 10,
                page = 0,
            )

            assertThat(result.size, equalTo(10))
        }
    }

    @Test
    @Throws(Exception::class)
    fun existentBreedNameReturnsMatches() {
        runTest {
            val name = "Abyssinian"

            val result = breedsService.getBreeds(name)

            assertThat(result.size, equalTo(1))
            assertThat(result[0].name, equalTo(name))
        }
    }

    @Test
    @Throws(Exception::class)
    fun nonExistentBreedNameReturnsEmptyList() {
        runTest {
            val name = "AbyssinianQuaQuaQua"

            val result = breedsService.getBreeds(name)

            assertThat(result.size, equalTo(0))
        }
    }

    @Test
    @Throws(Exception::class)
    fun apiTakesNameWithSpaces() {
        runTest {
            val name = "Devon Rex"

            val result = breedsService.getBreeds(name)

            assertThat(result.size, equalTo(1))
            assertThat(result[0].name, equalTo(name))
        }
    }
}
