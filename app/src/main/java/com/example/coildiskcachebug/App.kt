package com.example.coildiskcachebug

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.request.CachePolicy

class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maximumMaxSizeBytes(104900000) // 100 MB
                    .build()
            }
            .crossfade(true)
            .build()
    }

}