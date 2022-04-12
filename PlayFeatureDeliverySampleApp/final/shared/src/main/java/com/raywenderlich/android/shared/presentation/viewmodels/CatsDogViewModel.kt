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

package com.raywenderlich.android.shared.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.shared.data.network.NetworkResult
import com.raywenderlich.android.shared.data.repository.CatsRepository
import com.raywenderlich.android.shared.data.repository.DogsRepository
import com.raywenderlich.android.shared.presentation.states.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatsDogViewModel(
    private val dogsRepository: DogsRepository,
    private val catsRepository: CatsRepository
) : ViewModel() {

  private val _cats: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
  val cats = _cats.asStateFlow()

  private val _dogs: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
  val dogs = _dogs.asStateFlow()

  fun getDogs() {
    viewModelScope.launch {
      when (val value = dogsRepository.getDogs()) {
        is NetworkResult.Success -> _dogs.value = UIState.ShowData(value.data)
        is NetworkResult.APIError -> _dogs.value = UIState.Error("An API occurred")
        is NetworkResult.ServerError -> _dogs.value = UIState.Error("An error occurred")
        is NetworkResult.NetworkError -> _dogs.value = UIState.Error("You don't have an active internet connection")
      }
    }
  }

  fun getCats() {
    viewModelScope.launch {
      when (val value = catsRepository.getCats()) {
        is NetworkResult.Success -> _cats.value = UIState.ShowData(value.data)
        is NetworkResult.APIError -> _cats.value = UIState.Error("An API occurred")
        is NetworkResult.ServerError -> _cats.value = UIState.Error("An error occurred")
        is NetworkResult.NetworkError -> _cats.value = UIState.Error("You don't have an active internet connection")
      }
    }
  }

}