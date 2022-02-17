package com.orbitalsonic.adsconfiguration

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.nativetemplates.TemplateView

class MainActivity : AppCompatActivity() {

    private lateinit var admobAdsUtils: AdmobAdsUtils
    private lateinit var adsLoadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLoadingDialog()
        Handler(Looper.getMainLooper()).postDelayed({
            if (InterstitialAdsUtils.isInterstitialLoaded()){
                InterstitialAdsUtils.showSplashInterstitialAds(this,object :SplashInterstitialCallBack{
                    override fun onAdDismissedFullScreenContent() {
                        Log.i("AdsInformation","Main onAdDismissedFullScreenContent")
                        adsLoadingDialog.dismiss()
                    }

                    override fun onAdFailedToShowFullScreenContent() {
                        Log.i("AdsInformation","Main onAdDismissedFullScreenContent")
                        adsLoadingDialog.dismiss()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.i("AdsInformation","Main onAdDismissedFullScreenContent")
                        adsLoadingDialog.dismiss()
                    }

                })
            }else{
                adsLoadingDialog.dismiss()
            }
        }, 2000)

        val adsContainer:LinearLayout = findViewById(R.id.ads_container)
        admobAdsUtils = AdmobAdsUtils(this)
        admobAdsUtils.loadBannerAds(adsContainer)
        admobAdsUtils.loadInterstitialAds()

        findViewById<Button>(R.id.btn_interstitial).setOnClickListener {
            admobAdsUtils.showInterstitialAds()
            startActivity(Intent(this,NextActivity::class.java))
        }
        admobAdsUtils.loadBannerNativeAds(findViewById<TemplateView>(R.id.my_template))
    }

    private fun initLoadingDialog() {
        adsLoadingDialog = Dialog(this)
        adsLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        adsLoadingDialog.setContentView(R.layout.dialog_loading_ads)
        adsLoadingDialog.setCanceledOnTouchOutside(false)
        adsLoadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        adsLoadingDialog.show()
    }
}