package com.orbitalsonic.adsconfiguration

interface InterstitialOnShowCallBack {
    fun onAdDismissedFullScreenContent()
    fun onAdFailedToShowFullScreenContent()
    fun onAdShowedFullScreenContent()
    fun onAdImpression()
}