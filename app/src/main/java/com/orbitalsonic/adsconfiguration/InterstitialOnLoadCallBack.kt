package com.orbitalsonic.adsconfiguration

interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onAdPreLoaded(isAdPreloaded:Boolean)
    fun onInternetOrAppPurchased(isInternetConnected:Boolean, isAppPurchased:Boolean)
}