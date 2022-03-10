package com.orbitalsonic.adsconfiguration.adsconfig

import android.app.Activity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orbitalsonic.adsconfiguration.interfaces.InterstitialOnLoadCallBack
import com.orbitalsonic.adsconfiguration.interfaces.InterstitialOnShowCallBack
import com.orbitalsonic.adsconfiguration.utils.ALog
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.isInternetConnected

object InterstitialAdsPreloadConfig {


    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var interstitialOnLoadCallBack: InterstitialOnLoadCallBack
    private lateinit var interstitialOnShowCallBack: InterstitialOnShowCallBack
    var isLoadingAd = false

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
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                interstitialOnLoadCallBack.onAdPreLoaded(false)
                val adRequest: AdRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    activity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            mInterstitialAd = null
                            isLoadingAd = false
                            interstitialOnLoadCallBack.onAdFailedToLoad(adError.toString())
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                            isLoadingAd = false
                            interstitialOnLoadCallBack.onAdLoaded()

                        }
                    })
            } else {
                interstitialOnLoadCallBack.onAdPreLoaded(true)
            }
        }
    }

    fun showInterstitialAd(activity: Activity, mListener: InterstitialOnShowCallBack) {
        interstitialOnShowCallBack = mListener
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    interstitialOnShowCallBack.onAdImpression()
                }
            }
            mInterstitialAd?.show(activity)
        }
    }

    fun showAndLoadInterstitialAd(activity: Activity,admobInterstitialIds: String, mListener: InterstitialOnShowCallBack) {
        interstitialOnShowCallBack = mListener
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    ALog.i(AD_TAG, "onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                    loadAgainInterstitialAd(activity,admobInterstitialIds)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    ALog.i(AD_TAG, "onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                }

                override fun onAdShowedFullScreenContent() {
                    ALog.i(AD_TAG, "onAdShowedFullScreenContent")
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    ALog.i(AD_TAG, "onAdImpression")
                    interstitialOnShowCallBack.onAdImpression()
                }
            }
            mInterstitialAd?.show(activity)
        }
    }

   private fun loadAgainInterstitialAd(
        activity: Activity,
        admobInterstitialIds: String
    ) {
        if (isInternetConnected(activity)) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                val adRequest: AdRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    activity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            ALog.i(AD_TAG, "Interstitial onAdFailedToLoad: $adError")
                            mInterstitialAd = null
                            isLoadingAd = false
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            ALog.i(AD_TAG, "Interstitial onAdLoaded")
                            mInterstitialAd = interstitialAd
                            isLoadingAd = false

                        }
                    })
            }

        }
    }

    fun isInterstitialLoaded(): Boolean {
        return mInterstitialAd != null
    }

    fun dismissInterstitialLoaded() {
        mInterstitialAd = null
    }

}