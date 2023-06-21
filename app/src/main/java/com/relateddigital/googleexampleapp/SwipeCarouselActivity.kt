package com.relateddigital.googleexampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class SwipeCarouselActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_carousel)
        Log.d("MainActivity", "SwipeCarouselView created")

    }
}