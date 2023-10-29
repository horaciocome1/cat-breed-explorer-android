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

package io.github.horaciocome1.catbreedexplorer.data.remote.impl

import io.github.horaciocome1.catbreedexplorer.data.remote.BreedsService
import io.github.horaciocome1.catbreedexplorer.data.remote.model.Breed
import io.github.horaciocome1.catbreedexplorer.data.remote.util.KtorService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import javax.inject.Inject

class BreedsServiceImpl @Inject constructor() : KtorService(), BreedsService {

    override suspend fun getBreeds(limit: Int, page: Int): List<Breed> {
        return httpClient.use { client ->
            client.get {
                url {
                    path("breeds")
                    parameter("limit", limit)
                    parameter("page", page)
                }
            }
        }.body()
    }

    override suspend fun getBreeds(name: String): List<Breed> {
        return httpClient.use { client ->
            client.get {
                url {
                    path("breeds/search")
                    parameter("q", name)
                }
            }
        }.body()
    }
}
