package com.orbitalsonic.adsconfiguration

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        findViewById<Button>(R.id.btn_show).setOnClickListener {

        }
    }
}