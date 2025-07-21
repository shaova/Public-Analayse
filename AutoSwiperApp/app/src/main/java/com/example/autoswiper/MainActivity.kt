
package com.example.autoswiper

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.*
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isRunning = false
    private var handler: Handler? = null
    private var swipeRunnable: Runnable? = null
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var intervalInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        intervalInput = findViewById(R.id.intervalInput)

        findViewById<Button>(R.id.openAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        startButton.setOnClickListener {
            if (!isRunning) {
                val interval = intervalInput.text.toString().toLongOrNull() ?: 30
                startSwiping(interval)
                Toast.makeText(this, "开始刷视频，每隔 $interval 秒滑动一次", Toast.LENGTH_SHORT).show()
            }
        }

        stopButton.setOnClickListener {
            stopSwiping()
            Toast.makeText(this, "已停止滑动", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startSwiping(seconds: Long) {
        isRunning = true
        handler = Handler(Looper.getMainLooper())
        swipeRunnable = object : Runnable {
            override fun run() {
                AutoSwipeService.instance?.swipeLeft()
                handler?.postDelayed(this, seconds * 1000)
            }
        }
        handler?.post(swipeRunnable!!)
    }

    private fun stopSwiping() {
        isRunning = false
        handler?.removeCallbacks(swipeRunnable!!)
    }
}
