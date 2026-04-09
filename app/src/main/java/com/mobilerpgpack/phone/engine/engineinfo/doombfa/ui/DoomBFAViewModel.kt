package com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui

import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAEngineInfo.Companion.HOME_DIRECTORY_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.io.File

class DoomBFAViewModel : ViewModel(), KoinComponent {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val glslCacheFolder : File by inject { parametersOf(
        HOME_DIRECTORY_NAME + "${File.separator}base${File.separator}renderprogs${File.separator}glsl") }

    private val hlslCacheFolder : File by inject { parametersOf(
         "$HOME_DIRECTORY_NAME${File.separator}base${File.separator}renderprogs${File.separator}hlsl") }

    @Volatile
    private var cacheIsDeleted = true

    fun deleteCacheFolders(){
        if (cacheIsDeleted){
            cacheIsDeleted = false
            scope.launch { deleteCacheFoldersAsync() }
        }
    }

    private suspend fun deleteCacheFoldersAsync (){
        if (glslCacheFolder.exists()){
            glslCacheFolder.deleteRecursively()
        }

        if (hlslCacheFolder.exists()){
            hlslCacheFolder.deleteRecursively()
        }
        cacheIsDeleted = true
    }
}