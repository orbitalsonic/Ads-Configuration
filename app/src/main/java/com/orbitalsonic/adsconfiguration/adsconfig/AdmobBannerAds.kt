package com.orbitalsonic.adsconfiguration.adsconfig

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.orbitalsonic.adsconfiguration.R
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.orbitalsonic.adsconfiguration.utils.ALog
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.adsconfiguration.utils.GeneralUtils.isInternetConnected

class AdmobBannerAds(private val activity: Activity) {

    companion object {
        var adMobNativeAd: NativeAd? = null
        private var isNativeLoading = false
        private var isNativeFailed = false
    }

    private var adLoader: AdLoader? = null

    fun showAdMobNative(
        admobInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        nativeContainer: ConstraintLayout,
        adMobContainer: FrameLayout,
        loadingText: TextView,
        nativeNo: Int,
        adLoadedCallback: (nativeAd: NativeAd?) -> Unit
    ) {
        ALog.i(AD_TAG, "showAdMobNative is called")

        if (isInternetConnected(activity) || !isAppPurchased || isRemoteConfigActive) {
            if (adMobNativeAd != null) {
                if (activity.window.decorView.rootView.isShown) {
                    loadingText.visibility = View.GONE

                    nativeContainer.visibility = View.VISIBLE
                    adMobContainer.visibility = View.VISIBLE
                    populateUnifiedNativeAdView(adMobNativeAd, adMobContainer, nativeNo)

                }
            } else {
                if (!isNativeLoading) {
                    loadAd(admobInterstitialIds, nativeContainer, loadingText, adLoadedCallback)
                } else {
                    if (isNativeFailed) {
                        isNativeFailed = false
                        isNativeLoading = false
                        nativeContainer.visibility = View.GONE
                    }
                }
            }
        } else {
            nativeContainer.visibility = View.GONE
        }
    }

    private fun loadAd(
        admobInterstitialIds: String,
        nativeContainer: ConstraintLayout,
        loadingText: TextView,
        adLoadedCallback: (nativeAd: NativeAd?) -> Unit
    ) {
        ALog.i(AD_TAG, "loadAd is called")
        isNativeLoading = true
        isNativeFailed = false
        nativeContainer.visibility = View.VISIBLE
        val builder: AdLoader.Builder = AdLoader.Builder(activity, admobInterstitialIds)
        adMobNativeAd = null
        adLoader =
            builder.forNativeAd { unifiedNativeAd: NativeAd? -> adMobNativeAd = unifiedNativeAd }
                .withAdListener(object : AdListener() {
                    override fun onAdImpression() {
                        super.onAdImpression()
                        adMobNativeAd = null

                        ALog.i(AD_TAG, "native onAdImpression")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        ALog.i(AD_TAG, "native onAdFailedToLoad: " + loadAdError.message)
                        isNativeLoading = false
                        isNativeFailed = true
                        nativeContainer.visibility = View.GONE
                        super.onAdFailedToLoad(loadAdError)
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        loadingText.visibility = View.GONE
                        adLoader?.let { loader ->
                            if (!loader.isLoading) {
                                isNativeLoading = false
                            }
                        }

                        ALog.i(AD_TAG, "native onAdLoaded")
                        adLoadedCallback(adMobNativeAd)

                    }

                }).withNativeAdOptions(
                    com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                        .setAdChoicesPlacement(
                            NativeAdOptions.ADCHOICES_TOP_RIGHT
                        ).build()
                )
                .build()
        adLoader?.loadAd(AdRequest.Builder().build())

    }

    fun populateUnifiedNativeAdView(
        nativeAd: NativeAd?,
        adMobNativeContainer: FrameLayout,
        nativeNo: Int
    ) {
        ALog.i(AD_TAG, "populateUnifiedNativeAdView is called")

        nativeAd?.let { ad ->
            val inflater = LayoutInflater.from(activity)
            val adView: NativeAdView = if (nativeNo == 1) {
                inflater.inflate(R.layout.admob_native_small, null) as NativeAdView
            } else {
                inflater.inflate(R.layout.admob_native_small, null) as NativeAdView
            }
            adMobNativeContainer.visibility = View.VISIBLE
            adMobNativeContainer.removeAllViews()
            adMobNativeContainer.addView(adView)
/*
            val mediaView: MediaView = adView.findViewById(R.id.ad_media)
            adView.mediaView = mediaView*/

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)

            //Headline
            adView.headlineView?.let { headline ->
                (headline as TextView).text = ad.headline
                headline.isSelected = true
            }

            //Body
            adView.bodyView?.let { bodyView ->
                if (ad.body == null) {
                    bodyView.visibility = View.INVISIBLE
                } else {
                    bodyView.visibility = View.VISIBLE
                    (bodyView as TextView).text = ad.body
                }

            }

            //Call to Action
            adView.callToActionView?.let { ctaView ->
                if (ad.callToAction == null) {
                    ctaView.visibility = View.INVISIBLE
                } else {
                    ctaView.visibility = View.VISIBLE
                    (ctaView as Button).text = ad.callToAction
                }

            }

            //Icon
            adView.iconView?.let { iconView ->
                if (ad.icon == null) {
                    iconView.visibility = View.GONE
                } else {
                    (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                    iconView.visibility = View.VISIBLE
                }

            }

            adView.advertiserView?.let { adverView ->

                if (ad.advertiser == null) {
                    adverView.visibility = View.GONE
                } else {
                    (adverView as TextView).text = ad.advertiser
                    adverView.visibility = View.GONE
                }
            }

            adView.setNativeAd(ad)
        }
    }


    private fun getAdSize(adContainer: LinearLayout): AdSize {
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

}