package com.mobilerpgpack.phone.translator

import kotlinx.coroutines.Deferred

abstract class TranslationModel {

    private var currentDownload: Deferred<Boolean>? = null


    abstract val translationType : TranslationType
    open var allowDownloadingOveMobile : Boolean = false


}