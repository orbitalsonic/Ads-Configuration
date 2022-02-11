package com.orbitalsonic.adsconfiguration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.nativetemplates.TemplateView


class MainActivity : AppCompatActivity() {

    private lateinit var admobAdsUtils: AdmobAdsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adsContainer:LinearLayout = findViewById(R.id.ads_container)
        admobAdsUtils = AdmobAdsUtils(this)
        admobAdsUtils.loadBannerAds(adsContainer)
        admobAdsUtils.loadInterstitialAds()

        findViewById<Button>(R.id.btn_interstitial).setOnClickListener {
            admobAdsUtils.showInterstitialAds()
            startActivity(Intent(this,NextActivity::class.java))
        }


        admobAdsUtils.loadBannerNativeAds(findViewById<TemplateView>(R.id.my_template))


    }
}