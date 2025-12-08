package com.mobilerpgpack.phone.engine.engineinfo.utils

import java.io.File

class ModsFilesUpdater <T> (private val modsModel: T) where T : ModsModel{

    fun updateFiles(): T =
        modsModel.apply {
            pathToModsFolder.initialize("") {
                findFilesInModsFolder()
                this.save()
            }
            removeNotExistingMods()
            updateFilesInModsFolder()
        }

    private fun updateFilesInModsFolder(){
        modsModel.apply {
            if (pathToModsFolder.value.isNullOrEmpty() || !enableModsAutoUpdateInFolder){
                return
            }

            var newFoundedModsCount = 0

            getModsFromModsFolder()?.let {
                it.forEach { file ->
                    if (!mods.any{mod -> mod.pathToMod.value == file.absolutePath}){
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
            if (pathToModsFolder.value.isNullOrEmpty()){
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
                if (!it.pathToMod.value.isNullOrEmpty() && !File(it.pathToMod.value!!).exists()){
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

    private fun getModsFromModsFolder () = if (modsModel.pathToModsFolder.value.isNullOrEmpty()) null else
        File(modsModel.pathToModsFolder.value!!).listFiles()?.filter { file -> file.isMod }?.toList()

    private val File.isMod get() = this.isFile && modsModel.allowedModsExtensions.contains(this.extension)
}