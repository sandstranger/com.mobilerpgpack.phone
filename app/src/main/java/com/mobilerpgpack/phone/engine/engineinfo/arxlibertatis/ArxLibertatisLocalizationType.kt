package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

enum class ArxLibertatisLocalizationType {
    english,
    francais,
    deutsch,
    italiano,
    russian,
    spanish,
    japanese,
    chinese;

    companion object{
        val stringEntries = entries.map { it.toString() }.toList()
    }
}