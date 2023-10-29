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

import io.github.horaciocome1.catbreedexplorer.data.db.entities.BreedEntity
import io.github.horaciocome1.catbreedexplorer.data.remote.model.Breed

fun Breed.toEntity(): BreedEntity {
    return BreedEntity(
        id = id,
        weightImperial = weight.imperial,
        weightMetric = weight.metric,
        name = name,
        cfaUrl = cfaUrl,
        vetstreetUrl = vetstreetUrl,
        vcahospitalsUrl = vcahospitalsUrl,
        temperament = temperament,
        origin = origin,
        countryCodes = countryCodes,
        countryCode = countryCode,
        description = description,
        lifeSpan = lifeSpan,
        indoor = indoor,
        lap = lap,
        altNames = altNames,
        adaptability = adaptability,
        affectionLevel = affectionLevel,
        childFriendly = childFriendly,
        dogFriendly = dogFriendly,
        energyLevel = energyLevel,
        grooming = grooming,
        healthIssues = healthIssues,
        intelligence = intelligence,
        sheddingLevel = sheddingLevel,
        socialNeeds = socialNeeds,
        strangerFriendly = strangerFriendly,
        vocalisation = vocalisation,
        experimental = experimental,
        hairless = hairless,
        natural = natural,
        rare = rare,
        rex = rex,
        suppressedTail = suppressedTail,
        shortLegs = shortLegs,
        wikipediaUrl = wikipediaUrl,
        hypoallergenic = hypoallergenic,
        referenceImageId = referenceImageId,
        imageId = image.id,
        imageWidth = image.width,
        imageHeight = image.height,
        imageUrl = image.url,
    )
}

fun BreedEntity.toBreed(): Breed {
    return Breed(
        id = id,
        weight = Breed.Weight(
            imperial = weightImperial,
            metric = weightMetric,
        ),
        name = name,
        cfaUrl = cfaUrl,
        vetstreetUrl = vetstreetUrl,
        vcahospitalsUrl = vcahospitalsUrl,
        temperament = temperament,
        origin = origin,
        countryCodes = countryCodes,
        countryCode = countryCode,
        description = description,
        lifeSpan = lifeSpan,
        indoor = indoor,
        lap = lap,
        altNames = altNames,
        adaptability = adaptability,
        affectionLevel = affectionLevel,
        childFriendly = childFriendly,
        dogFriendly = dogFriendly,
        energyLevel = energyLevel,
        grooming = grooming,
        healthIssues = healthIssues,
        intelligence = intelligence,
        sheddingLevel = sheddingLevel,
        socialNeeds = socialNeeds,
        strangerFriendly = strangerFriendly,
        vocalisation = vocalisation,
        experimental = experimental,
        hairless = hairless,
        natural = natural,
        rare = rare,
        rex = rex,
        suppressedTail = suppressedTail,
        shortLegs = shortLegs,
        wikipediaUrl = wikipediaUrl,
        hypoallergenic = hypoallergenic,
        referenceImageId = referenceImageId,
        image = Breed.Image(
            id = imageId,
            width = imageWidth,
            height = imageHeight,
            url = imageUrl,
        ),
    )
}
