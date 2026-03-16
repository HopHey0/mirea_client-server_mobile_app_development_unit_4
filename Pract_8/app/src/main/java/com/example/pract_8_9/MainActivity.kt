package com.example.pract_8_9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.pract_8_9.ui.theme.Pract_8_9Theme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pract_8_9Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val photoLoaderViewModel: PhotoLoaderViewModel = koinViewModel()
                    PhotoLoaderScreen(
                        modifier = Modifier.padding(innerPadding),
                        photoLoaderViewModel
                    )
                }
            }
        }
    }
}