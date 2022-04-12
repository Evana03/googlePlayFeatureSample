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

package com.raywenderlich.android.cats

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.splitcompat.SplitCompat
import com.raywenderlich.android.shared.R
import com.raywenderlich.android.shared.databinding.ActivityCatsDogsBinding
import com.raywenderlich.android.shared.presentation.adapters.DogsCatsAdapter
import com.raywenderlich.android.shared.presentation.states.UIModel
import com.raywenderlich.android.shared.presentation.states.UIState
import com.raywenderlich.android.shared.presentation.viewmodels.CatsDogViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatsActivity : AppCompatActivity() {
  private val catsDogViewModel: CatsDogViewModel by viewModel()
  private val catsDogsAdapter = DogsCatsAdapter()
  private lateinit var binding: ActivityCatsDogsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCatsDogsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    catsDogViewModel.getCats()
    binding.rv.adapter = catsDogsAdapter
    observeCats()

  }

  private fun observeCats() {
    lifecycleScope.launch {
      catsDogViewModel.cats.flowWithLifecycle(lifecycle).collect { value: UIState ->
        when (value) {
          is UIState.ShowData<*> -> {
            binding.animationView.cancelAnimation()
            binding.animationView.visibility = View.GONE
            populateData(value.data as List<UIModel>)
          }
          is UIState.Error -> {
            binding.animationView.cancelAnimation()
            binding.animationView.visibility = View.GONE
            Toast.makeText(applicationContext, value.message, Toast.LENGTH_SHORT).show()
          }
          UIState.Loading -> {
            binding.animationView.apply {
              setAnimation(R.raw.cat_animation)
              playAnimation()
              visibility = View.VISIBLE
            }
          }
        }
      }
    }
  }

  private fun populateData(data: List<UIModel>) {
    catsDogsAdapter.submitList(data)
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    SplitCompat.install(this)
  }
}