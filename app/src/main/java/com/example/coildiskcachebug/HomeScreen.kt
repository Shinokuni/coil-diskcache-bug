package com.example.coildiskcachebug

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class HomeScreen: Screen {

    private val MEMORY_KEY = "MEMORY_KEY"
    private val DISK_CACHE_KEY = "DISK_CACHE_KEY"

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        loadMemoryCacheIcon(context)
                        navigator.push(IconScreen(MEMORY_KEY))
                    }
                }) {
                    Text(text = "Memory cache")
                }

                AsyncImage(
                    model = MEMORY_KEY,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.size(16.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        loadDiskCacheIcon(context)
                        navigator.push(IconScreen(DISK_CACHE_KEY))
                    }
                }) {
                    Text(text = "Disk cache")
                }

                AsyncImage(
                    model = DISK_CACHE_KEY,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    private suspend fun loadMemoryCacheIcon(context: Context) {
        val imageLoader = context.imageLoader

        val request = ImageRequest.Builder(context)
            .memoryCacheKey(MEMORY_KEY)
            .data(loadIconByteArray(context))
            .build()

        imageLoader.execute(request)
    }

    private suspend fun loadDiskCacheIcon(context: Context) {
        val imageLoader = context.imageLoader

        val request = ImageRequest.Builder(context)
            .diskCacheKey(DISK_CACHE_KEY)
            .data(loadIconByteArray(context))
            .build()

        imageLoader.execute(request)
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun loadIconByteArray(context: Context): ByteArray {
        val inputStream = context.assets.open("favicon.json")
        val string = BufferedReader(inputStream.reader()).readText()
        val jsonObject = JSONObject(string)

        val data = jsonObject["data"] as String
        val base64Image = data.substringAfter("base64,")
        return Base64.decode(base64Image.toByteArray())
    }

}