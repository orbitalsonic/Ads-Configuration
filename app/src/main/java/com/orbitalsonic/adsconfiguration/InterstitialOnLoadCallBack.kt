package com.orbitalsonic.adsconfiguration

interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad()
    fun onAdLoaded()
    fun onAdPreLoaded(isAdPreloaded:Boolean)
    fun onInternetOrAppPurchased(isInternetConnected:Boolean, isAppPurchased:Boolean)
}