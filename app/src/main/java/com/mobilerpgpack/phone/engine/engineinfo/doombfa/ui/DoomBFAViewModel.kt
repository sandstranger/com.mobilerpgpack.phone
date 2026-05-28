package com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui

import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAEngineInfo.Companion.HOME_DIRECTORY_NAME
import com.mobilerpgpack.phone.main.KoinModulesProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class DoomBFAViewModel : ViewModel(), KoinComponent {
    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
    private val glslCacheFolder : File by inject { parametersOf(buildPathToCacheFolder("glsl")) }
    private val hlslCacheFolder : File by inject { parametersOf(buildPathToCacheFolder("hlsl")) }
    @Volatile
    private var cacheIsDeleted = true

    fun deleteCacheFolders(){
        if (cacheIsDeleted){
            cacheIsDeleted = false
            scope.launch { deleteCacheFoldersAsync() }
        }
    }

    private fun deleteCacheFoldersAsync (){
        if (glslCacheFolder.exists()){
            glslCacheFolder.deleteRecursively()
        }

        if (hlslCacheFolder.exists()){
            hlslCacheFolder.deleteRecursively()
        }
        cacheIsDeleted = true
    }

    private fun buildPathToCacheFolder (targetCacheFolder : String) =
        "$HOME_DIRECTORY_NAME${File.separator}base${File.separator}renderprogs${File.separator}${targetCacheFolder}"
}