package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.opentouchgaming.saffal.FileSAF
import java.io.File

class ModsFilesUpdater private constructor(private val modsModel: ModsModel){
    private fun updateFilesInModsFolder(){
        modsModel.apply {
            if (pathToModsFolder.liveData.value.isNullOrEmpty() || !enableModsAutoUpdateInFolder.liveData.value!!){
                return
            }

            var newFoundedModsCount = 0

            getModsFromModsFolder()?.let {
                it.forEach { file ->
                    if (!mods.any{mod -> mod.pathToMod.liveData.value == file.absolutePath}){
                        val mod = Mod()
                        mod.pathToMod.value = file.absolutePath
                        mods +=mod
                        ++newFoundedModsCount
                    }
                }

                if (newFoundedModsCount >0){
                    modsCount +=newFoundedModsCount
                    save()
                    updateComposeModsList()
                }
            }
        }
    }

    private fun findFilesInModsFolder(){
        modsModel.apply {
            if (pathToModsFolder.liveData.value.isNullOrEmpty()){
                return
            }

            getModsFromModsFolder()?.let {
                mods.clear()

                it.forEach { file ->
                    val mod = Mod()
                    mod.pathToMod.value = file.absolutePath
                    mods +=mod
                }
                modsCount = it.count()
                updateComposeModsList()
                save()
            }
        }
    }

    private fun removeNotExistingMods(){
        modsModel.apply {
            val modsToRemove = mutableSetOf<Mod>()

            mods.forEach {
                if (!it.pathToMod.liveData.value.isNullOrEmpty() && !FileSAF(it.pathToMod.liveData.value!!).exists()){
                    modsToRemove += it
                }
            }

            mods.removeAll(modsToRemove)
            modsCount -= modsToRemove.count()

            if (modsToRemove.count() >0){
                save()
                updateComposeModsList()
            }
        }
    }

    private fun getModsFromModsFolder () = if (modsModel.pathToModsFolder.liveData.value.isNullOrEmpty()) null else
        FileSAF(modsModel.pathToModsFolder.liveData.value!!).listFiles()?.filter { file -> file.isMod }?.toList()

    private val File.isMod get() = this.isFile && modsModel.allowedModsExtensions.contains(this.extension)

    companion object{
        @Suppress("UNCHECKED_CAST")
        fun <T> ModsModel.updateFiles(): T where T : ModsModel {
            return ModsFilesUpdater(this).let { filesUpdater ->
                pathToModsFolder.initialize("") {
                    filesUpdater.findFilesInModsFolder()
                    this.save()
                }
                filesUpdater.removeNotExistingMods()
                filesUpdater.updateFilesInModsFolder()
                this as T
            }
        }
    }
}
