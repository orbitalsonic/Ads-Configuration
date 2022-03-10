package com.orbitalsonic.adsconfiguration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbitalsonic.adsconfiguration.adsconfig.AdmobAdaptiveAds
import com.orbitalsonic.adsconfiguration.adsconfig.AdmobNativeAds
import com.orbitalsonic.adsconfiguration.adsconfig.InterstitialAdsPreloadConfig
import com.orbitalsonic.adsconfiguration.interfaces.InterstitialOnLoadCallBack
import com.orbitalsonic.adsconfiguration.interfaces.InterstitialOnShowCallBack
import com.orbitalsonic.adsconfiguration.utils.ALog
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.AD_TAG

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nativeContainer:ConstraintLayout = findViewById(R.id.native_container)
        val adPlaceHolder:FrameLayout = findViewById(R.id.ad_place_holder)
        val loadingAdText:TextView = findViewById(R.id.loading_ad_text)

        AdmobNativeAds(this).showAdMobNative( getString(R.string.admob_native_ids),
            true,
            false,nativeContainer,adPlaceHolder, loadingAdText, 1){ nativeAd ->
            nativeAd?.let {
                AdmobNativeAds(this).populateUnifiedNativeAdView(it,adPlaceHolder, 1 )
            }
        }

        InterstitialAdsPreloadConfig.loadInterstitialAd(
            this,
            getString(R.string.admob_interstitial_ids),
            true,
            false,
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    ALog.i(AD_TAG, "Interstitial onAdFailedToLoad: $adError")
                }

                override fun onAdLoaded() {
                    ALog.i(AD_TAG, "Interstitial onAdLoaded")
                }

                override fun onAdPreLoaded(isAdPreloaded: Boolean) {
                    ALog.i(AD_TAG, "Interstitial isAdPreloaded: $isAdPreloaded")
                }

                override fun onInternetOrAppPurchased(
                    isInternetConnected: Boolean,
                    isAppPurchased: Boolean
                ) {
                    ALog.i(AD_TAG, "Interstitial isInternetConnected: $isInternetConnected")
                    ALog.i(AD_TAG, "Interstitial isAppPurchased: $isAppPurchased")
                }

            })

        findViewById<Button>(R.id.btn_next).setOnClickListener {
            if (InterstitialAdsPreloadConfig.isInterstitialLoaded()){
                InterstitialAdsPreloadConfig.showInterstitialAd(this,object :
                    InterstitialOnShowCallBack {
                    override fun onAdDismissedFullScreenContent() {
                        ALog.i(AD_TAG, "Interstitial onAdDismissedFullScreenContent")
                        startActivity(Intent(this@MainActivity, NextActivity::class.java))
                    }

                    override fun onAdFailedToShowFullScreenContent() {
                        ALog.i(AD_TAG, "Interstitial onAdFailedToShowFullScreenContent")
                    }

                    override fun onAdShowedFullScreenContent() {
                        ALog.i(AD_TAG, "Interstitial onAdShowedFullScreenContent")
                    }

                    override fun onAdImpression() {
                        ALog.i(AD_TAG, "Interstitial onAdImpression")
                    }

                })
            }else{
                startActivity(Intent(this, NextActivity::class.java))
            }

        }
    }
}