package com.example.pract_10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pract_10.ui.theme.Pract_10Theme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pract_10Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeolocationScreen(
                        viewModel = koinViewModel(),
                        modifier = Modifier.padding(innerPadding)
                        )
                }
            }
        }
    }
}
