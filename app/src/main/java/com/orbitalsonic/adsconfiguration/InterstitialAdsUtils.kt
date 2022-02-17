package com.orbitalsonic.adsconfiguration

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialAdsUtils {

    private var interstitialSplash: InterstitialAd? = null
    private lateinit var callbackListener:SplashInterstitialCallBack

    fun loadSplashInterstitialAds(activity: Activity){
        val adRequest: AdRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity,activity.getString(R.string.admob_interstitial_splash_ids), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i("AdsInformation","onAdFailedToLoad")
                interstitialSplash = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.i("AdsInformation","onAdLoaded")
                interstitialSplash = interstitialAd
            }
        })
    }

    fun isInterstitialLoaded():Boolean{
        return interstitialSplash != null
    }

    fun showSplashInterstitialAds(activity: Activity,mListener:SplashInterstitialCallBack){
        callbackListener = mListener
        if (interstitialSplash != null) {
            interstitialSplash?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i("AdsInformation","Splash onAdDismissedFullScreenContent")
                    callbackListener.onAdDismissedFullScreenContent()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.i("AdsInformation","Splash onAdFailedToShowFullScreenContent")
                    callbackListener.onAdFailedToShowFullScreenContent()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i("AdsInformation","Splash onAdShowedFullScreenContent")
                    callbackListener.onAdShowedFullScreenContent()
                    interstitialSplash = null
                }
            }
            interstitialSplash?.show(activity)
        }
    }

}