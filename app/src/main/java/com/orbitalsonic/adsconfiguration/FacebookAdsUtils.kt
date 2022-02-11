package com.orbitalsonic.adsconfiguration


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.ads.*
import com.facebook.ads.AdSize


class FacebookAdsUtils(context: Context) {

    private val adViewBanner: AdView
    private val adViewMedium: AdView
    private val interstitialAd: InterstitialAd
    private val interstitialAdSplash: InterstitialAd
    private val mContext = context
    private val tag:String = "FacebookAds"
    private lateinit var adViewNativeM: ConstraintLayout
    private lateinit var adViewNativeB: LinearLayout
    private val nativeAdM: NativeAd
    private val nativeBannerAd: NativeBannerAd

    init {
        AudienceNetworkAds.initialize(mContext)
        interstitialAd = InterstitialAd(
            mContext,
            mContext.getString(R.string.facebook_interstitial_ids)
        )
        interstitialAdSplash = InterstitialAd(
            mContext,
            mContext.getString(R.string.facebook_interstitial_splash_ids)
        )

        nativeAdM = NativeAd(mContext, mContext.getString(R.string.facebook_native_medium_ids))
        nativeBannerAd = NativeBannerAd(mContext, mContext.getString(R.string.facebook_native_banner_ids))

        adViewBanner = AdView(mContext, mContext.getString(R.string.facebook_simple_banner_ids), AdSize.BANNER_HEIGHT_90)

        adViewMedium = AdView(mContext, mContext.getString(R.string.facebook_simple_medium_ids), AdSize.RECTANGLE_HEIGHT_250)

    }

    private var interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
        override fun onInterstitialDisplayed(ad: Ad) {
            // Interstitial ad displayed callback
            Log.e(tag, "Interstitial ad displayed.")
        }

        override fun onInterstitialDismissed(ad: Ad) {
            // Interstitial dismissed callback
            Log.e(tag, "Interstitial ad dismissed.")
            interstitialAd.loadAd()
        }

        override fun onError(ad: Ad, adError: AdError) {
            // Ad error callback
            Log.e(tag, "Interstitial ad failed to load: " + adError.errorMessage)
        }

        override fun onAdLoaded(ad: Ad) {
            // Interstitial ad is loaded and ready to be displayed
            Log.d(tag, "Interstitial ad is loaded and ready to be displayed!")
        }

        override fun onAdClicked(ad: Ad) {
            // Ad clicked callback
            Log.d(tag, "Interstitial ad clicked!")
        }

