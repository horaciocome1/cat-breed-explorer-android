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

package io.github.horaciocome1.catbreedexplorer.data.mappers

import io.github.horaciocome1.catbreedexplorer.util.BreedFakeUtil
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BreedMapperTest {

    @Test
    fun `toBreedEntity() maps Breed to BreedEntity`() {
        val breed = BreedFakeUtil.getBreed()

        val breedEntity = breed.toEntity()

        assertThat(breed.id, equalTo(breedEntity.id))
        assertThat(breed.weight.imperial, equalTo(breedEntity.weightImperial))
        assertThat(breed.weight.metric, equalTo(breedEntity.weightMetric))
        assertThat(breed.name, equalTo(breedEntity.name))
        assertThat(breed.cfaUrl, equalTo(breedEntity.cfaUrl))
        assertThat(breed.vetstreetUrl, equalTo(breedEntity.vetstreetUrl))
        assertThat(breed.vcahospitalsUrl, equalTo(breedEntity.vcahospitalsUrl))
        assertThat(breed.temperament, equalTo(breedEntity.temperament))
        assertThat(breed.origin, equalTo(breedEntity.origin))
        assertThat(breed.countryCodes, equalTo(breedEntity.countryCodes))
        assertThat(breed.countryCode, equalTo(breedEntity.countryCode))
        assertThat(breed.description, equalTo(breedEntity.description))
        assertThat(breed.lifeSpan, equalTo(breedEntity.lifeSpan))
        assertThat(breed.indoor, equalTo(breedEntity.indoor))
        assertThat(breed.lap, equalTo(breedEntity.lap))
        assertThat(breed.altNames, equalTo(breedEntity.altNames))
        assertThat(breed.adaptability, equalTo(breedEntity.adaptability))
        assertThat(breed.affectionLevel, equalTo(breedEntity.affectionLevel))
        assertThat(breed.childFriendly, equalTo(breedEntity.childFriendly))
        assertThat(breed.dogFriendly, equalTo(breedEntity.dogFriendly))
        assertThat(breed.energyLevel, equalTo(breedEntity.energyLevel))
        assertThat(breed.grooming, equalTo(breedEntity.grooming))
        assertThat(breed.healthIssues, equalTo(breedEntity.healthIssues))
        assertThat(breed.intelligence, equalTo(breedEntity.intelligence))
        assertThat(breed.sheddingLevel, equalTo(breedEntity.sheddingLevel))
        assertThat(breed.socialNeeds, equalTo(breedEntity.socialNeeds))
        assertThat(breed.strangerFriendly, equalTo(breedEntity.strangerFriendly))
        assertThat(breed.vocalisation, equalTo(breedEntity.vocalisation))
        assertThat(breed.experimental, equalTo(breedEntity.experimental))
        assertThat(breed.hairless, equalTo(breedEntity.hairless))
        assertThat(breed.natural, equalTo(breedEntity.natural))
        assertThat(breed.rare, equalTo(breedEntity.rare))
        assertThat(breed.image.id, equalTo(breedEntity.imageId))
        assertThat(breed.image.width, equalTo(breedEntity.imageWidth))
        assertThat(breed.image.height, equalTo(breedEntity.imageHeight))
        assertThat(breed.image.url, equalTo(breedEntity.imageUrl))
    }

    @Test
    fun `toBreed() maps BreedEntity to Breed`() {
        val breedEntity = BreedFakeUtil.getBreed().toEntity()

        val breed = breedEntity.toBreed()

        assertThat(breed.id, equalTo(breedEntity.id))
        assertThat(breed.weight.imperial, equalTo(breedEntity.weightImperial))
        assertThat(breed.weight.metric, equalTo(breedEntity.weightMetric))
        assertThat(breed.name, equalTo(breedEntity.name))
        assertThat(breed.cfaUrl, equalTo(breedEntity.cfaUrl))
        assertThat(breed.vetstreetUrl, equalTo(breedEntity.vetstreetUrl))
        assertThat(breed.vcahospitalsUrl, equalTo(breedEntity.vcahospitalsUrl))
        assertThat(breed.temperament, equalTo(breedEntity.temperament))
        assertThat(breed.origin, equalTo(breedEntity.origin))
        assertThat(breed.countryCodes, equalTo(breedEntity.countryCodes))
        assertThat(breed.countryCode, equalTo(breedEntity.countryCode))
        assertThat(breed.description, equalTo(breedEntity.description))
        assertThat(breed.lifeSpan, equalTo(breedEntity.lifeSpan))
        assertThat(breed.indoor, equalTo(breedEntity.indoor))
        assertThat(breed.lap, equalTo(breedEntity.lap))
        assertThat(breed.altNames, equalTo(breedEntity.altNames))
        assertThat(breed.adaptability, equalTo(breedEntity.adaptability))
        assertThat(breed.affectionLevel, equalTo(breedEntity.affectionLevel))
        assertThat(breed.childFriendly, equalTo(breedEntity.childFriendly))
        assertThat(breed.dogFriendly, equalTo(breedEntity.dogFriendly))
        assertThat(breed.energyLevel, equalTo(breedEntity.energyLevel))
        assertThat(breed.grooming, equalTo(breedEntity.grooming))
        assertThat(breed.healthIssues, equalTo(breedEntity.healthIssues))
        assertThat(breed.intelligence, equalTo(breedEntity.intelligence))
        assertThat(breed.sheddingLevel, equalTo(breedEntity.sheddingLevel))
        assertThat(breed.socialNeeds, equalTo(breedEntity.socialNeeds))
        assertThat(breed.strangerFriendly, equalTo(breedEntity.strangerFriendly))
        assertThat(breed.vocalisation, equalTo(breedEntity.vocalisation))
        assertThat(breed.experimental, equalTo(breedEntity.experimental))
        assertThat(breed.hairless, equalTo(breedEntity.hairless))
        assertThat(breed.natural, equalTo(breedEntity.natural))
        assertThat(breed.rare, equalTo(breedEntity.rare))
        assertThat(breed.image.id, equalTo(breedEntity.imageId))
        assertThat(breed.image.width, equalTo(breedEntity.imageWidth))
        assertThat(breed.image.height, equalTo(breedEntity.imageHeight))
        assertThat(breed.image.url, equalTo(breedEntity.imageUrl))
    }
}
