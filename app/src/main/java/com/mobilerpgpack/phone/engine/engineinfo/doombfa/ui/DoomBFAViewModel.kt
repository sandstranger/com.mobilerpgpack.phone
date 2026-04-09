package com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui

import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAEngineInfo.Companion.HOME_DIRECTORY_NAME
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.io.File

class DoomBFAViewModel : ViewModel(), KoinComponent {
    private val glslCacheFolder : File by inject { parametersOf(
        HOME_DIRECTORY_NAME + "${File.separator}base${File.separator}renderprogs${File.separator}glsl") }

    private val hlslCacheFolder : File by inject { parametersOf(
         "$HOME_DIRECTORY_NAME${File.separator}base${File.separator}renderprogs${File.separator}hlsl") }

    fun deleteCacheFolders(){
        if (glslCacheFolder.exists()){
            glslCacheFolder.deleteRecursively()
        }

        if (hlslCacheFolder.exists()){
            hlslCacheFolder.deleteRecursively()
        }
    }
}