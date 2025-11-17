package com.mobilerpgpack.phone.utils

interface IAssetExtractor{
    val assetsCopied : Boolean

    suspend fun copyAssetsContentToInternalStorage ()
}