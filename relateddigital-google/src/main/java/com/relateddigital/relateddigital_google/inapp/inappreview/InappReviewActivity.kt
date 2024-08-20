package com.relateddigital.relateddigital_google.inapp.inappreview

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.relateddigital.relateddigital_google.R
/*
class InappReviewActivity : AppCompatActivity() {

    private lateinit var reviewManager: ReviewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inapp_review)

        // ReviewManager'ı başlat
        reviewManager = ReviewManagerFactory.create(this)

        // In-app review işlemini başlatan düğme
        val reviewButton: Button = findViewById(R.id.reviewButton)
        reviewButton.setOnClickListener {
            startInAppReview()
        }
    }

    private fun startInAppReview() {
        // In-app review işlemini başlatma
        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()
        request.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                // In-app review penceresini gösterme
                val reviewInfo: ReviewInfo = task.result
                val flow: Task<Void> = reviewManager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener(OnCompleteListener { reviewFlowTask ->
                    // İnceleme tamamlandığında buraya gelir
                })
            } else {
                // In-app review işlemi başlatılamadığında buraya gelir
            }
        })
    }
} */