package com.orbitalsonic.adsconfiguration.interfaces

interface InterstitialOnShowCallBack {
    fun onAdDismissedFullScreenContent()
    fun onAdFailedToShowFullScreenContent()
    fun onAdShowedFullScreenContent()
    fun onAdImpression()
}