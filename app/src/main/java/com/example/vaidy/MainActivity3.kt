package com.example.vaidy

import ImageAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vaidy.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding4: ActivityMain3Binding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageList: MutableList<String> // Assuming URLs of images

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding4 = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding4.root)

        val bundle: Bundle? = intent.extras
        binding4.textView6.text = bundle?.getString("UID")
        val Phn = bundle?.getString("Phn").toString()
        binding4.textView7.text = Phn
        binding4.textView8.text = bundle?.getString("Date")

        val arr0 = bundle?.getString("url1")
        val arr1 = bundle?.getString("url2")
        val arr2 = bundle?.getString("url3")
        val arr3 = bundle?.getString("url4")

        imageList = mutableListOf()

        if (arr0 != null) {
            imageList.add(arr0)
        }
        if (arr1 != null) {
            imageList.add(arr1)
        }
        if (arr2 != null) {
            imageList.add(arr2)
        }
        if (arr3 != null) {
            imageList.add(arr3)
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        imageAdapter = ImageAdapter(imageList)
        binding4.viewPager.adapter = imageAdapter
    }
}

