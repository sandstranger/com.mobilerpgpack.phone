package com.mobilerpgpack.phone.utils

interface IAssetExtractor{
    val assetsCopied : Boolean

    val assetsStartedCopyListeners : MutableCollection<()-> Unit>

    suspend fun copyAssetsContentToInternalStorage ()
}