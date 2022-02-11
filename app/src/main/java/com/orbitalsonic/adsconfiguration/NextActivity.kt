package com.orbitalsonic.adsconfiguration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.ads.nativetemplates.TemplateView

class NextActivity : AppCompatActivity() {

    private lateinit var admobAdsUtils: AdmobAdsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_start)

        admobAdsUtils = AdmobAdsUtils(this)
        admobAdsUtils.loadMediumNativeAds(findViewById<TemplateView>(R.id.my_template))
    }
}