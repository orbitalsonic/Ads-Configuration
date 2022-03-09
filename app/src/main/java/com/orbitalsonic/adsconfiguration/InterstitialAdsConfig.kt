package com.orbitalsonic.adsconfiguration

import android.app.Activity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orbitalsonic.adsconfiguration.GeneralUtils.isInternetConnected

class InterstitialAdsConfig(activity: Activity) {

    val AD_TAG = "AdsInformation"
    private var mInterstitialAd: InterstitialAd? = null
    private val mActivity = activity
    private var adRequest: AdRequest = AdRequest.Builder().build()

    private lateinit var interstitialOnLoadCallBack: InterstitialOnLoadCallBack
    private lateinit var interstitialOnShowCallBack: InterstitialOnShowCallBack


    fun loadInterstitialAd(
        admobInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        mListener: InterstitialOnLoadCallBack
    ) {
        interstitialOnLoadCallBack = mListener

        interstitialOnLoadCallBack.onInternetOrAppPurchased(
            isInternetConnected(mActivity),
            isAppPurchased
        )

        if (isInternetConnected(mActivity) || !isAppPurchased || isRemoteConfigActive) {
            if (mInterstitialAd == null) {
                interstitialOnLoadCallBack.onAdPreLoaded(false)
                InterstitialAd.load(
                    mActivity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            mInterstitialAd = null
                            interstitialOnLoadCallBack.onAdFailedToLoad(adError.toString())
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                            interstitialOnLoadCallBack.onAdLoaded()

                        }
                    })
            } else {
                interstitialOnLoadCallBack.onAdPreLoaded(true)
            }
        }
    }

    fun showInterstitialAd( mListener: InterstitialOnShowCallBack) {
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
            mInterstitialAd?.show(mActivity)
        }
    }

    fun showAndLoadInterstitialAd(admobInterstitialIds: String, mListener: InterstitialOnShowCallBack) {
        interstitialOnShowCallBack = mListener
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                    loadAgainInterstitialAd(admobInterstitialIds)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                }

                override fun onAdShowedFullScreenContent() {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdShowedFullScreenContent")
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdImpression")
                    interstitialOnShowCallBack.onAdImpression()
                }
            }
            mInterstitialAd?.show(mActivity)
        }
    }

    private fun loadAgainInterstitialAd(
        admobInterstitialIds: String
    ) {
        if (isInternetConnected(mActivity)) {
            if (mInterstitialAd == null) {
                InterstitialAd.load(
                    mActivity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdFailedToLoad: $adError")
                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            ALog.i(InterstitialAdsPreloadConfig.AD_TAG, "onAdLoaded")
                            mInterstitialAd = interstitialAd

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