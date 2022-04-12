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

package com.raywenderlich.android.playfeaturedelivery

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.raywenderlich.android.playfeaturedelivery.databinding.ActivityMainBinding

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private val splitInstallManager: SplitInstallManager by lazy { SplitInstallManagerFactory.create(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupModulesDownload()

    binding.dogsCard.setOnClickListener {
      val intent = Intent()
      intent.setClassName(BuildConfig.APPLICATION_ID, "com.raywenderlich.android.dogs.DogsActivity")
      startActivity(intent)
    }

  }

  private fun setupModulesDownload() {
    val catsModuleInstallRequest = SplitInstallRequest.newBuilder()
        .addModule(CATS_MODULE)
        .build()

    splitInstallManager.registerListener {
      when (it.status()) {
        SplitInstallSessionStatus.DOWNLOADING -> {
          binding.progressIndicator.visibility = View.VISIBLE
          Toast.makeText(applicationContext, "Downloading", Toast.LENGTH_SHORT).show()
        }
        SplitInstallSessionStatus.INSTALLED -> {
          binding.progressIndicator.visibility = View.GONE
          Toast.makeText(applicationContext, "Module Download Completed", Toast.LENGTH_SHORT).show()
          val intent = Intent()
          intent.setClassName(BuildConfig.APPLICATION_ID, "com.raywenderlich.android.cats.CatsActivity")
          startActivity(intent)
        }
      }
    }

    binding.catsCard.setOnClickListener {
      splitInstallManager.startInstall(catsModuleInstallRequest)
    }

  }

  companion object {
    private const val DOGS_MODULE = "dog"
    private const val CATS_MODULE = "cats"
  }
}
