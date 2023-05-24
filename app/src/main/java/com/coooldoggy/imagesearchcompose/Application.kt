package com.coooldoggy.imagesearchcompose

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import org.conscrypt.Conscrypt
import java.security.Security

@HiltAndroidApp
class Application : Application() {
    companion object {
        private var globalApplication: Application? = null

        fun getContext(): Context? {
            return globalApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
    }

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            globalApplication = this@Application
        }
    }
}
