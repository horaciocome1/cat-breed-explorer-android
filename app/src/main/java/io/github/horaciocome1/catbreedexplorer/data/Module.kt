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

package io.github.horaciocome1.catbreedexplorer.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.horaciocome1.catbreedexplorer.data.db.AppDataStore
import io.github.horaciocome1.catbreedexplorer.data.db.AppDatabase
import io.github.horaciocome1.catbreedexplorer.data.remote.BreedsService
import io.github.horaciocome1.catbreedexplorer.data.remote.impl.BreedsServiceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideAppDataStore(@ApplicationContext context: Context) = AppDataStore(context)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "BreedDatabase",
        ).build()

    @Provides
    fun provideBreedDao(appDatabase: AppDatabase) = appDatabase.breedDao()

    @Provides
    fun provideFavoriteBreedDao(appDatabase: AppDatabase) = appDatabase.favoriteBreedDao()

    @Provides
    fun provideBreedsService(impl: BreedsServiceImpl): BreedsService = impl
}
