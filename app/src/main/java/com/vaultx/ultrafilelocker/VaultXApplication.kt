package com.vaultx.ultrafilelocker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VaultXApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any global configurations here
    }
}

