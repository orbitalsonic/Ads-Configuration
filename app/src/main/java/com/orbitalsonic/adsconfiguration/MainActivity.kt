package com.orbitalsonic.adsconfiguration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.orbitalsonic.adsconfiguration.InterstitialAdsPreloadConfig.AD_TAG

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InterstitialAdsPreloadConfig.loadInterstitialAd(
            this,
            getString(R.string.admob_interstitial_ids),
            true,
            false,
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    ALog.i(AD_TAG, "onAdFailedToLoad: $adError")
                }

                override fun onAdLoaded() {
                    ALog.i(AD_TAG, "onAdLoaded")
                }

                override fun onAdPreLoaded(isAdPreloaded: Boolean) {
                    ALog.i(AD_TAG, "isAdPreloaded: $isAdPreloaded")
                }

                override fun onInternetOrAppPurchased(
                    isInternetConnected: Boolean,
                    isAppPurchased: Boolean
                ) {
                    ALog.i(AD_TAG, "isInternetConnected: $isInternetConnected")
                    ALog.i(AD_TAG, "isAppPurchased: $isAppPurchased")
                }

            })

        findViewById<Button>(R.id.btn_next).setOnClickListener {
            if (InterstitialAdsPreloadConfig.isInterstitialLoaded()){
                InterstitialAdsPreloadConfig.showInterstitialAd(this,object :InterstitialOnShowCallBack{
                    override fun onAdDismissedFullScreenContent() {
                        ALog.i(AD_TAG, "onAdDismissedFullScreenContent")
                        startActivity(Intent(this@MainActivity, NextActivity::class.java))
                    }

                    override fun onAdFailedToShowFullScreenContent() {
                        ALog.i(AD_TAG, "onAdFailedToShowFullScreenContent")
                    }

                    override fun onAdShowedFullScreenContent() {
                        ALog.i(AD_TAG, "onAdShowedFullScreenContent")
                    }

                    override fun onAdImpression() {
                        ALog.i(AD_TAG, "onAdImpression")
                    }

                })
            }else{
                startActivity(Intent(this, NextActivity::class.java))
            }

        }
    }
}