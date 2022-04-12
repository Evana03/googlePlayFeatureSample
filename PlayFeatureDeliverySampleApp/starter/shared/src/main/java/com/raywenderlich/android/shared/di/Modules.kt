/*
 * Copyright (c) 2021 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.shared.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.raywenderlich.android.shared.data.network.CatsAPI
import com.raywenderlich.android.shared.data.network.DogsAPI
import com.raywenderlich.android.shared.data.repository.*
import com.raywenderlich.android.shared.presentation.viewmodels.CatsDogViewModel
import com.raywenderlich.android.shared.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit

val okhttpModule: Module = module {
  single {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = when (BuildConfig.BUILD_TYPE) {
      "release" -> HttpLoggingInterceptor.Level.NONE
      else -> HttpLoggingInterceptor.Level.BODY
    }
    OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
  }
}

val dogsRetrofitModule: Module = module {
  single(named("dogs")) {
    Retrofit.Builder()
        .baseUrl(Constants.DOGS_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(get())
        .build()
  }
}

val catsRetrofitModule: Module = module {
  single(named("cats")) {
    Retrofit.Builder()
        .baseUrl(Constants.CATS_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(get())
        .build()
  }
}

val repositoryModule: Module = module {
  single<DogsRepository> { DogsRepositoryImpl(get()) }
  single<CatsRepository> { CatsRepositoryImpl(get()) }
}

val catsAPIModule: Module = module {
  single<CatsAPI> { get<Retrofit>(named("cats")).create() }
}

val dogsAPIModule: Module = module {
  single<DogsAPI> { get<Retrofit>(named("dogs")).create() }
}

val viewModelModule: Module = module {
  single { CatsDogViewModel(get(), get()) }
}

val sharedModules: List<Module> = listOf(
    okhttpModule,
    dogsRetrofitModule,
    catsRetrofitModule,
    catsAPIModule,
    dogsAPIModule,
    repositoryModule,
    viewModelModule
)