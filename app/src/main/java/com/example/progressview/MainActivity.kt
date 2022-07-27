package com.example.progressview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener {
            dotLoadingView.start()
            mouseEatingView.start()
        }
        stopBtn.setOnClickListener {
            dotLoadingView.stop()
            mouseEatingView.stop()
        }
    }
}