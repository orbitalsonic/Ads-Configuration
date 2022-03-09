package com.orbitalsonic.adsconfiguration

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        InterstitialAdsPreloadConfig.loadInterstitialAd(
            this,
            getString(R.string.admob_interstitial_ids),
            true,
            false,
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdFailedToLoad: $adError")
                }

                override fun onAdLoaded() {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdLoaded")
                }

                override fun onAdPreLoaded(isAdPreloaded: Boolean) {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "isAdPreloaded: $isAdPreloaded")
                }

                override fun onInternetOrAppPurchased(
                    isInternetConnected: Boolean,
                    isAppPurchased: Boolean
                ) {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "isInternetConnected: $isInternetConnected")
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "isAppPurchased: $isAppPurchased")
                }

            })

        findViewById<Button>(R.id.btn_show).setOnClickListener {
            if (InterstitialAdsPreloadConfig.isInterstitialLoaded()){
                InterstitialAdsPreloadConfig.showAndLoadInterstitialAd(this,getString(R.string.admob_interstitial_ids),object :InterstitialOnShowCallBack{
                    override fun onAdDismissedFullScreenContent() {
                        ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdDismissedFullScreenContent")
                    }

                    override fun onAdFailedToShowFullScreenContent() {
                        ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdFailedToShowFullScreenContent")
                    }

                    override fun onAdShowedFullScreenContent() {
                        ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdShowedFullScreenContent")
                    }

                    override fun onAdImpression() {
                        ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdImpression")
                    }

                })
            }
        }
    }
}