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

package io.github.horaciocome1.catbreedexplorer.domain.mappers

import io.github.horaciocome1.catbreedexplorer.data.remote.model.Breed
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedDetailsModel
import io.github.horaciocome1.catbreedexplorer.domain.model.BreedListModel
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BreedMapperTest {

    @Test
    fun `test toListModel`() {
        val breed = Breed(
            id = "id",
            name = "name",
            image = Breed.Image(
                url = "url",
            ),
            origin = "origin",
        )

        val expected = BreedListModel(
            id = "id",
            name = "name",
            imageUrl = "url",
            origin = "origin",
            favorite = false,
        )

        val actual = breed.toListModel(false)

        assertThat(actual, equalTo(expected))
    }

    @Test
    fun `test toDetailsModel`() {
        val breed = Breed(
            id = "id",
            name = "name",
            image = Breed.Image(
                url = "url",
            ),
            origin = "origin",
            description = "description",
            temperament = "temperament",
            lifeSpan = "lifeSpan",
        )

        val expected = BreedDetailsModel(
            id = "id",
            name = "name",
            imageUrl = "url",
            origin = "origin",
            description = "description",
            temperament = "temperament",
            lifeSpan = "lifeSpan",
        )

        val actual = breed.toDetailsModel()

        assertThat(actual, equalTo(expected))
    }
}
