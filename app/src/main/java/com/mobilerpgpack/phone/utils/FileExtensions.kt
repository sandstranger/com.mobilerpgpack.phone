package com.mobilerpgpack.phone.utils

import java.io.File

fun File.writeTextSafely (textToWrite : String){
    this.parentFile?.mkdirs()
    if (exists()){
        delete()
    }
    writeText(textToWrite)
}