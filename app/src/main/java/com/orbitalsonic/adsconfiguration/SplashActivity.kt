package com.orbitalsonic.adsconfiguration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        InterstitialAdsUtils.loadSplashInterstitialAds(this)
        Handler(Looper.getMainLooper()).postDelayed({
            intentMethod()
        }, 7000)
    }

    private fun intentMethod() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()

    }
}