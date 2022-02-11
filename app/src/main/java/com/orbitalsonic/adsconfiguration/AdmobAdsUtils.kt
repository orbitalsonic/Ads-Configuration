package com.orbitalsonic.adsconfiguration

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdmobAdsUtils(context: Context) {

    private  var adaptiveAdView: AdView
    private  var mediumAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private var mInterstitialAdSplash: InterstitialAd? = null
    private  var  adRequest: AdRequest
    private val mContext = context
    private var isLoaded = false

    init {
        MobileAds.initialize(mContext) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.i("MyApp", String.format(
                    "Adapter name: %s, Description: %s, Latency: %d",
                    adapterClass, status!!.description, status.latency))
            }

        }

        adaptiveAdView = AdView(mContext)
        mediumAdView = AdView(mContext)
        adRequest = AdRequest.Builder().build()

    }

    fun loadInterstitialAds(){

        InterstitialAd.load(mContext,mContext.getString(R.string.admob_interstitial_ids), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i("AdsInformation","onAdFailedToLoad")
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.i("AdsInformation","onAdLoaded")
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.i("AdsInformation","AdsReload")
//                loadInterstitialAgain()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.i("AdsInformation","AdsReload")
            }

            override fun onAdShowedFullScreenContent() {
                Log.i("AdsInformation","AdsReload")
                mInterstitialAd = null
            }
        }
    }


    private fun loadInterstitialAgain(){

        InterstitialAd.load(mContext,mContext.getString(R.string.admob_interstitial_ids), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }

        })
    }

    fun loadInterstitialAdsSplash(){

        InterstitialAd.load(mContext,mContext.getString(R.string.admob_interstitial_splash_ids), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAdSplash = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAdSplash = interstitialAd
            }
        })

        mInterstitialAdSplash?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAdSplash = null
            }
        }
    }

    fun isAdsLoaded():Boolean{
        isLoaded = mInterstitialAd != null
        return isLoaded
    }

    fun showInterstitialAds(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(mContext as Activity)
        }
    }

    fun showInterstitialAdsSplash(){
        if (mInterstitialAdSplash != null) {
            mInterstitialAdSplash?.show(mContext as Activity)
        }
    }

    fun loadBannerAds(adContainer:LinearLayout){
        adContainer.addView(adaptiveAdView)
        adaptiveAdView.adUnitId = mContext.getString(R.string.admob_simple_banner_ids)

        adaptiveAdView.adSize = getAdSize(adContainer)

        val adRequest = AdRequest
            .Builder()
            .build()
        adaptiveAdView.loadAd(adRequest)
    }

    fun loadBannerNativeAds(template: TemplateView){
        val adLoader: AdLoader = AdLoader.Builder(mContext, mContext.getString(R.string.admob_native_banner_ids))
            .forNativeAd { nativeAd ->
                template.setNativeAd(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    template.visibility = View.GONE
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    template.visibility = View.VISIBLE
                }
            })
            .build()

        adLoader.loadAd(AdManagerAdRequest.Builder().build())
    }

    fun loadMediumNativeAds(template: TemplateView){
        val adLoader: AdLoader = AdLoader.Builder(mContext, mContext.getString(R.string.admob_native_medium_ids))
            .forNativeAd { nativeAd ->
                template.setNativeAd(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    template.visibility = View.GONE
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    template.visibility = View.VISIBLE
                }
            })
            .build()

        adLoader.loadAd(AdManagerAdRequest.Builder().build())
    }


//    fun testMediation(){
//        MediationTestSuite.launch(mContext)
//    }


    private fun getAdSize(adContainer:LinearLayout):AdSize{
        val display =  (mContext as Activity).windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, adWidth)

    }

}