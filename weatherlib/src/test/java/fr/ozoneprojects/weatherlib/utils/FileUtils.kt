package fr.ozoneprojects.weatherlib.utils

import java.io.File

const val ASSET_BASE_PATH = "../weatherlib/src/test/assets"

fun readFileAsTextUsingInputStream(fileName: String): String =
    File("$ASSET_BASE_PATH/$fileName").inputStream().readBytes().toString(Charsets.UTF_8)