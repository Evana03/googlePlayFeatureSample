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

package com.raywenderlich.android.shared.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.raywenderlich.android.shared.databinding.ItemCatsDogsBinding
import com.raywenderlich.android.shared.presentation.states.UIModel

class DogsCatsAdapter : ListAdapter<UIModel, DogsCatsAdapter.DogsCatViewHolder>(DogsCatDiffUtil) {

  class DogsCatViewHolder(private val itemCatsDogsBinding: ItemCatsDogsBinding) : RecyclerView.ViewHolder(itemCatsDogsBinding.root) {

    fun bind(uiModel: UIModel) {
      when {
        uiModel.url.contains("https") -> {
          itemCatsDogsBinding.image.load(uiModel.url)
        }
        else -> {
          Log.d("Cat", "https://cataas.com/cat/${uiModel.url}")
          itemCatsDogsBinding.image.load("https://cataas.com/cat/${uiModel.url}")
        }
      }
    }
  }

  object DogsCatDiffUtil : DiffUtil.ItemCallback<UIModel>() {
    override fun areItemsTheSame(oldItem: UIModel, newItem: UIModel): Boolean {
      return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: UIModel, newItem: UIModel): Boolean {
      return oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsCatViewHolder =
      DogsCatViewHolder(
          ItemCatsDogsBinding.inflate(
              LayoutInflater.from(parent.context),
              parent,
              false
          )
      )

  override fun onBindViewHolder(holder: DogsCatViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}