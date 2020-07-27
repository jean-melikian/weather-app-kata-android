package fr.ozoneprojects.weatherlib.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUtils {
    val moshi: Moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()
}