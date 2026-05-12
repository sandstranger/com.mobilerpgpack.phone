package com.mobilerpgpack.phone.engine.engineinfo.utils.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class ModsExporterViewModel : ViewModel(), KoinComponent {
    private val context : Context by inject ()

    fun importMods(modsModel: ModsModel, pathToFile : String){
        File(pathToFile).let { modsFile ->
            if (modsFile.exists()){
                modsModel.load((modsFile.readText()))
            }
        }
    }

    fun exportMods(uri: Uri, text: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(text.toByteArray(Charsets.UTF_8))
                stream.flush()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}