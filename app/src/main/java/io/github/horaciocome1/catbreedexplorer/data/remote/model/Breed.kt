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

package io.github.horaciocome1.catbreedexplorer.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Breed(
    @SerialName("weight") val weight: Weight = Weight(),
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("cfa_url") val cfaUrl: String = "",
    @SerialName("vetstreet_url") val vetstreetUrl: String = "",
    @SerialName("vcahospitals_url") val vcahospitalsUrl: String = "",
    @SerialName("temperament") val temperament: String = "",
    @SerialName("origin") val origin: String = "",
    @SerialName("country_codes") val countryCodes: String = "",
    @SerialName("country_code") val countryCode: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("life_span") val lifeSpan: String = "",
    @SerialName("indoor") val indoor: Int = 0,
    @SerialName("lap") val lap: Int = 0,
    @SerialName("alt_names") val altNames: String = "",
    @SerialName("adaptability") val adaptability: Int = 0,
    @SerialName("affection_level") val affectionLevel: Int = 0,
    @SerialName("child_friendly") val childFriendly: Int = 0,
    @SerialName("dog_friendly") val dogFriendly: Int = 0,
    @SerialName("energy_level") val energyLevel: Int = 0,
    @SerialName("grooming") val grooming: Int = 0,
    @SerialName("health_issues") val healthIssues: Int = 0,
    @SerialName("intelligence") val intelligence: Int = 0,
    @SerialName("shedding_level") val sheddingLevel: Int = 0,
    @SerialName("social_needs") val socialNeeds: Int = 0,
    @SerialName("stranger_friendly") val strangerFriendly: Int = 0,
    @SerialName("vocalisation") val vocalisation: Int = 0,
    @SerialName("experimental") val experimental: Int = 0,
    @SerialName("hairless") val hairless: Int = 0,
    @SerialName("natural") val natural: Int = 0,
    @SerialName("rare") val rare: Int = 0,
    @SerialName("rex") val rex: Int = 0,
    @SerialName("suppressed_tail") val suppressedTail: Int = 0,
    @SerialName("short_legs") val shortLegs: Int = 0,
    @SerialName("wikipedia_url") val wikipediaUrl: String = "",
    @SerialName("hypoallergenic") val hypoallergenic: Int = 0,
    @SerialName("reference_image_id") val referenceImageId: String = "",
    @SerialName("image") val image: Image = Image(),
) {

    @Serializable
    data class Weight(
        @SerialName("imperial") val imperial: String = "",
        @SerialName("metric") val metric: String = "",
    )

    @Serializable
    data class Image(
        @SerialName("id") val id: String = "",
        @SerialName("width") val width: Int = 0,
        @SerialName("height") val height: Int = 0,
        @SerialName("url") val url: String = "",
    )
}