        override fun onLoggingImpression(ad: Ad) {
            // Ad impression logged callback
            Log.d(tag, "Interstitial ad impression logged!")
        }
    }

    var interstitialAdListenerSplash: InterstitialAdListener = object : InterstitialAdListener {
        override fun onInterstitialDisplayed(ad: Ad) {
            // Interstitial ad displayed callback
            Log.e(tag, "Interstitial ad displayed.")
        }

        override fun onInterstitialDismissed(ad: Ad) {
            // Interstitial dismissed callback
            Log.e(tag, "Interstitial ad dismissed.")
        }

        override fun onError(ad: Ad, adError: AdError) {
            // Ad error callback
            Log.e(tag, "Interstitial ad failed to load: " + adError.errorMessage)
        }

        override fun onAdLoaded(ad: Ad) {
            // Interstitial ad is loaded and ready to be displayed
            Log.d(tag, "Interstitial ad is loaded and ready to be displayed!")
        }

        override fun onAdClicked(ad: Ad) {
            // Ad clicked callback
            Log.d(tag, "Interstitial ad clicked!")
        }

        override fun onLoggingImpression(ad: Ad) {
            // Ad impression logged callback
            Log.d(tag, "Interstitial ad impression logged!")
        }
    }

    fun loadInterstitialAds(){
        interstitialAd.loadAd(
            interstitialAd.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        );
    }

    fun loadInterstitialAdsSplash(){
        interstitialAdSplash.loadAd(
            interstitialAdSplash.buildLoadAdConfig()
                .withAdListener(interstitialAdListenerSplash)
                .build()
        );
    }

    fun showInterstitialAds(){
        if (interstitialAd.isAdLoaded){
            interstitialAd.show()
        }
    }

    fun showInterstitialAdsSplash(){
        if (interstitialAdSplash.isAdLoaded){
            interstitialAdSplash.show()
        }
    }

    fun loadBannerAds(adContainer:LinearLayout){
        adContainer.addView(adViewBanner)

        adViewBanner.loadAd()
    }
    fun loadMediumAds(adContainer:LinearLayout){
        adContainer.addView(adViewMedium)

        adViewMedium.loadAd()
    }

    fun isInterstitialAdsLoaded():Boolean{
        return interstitialAd.isAdLoaded
    }

    fun destroyBannerAds(){
        adViewBanner.destroy()
    }
    fun destroyMediumAds(){
        adViewMedium.destroy()
    }
    fun destroyInterstitialAds(){
        interstitialAd.destroy()
    }
    fun destroyInterstitialAdsSplash(){
        interstitialAdSplash.destroy()
    }

    fun loadNativeMediumAd(nativeAdLayout: NativeAdLayout) {

        val nativeAdListener: NativeAdListener = object : NativeAdListener {
            override fun onMediaDownloaded(ad: Ad) {
                // Native ad finished downloading all assets
                Log.e(tag, "Native ad finished downloading all assets.")
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Native ad failed to load
                Log.e(tag, "Native ad failed to load: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(tag, "Native ad is loaded and ready to be displayed!")
                // Race condition, load() called again before last ad was displayed
                if (nativeAdM != ad) {
                    return
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAdM,nativeAdLayout)
            }

            override fun onAdClicked(ad: Ad) {
                // Native ad clicked
                Log.d(tag, "Native ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Native ad impression
                Log.d(tag, "Native ad impression logged!")
            }
        }

        // Request an ad
        nativeAdM.loadAd(
            nativeAdM.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build()
        )
    }

    fun loadNativeBannerAd(nativeAdLayout: NativeAdLayout) {

        val nativeAdListener: NativeAdListener = object : NativeAdListener {
            override fun onMediaDownloaded(ad: Ad) {
                // Native ad finished downloading all assets
                Log.e(tag, "Native ad finished downloading all assets.")
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Native ad failed to load
                Log.e(tag, "Native ad failed to load: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(tag, "Native ad is loaded and ready to be displayed!")
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd != ad) {
                    return
                }
                // Inflate Native Ad into Container
                inflateAdBanner(nativeBannerAd,nativeAdLayout)
            }

            override fun onAdClicked(ad: Ad) {
                // Native ad clicked
                Log.d(tag, "Native ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Native ad impression
                Log.d(tag, "Native ad impression logged!")
            }
        }

        // Request an ad
        nativeBannerAd.loadAd(
            nativeBannerAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build()
        )
    }

    private fun inflateAd(nativeAd: NativeAd,nativeAdLayout: NativeAdLayout) {
        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mContext)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adViewNativeM = inflater.inflate(R.layout.facebook_native_medium_ads, nativeAdLayout, false) as ConstraintLayout
        nativeAdLayout.addView(adViewNativeM)

        // Add the AdOptionsView
        val adChoicesContainer: LinearLayout = adViewNativeM.findViewById(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(mContext, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdIcon: MediaView = adViewNativeM.findViewById(R.id.native_ad_icon)
        val nativeAdTitle = adViewNativeM.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdMedia: MediaView = adViewNativeM.findViewById(R.id.native_ad_media)
        val nativeAdSocialContext = adViewNativeM.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdBody = adViewNativeM.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adViewNativeM.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction: Button = adViewNativeM.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.visibility = if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adViewNativeM, nativeAdMedia, nativeAdIcon, clickableViews
        )
    }

    private fun inflateAdBanner(nativeBannerAd: NativeBannerAd,nativeAdLayout: NativeAdLayout) {
        // Unregister last ad
        nativeBannerAd.unregisterView()

        // Add the Ad view into the ad container.
        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mContext)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adViewNativeB = inflater.inflate(R.layout.facebook_native_banner_ads, nativeAdLayout, false) as LinearLayout
        nativeAdLayout.addView(adViewNativeB)

        // Add the AdChoices icon
        val adChoicesContainer: RelativeLayout = adViewNativeB.findViewById(R.id.ad_choices_container)
        val adOptionsView =
            AdOptionsView(mContext, nativeBannerAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdTitle: TextView = adViewNativeB.findViewById(R.id.native_ad_title)
        val nativeAdSocialContext: TextView = adViewNativeB.findViewById(R.id.native_ad_social_context)
        val sponsoredLabel: TextView = adViewNativeB.findViewById(R.id.native_ad_sponsored_label)
        val nativeAdIconView: MediaView = adViewNativeB.findViewById(R.id.native_icon_view)
        val nativeAdCallToAction: Button = adViewNativeB.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdCallToAction.text = nativeBannerAd.adCallToAction
        nativeAdCallToAction.visibility =
            if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdTitle.text = nativeBannerAd.advertiserName
        nativeAdSocialContext.text = nativeBannerAd.adSocialContext
        sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

        // Register the Title and CTA button to listen for clicks.
        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeBannerAd.registerViewForInteraction(
            adViewBanner, nativeAdIconView, clickableViews
        )
    }

}