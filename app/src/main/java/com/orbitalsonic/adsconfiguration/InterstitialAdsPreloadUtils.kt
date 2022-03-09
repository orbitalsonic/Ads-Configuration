package com.orbitalsonic.adsconfiguration

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orbitalsonic.adsconfiguration.GeneralUtils.isInternetConnected

object InterstitialAdsPreloadUtils {

    const val AD_TAG = "AdsInformation"
    private var interstitialAd: InterstitialAd? = null
    private lateinit var interstitialOnLoadCallBack: InterstitialOnLoadCallBack
    private lateinit var interstitialOnShowCallBack: InterstitialOnShowCallBack

    fun loadInterstitialAd(
        activity: Activity,
        admobInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        mListener: InterstitialOnLoadCallBack
    ) {
        interstitialOnLoadCallBack = mListener

        interstitialOnLoadCallBack.onInternetOrAppPurchased(
            isInternetConnected(activity),
            isAppPurchased
        )

        if (isInternetConnected(activity) || !isAppPurchased || isRemoteConfigActive) {
            if (interstitialAd == null) {
                interstitialOnLoadCallBack.onAdPreLoaded(false)
                val adRequest: AdRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    activity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.i(AD_TAG, "onAdFailedToLoad: $adError")
                            interstitialAd = null
                            interstitialOnLoadCallBack.onAdFailedToLoad()
//                (activity as SplashActivity).launchActivity(300)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.i(AD_TAG, "onAdLoaded")
                            InterstitialAdsPreloadUtils.interstitialAd = interstitialAd
                            interstitialOnLoadCallBack.onAdLoaded()

                        }
                    })
            } else {
                interstitialOnLoadCallBack.onAdPreLoaded(true)
            }

        }
    }

    fun showSplashInterstitialAds(activity: Activity, mListener: InterstitialOnShowCallBack) {
        interstitialOnShowCallBack = mListener
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i(AD_TAG, "onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.i(AD_TAG, "onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i(AD_TAG, "onAdShowedFullScreenContent")
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    interstitialAd = null
                }

                override fun onAdImpression() {
                    interstitialOnShowCallBack.onAdImpression()
                }
            }
            interstitialAd?.show(activity)
        }
    }

    fun isInterstitialLoaded(): Boolean {
        return interstitialAd != null
    }

    fun dismissInterstitialLoaded() {
        interstitialAd = null
    }

}