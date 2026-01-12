package com.mobilerpgpack.phone.utils

import android.app.Activity
import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Native

class ScreenOrientationChanger (private val activity : Activity, nativeLibNameToLoad : String) {
    private fun interface ForceLandscapeActivityOrientationCallback : Callback {
        fun forceLandscape()
    }

    private interface ForceActivityLandscapeOrientationNativeBridge : Library {
        fun registerForceLandscapeActivityOrientationCallback(cb: ForceLandscapeActivityOrientationCallback)
    }

    private val forceActivityOrientationCallback: ForceLandscapeActivityOrientationCallback

    init {
        forceActivityOrientationCallback = { activity.forceLandscapeOrientation() }
        Native.load(nativeLibNameToLoad,
            ForceActivityLandscapeOrientationNativeBridge::class.java).apply {
            registerForceLandscapeActivityOrientationCallback (forceActivityOrientationCallback)
        }
    }
}
