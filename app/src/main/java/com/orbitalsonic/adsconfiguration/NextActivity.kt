package com.orbitalsonic.adsconfiguration

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbitalsonic.adsconfiguration.adsconfig.AdmobBannerAds
import com.orbitalsonic.adsconfiguration.adsconfig.InterstitialAdsPreloadConfig
import com.orbitalsonic.adsconfiguration.interfaces.InterstitialOnLoadCallBack
import com.orbitalsonic.adsconfiguration.interfaces.InterstitialOnShowCallBack
import com.orbitalsonic.adsconfiguration.utils.ALog
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.AD_TAG

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        val nativeContainer: ConstraintLayout = findViewById(R.id.native_container)
        val adPlaceHolder: FrameLayout = findViewById(R.id.ad_place_holder)
        val loadingAdText: TextView = findViewById(R.id.loading_ad_text)

        AdmobBannerAds(this).showAdMobNative( getString(R.string.admob_native_banner_ids),
            true,
            false,nativeContainer,adPlaceHolder, loadingAdText, 2){ nativeAd ->
            nativeAd?.let {
                AdmobBannerAds(this).populateUnifiedNativeAdView(it,adPlaceHolder, 2 )
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
                    ALog.i(AD_TAG, "isInternetConnected: $isInternetConnected")
                    ALog.i(AD_TAG, "isAppPurchased: $isAppPurchased")
                }

            })

        findViewById<Button>(R.id.btn_show).setOnClickListener {
            if (InterstitialAdsPreloadConfig.isInterstitialLoaded()){
                InterstitialAdsPreloadConfig.showAndLoadInterstitialAd(this,getString(R.string.admob_interstitial_ids),object :
                    InterstitialOnShowCallBack {
                    override fun onAdDismissedFullScreenContent() {
                        ALog.i(AD_TAG, "Interstitial onAdDismissedFullScreenContent")
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
            }
        }
    }
}