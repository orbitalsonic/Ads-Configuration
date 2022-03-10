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

class InterstitialAdsConfig(activity: Activity) {

    private var mInterstitialAd: InterstitialAd? = null
    private val mActivity = activity
    private var adRequest: AdRequest = AdRequest.Builder().build()

    private lateinit var interstitialOnLoadCallBack: InterstitialOnLoadCallBack
    private lateinit var interstitialOnShowCallBack: InterstitialOnShowCallBack
    var isLoadingAd = false


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
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                interstitialOnLoadCallBack.onAdPreLoaded(false)
                InterstitialAd.load(
                    mActivity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            isLoadingAd = false
                            mInterstitialAd = null
                            interstitialOnLoadCallBack.onAdFailedToLoad(adError.toString())
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            isLoadingAd = false
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
                    ALog.i(AD_TAG, "Interstitial onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                    loadAgainInterstitialAd(admobInterstitialIds)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    ALog.i(AD_TAG, "Interstitial onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                }

                override fun onAdShowedFullScreenContent() {
                    ALog.i(AD_TAG, "Interstitial onAdShowedFullScreenContent")
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    ALog.i(AD_TAG, "Interstitial onAdImpression")
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
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                InterstitialAd.load(
                    mActivity,
                    admobInterstitialIds,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            ALog.i(AD_TAG, "Interstitial onAdFailedToLoad: $adError")
                            isLoadingAd = false
                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            ALog.i(AD_TAG, "Interstitial onAdLoaded")
                            isLoadingAd = false
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