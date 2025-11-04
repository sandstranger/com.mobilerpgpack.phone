package com.mobilerpgpack.phone.engine

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.activity.SDL2GameActivity
import com.mobilerpgpack.phone.engine.activity.SDL3GameActivity
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.utils.AssetExtractor
import com.mobilerpgpack.phone.utils.startActivity
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get


fun startEngine(context: Context,engineToPlay: EngineTypes) {

    val assetsExtractor: AssetExtractor = get(AssetExtractor::class.java)

    if (!assetsExtractor.assetsCopied) {
        return
    }

    val activeEngineInfo: IEngineInfo = get (IEngineInfo::class.java){
        parametersOf(engineToPlay.toString()) }

    if (activeEngineInfo.pathToResource.isEmpty()) {
        MaterialDialog(context).show {
            title(R.string.error)
            message(R.string.can_not_start_engine)
            positiveButton(R.string.ok_text)
        }
        return
    }

    when (engineToPlay) {
        EngineTypes.Doom64ExPlus -> context.startActivity<SDL3GameActivity>()
        else -> context.startActivity<SDL2GameActivity>()
    }
}
