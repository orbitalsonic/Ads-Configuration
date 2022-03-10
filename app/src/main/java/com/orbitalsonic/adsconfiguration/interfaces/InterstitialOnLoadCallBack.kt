package com.orbitalsonic.adsconfiguration.interfaces

interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onAdPreLoaded(isAdPreloaded:Boolean)
    fun onInternetOrAppPurchased(isInternetConnected:Boolean, isAppPurchased:Boolean)
}