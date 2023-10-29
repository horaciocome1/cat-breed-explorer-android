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

package io.github.horaciocome1.catbreedexplorer.data.remote.util

import io.github.horaciocome1.catbreedexplorer.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val HEADER_X_API_KEY = "x-api-key"

abstract class KtorService {

    val httpClient: HttpClient
        get() = HttpClient(Android) {
            install(Logging) {
                logger = object : Logger {

                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HEADER_X_API_KEY }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildConfig.HOST
                    path("v1/")
                }
                header(HEADER_X_API_KEY, SecureKeyLibrary.getApiKey())
            }
        }
}
