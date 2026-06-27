package com.mobilerpgpack.phone.utils

interface IAssetExtractor{
    val assetsCopied : Boolean
    val assetsStartedCopyListeners : MulticastAction
    val assetsFinishCopyListeners : MulticastAction
    fun clearSubscribers ()
    suspend fun copyAssetsContentToInternalStorage (copyForced: Boolean = false)
}