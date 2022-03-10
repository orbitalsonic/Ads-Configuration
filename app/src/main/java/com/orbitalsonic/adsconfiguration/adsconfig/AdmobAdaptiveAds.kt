package com.orbitalsonic.adsconfiguration.adsconfig

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.*
import com.orbitalsonic.adsconfiguration.utils.ALog
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.isInternetConnected

class AdmobAdaptiveAds(private val activity: Activity) {

    companion object {
        var adaptiveAdView: AdView? = null
        private var isAdaptiveLoading = false
        private var isAdaptiveFailed = false
    }

    fun showAdaptiveBanner(
        admobAdaptiveBannerIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        bannerContainerLayout: ConstraintLayout,
        adaptivePlaceHolder: FrameLayout,
        loadingText: TextView,
        adLoadedCallback: (adview:AdView?) -> Unit
    ){
        ALog.i(AD_TAG, "showAdaptive is called")

        if (isInternetConnected(activity) || !isAppPurchased || isRemoteConfigActive) {
            if (adaptiveAdView != null) {
                ALog.i(AD_TAG, "showAdaptive is not null")
                loadingText.visibility = View.GONE

                bannerContainerLayout.visibility = View.VISIBLE
                adaptivePlaceHolder.visibility = View.VISIBLE
                adaptivePlaceHolder.addView(adaptiveAdView)

            } else {
                if (!isAdaptiveLoading) {
                    ALog.i(AD_TAG, "isAdaptiveLoading: $isAdaptiveLoading")
                    loadAdaptiveBanner(admobAdaptiveBannerIds,bannerContainerLayout,adaptivePlaceHolder,loadingText,adLoadedCallback)

                } else {
                    if (isAdaptiveFailed) {
                        ALog.i(AD_TAG, "isAdaptiveFailed: $isAdaptiveFailed")
                        isAdaptiveFailed = false
                        isAdaptiveLoading = false
                        bannerContainerLayout.visibility = View.GONE
                    }
                }
            }
        } else {
            bannerContainerLayout.visibility = View.GONE
        }
    }

    private fun loadAdaptiveBanner(
        admobAdaptiveBannerIds: String,
        bannerContainerLayout: ConstraintLayout,
        adaptivePlaceHolder: FrameLayout,
        loadingText: TextView,
        adLoadedCallback: (adview:AdView?) -> Unit
    ){
        isAdaptiveLoading = true
        isAdaptiveFailed = false
        bannerContainerLayout.visibility = View.VISIBLE
        adaptiveAdView = null
        adaptiveAdView = AdView(activity)
        adaptiveAdView?.adUnitId = admobAdaptiveBannerIds
        adaptiveAdView?.adSize = getAdSize(adaptivePlaceHolder)

        val adRequest = AdRequest.Builder().build()
        adaptiveAdView?.loadAd(adRequest)
        adaptiveAdView?.adListener = object : AdListener() {

            override fun onAdImpression() {
                super.onAdImpression()
                ALog.i(AD_TAG, "Adaptive onAdImpression")
                adaptiveAdView = null
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                ALog.i(AD_TAG, "Adaptive onAdLoaded")
                loadingText.visibility = View.GONE
                if (!isAdaptiveLoading){
                    isAdaptiveLoading = false
                }
                adLoadedCallback(adaptiveAdView)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                ALog.i(AD_TAG, "Adaptive onAdFailedToLoad:" + adError.message)
                isAdaptiveFailed = true
                isAdaptiveLoading = false
                bannerContainerLayout.visibility = View.GONE
            }

            override fun onAdOpened() {}

            override fun onAdClicked() {}

            override fun onAdClosed() {}
        }
    }


    private fun getAdSize(adContainer: FrameLayout): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)

    }

    fun dismissAdaptiveAd() {
        adaptiveAdView = null
        isAdaptiveFailed = false
        isAdaptiveLoading = false
    }

}