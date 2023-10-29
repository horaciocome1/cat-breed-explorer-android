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

package io.github.horaciocome1.catbreedexplorer.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "weight_imperial") val weightImperial: String,
    @ColumnInfo(name = "weight_metric") val weightMetric: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cfa_url") val cfaUrl: String,
    @ColumnInfo(name = "vetstreet_url") val vetstreetUrl: String,
    @ColumnInfo(name = "vcahospitals_url") val vcahospitalsUrl: String,
    @ColumnInfo(name = "temperament") val temperament: String,
    @ColumnInfo(name = "origin") val origin: String,
    @ColumnInfo(name = "country_codes") val countryCodes: String,
    @ColumnInfo(name = "country_code") val countryCode: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "life_span") val lifeSpan: String,
    @ColumnInfo(name = "indoor") val indoor: Int,
    @ColumnInfo(name = "lap") val lap: Int,
    @ColumnInfo(name = "alt_names") val altNames: String,
    @ColumnInfo(name = "adaptability") val adaptability: Int,
    @ColumnInfo(name = "affection_level") val affectionLevel: Int,
    @ColumnInfo(name = "child_friendly") val childFriendly: Int,
    @ColumnInfo(name = "dog_friendly") val dogFriendly: Int,
    @ColumnInfo(name = "energy_level") val energyLevel: Int,
    @ColumnInfo(name = "grooming") val grooming: Int,
    @ColumnInfo(name = "health_issues") val healthIssues: Int,
    @ColumnInfo(name = "intelligence") val intelligence: Int,
    @ColumnInfo(name = "shedding_level") val sheddingLevel: Int,
    @ColumnInfo(name = "social_needs") val socialNeeds: Int,
    @ColumnInfo(name = "stranger_friendly") val strangerFriendly: Int,
    @ColumnInfo(name = "vocalisation") val vocalisation: Int,
    @ColumnInfo(name = "experimental") val experimental: Int,
    @ColumnInfo(name = "hairless") val hairless: Int,
    @ColumnInfo(name = "natural") val natural: Int,
    @ColumnInfo(name = "rare") val rare: Int,
    @ColumnInfo(name = "rex") val rex: Int,
    @ColumnInfo(name = "suppressed_tail") val suppressedTail: Int,
    @ColumnInfo(name = "short_legs") val shortLegs: Int,
    @ColumnInfo(name = "wikipedia_url") val wikipediaUrl: String,
    @ColumnInfo(name = "hypoallergenic") val hypoallergenic: Int,
    @ColumnInfo(name = "reference_image_id") val referenceImageId: String,
    @ColumnInfo(name = "image_id") val imageId: String,
    @ColumnInfo(name = "image_width") val imageWidth: Int,
    @ColumnInfo(name = "image_height") val imageHeight: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)
