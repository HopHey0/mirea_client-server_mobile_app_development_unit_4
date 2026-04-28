package com.example.pract_5_7

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pract_5_7.ui.theme.Pract_5_7Theme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import java.util.Timer
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        enableEdgeToEdge()
        setContent {
            Pract_5_7Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    TimerScreen(modifier = Modifier.padding(innerPadding))
                    RandomNumberScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier
    ) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = CountState.time.collectAsState().value.toString(),
            modifier = modifier,
            fontSize = 84.sp
        )
        Button(
            modifier = Modifier.width(200.dp).height(80.dp),
            onClick = { context.startService(Intent(context, TimerForegroundService::class.java)) }
        ) {
            Text("Start")
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(
            modifier = Modifier.width(200.dp).height(80.dp),
            onClick = { context.stopService(Intent(context, TimerForegroundService::class.java)) }
        ) {
            Text("Stop")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    Pract_5_7Theme {
        TimerScreen()
    }
}

@Composable
fun InvisibleTimerScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val count = if (CountState.time.collectAsState().value == 0) "" else CountState.time.collectAsState().value.toString()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = count,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }){
                    if (newValue.isNotBlank()) {
                        CountState.changeTime(newValue.toInt())
                    } else {
                        CountState.changeTime(0)
                    }
                }},
            label = { Text(text = "Секунды") },
            //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            modifier = Modifier.width(200.dp).height(80.dp),
            onClick = {
                context.startService(Intent(context, TimerBackgroundService::class.java))
            }
        ) {
            Text("Запустить таймер")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvisibleTimerScreenPreview() {
    Pract_5_7Theme {
        InvisibleTimerScreen()
    }
}

@Composable
fun RandomNumberScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val count = CountState.time.collectAsState().value
    var timerService by remember { mutableStateOf<BoundService?>(null) }
    var isConnected by remember { mutableStateOf(false) }
    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerService = (service as BoundService.MyBinder).service
                isConnected = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                timerService = null
                isConnected = false
            }
        }
    }

    DisposableEffect(Unit) {
        context.startService(Intent(context, BoundService::class.java))
        onDispose {
            context.stopService(Intent(context, BoundService::class.java))
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (!isConnected) {
                    "/"
                } else {
                    count.toString()
                },
            fontSize = 42.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            modifier = Modifier.width(200.dp).height(80.dp),
            onClick = {
                val intent = Intent(context, BoundService::class.java)
                if (!isConnected) {
                    context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
                } else {
                    isConnected = false
                    context.unbindService(connection)
                }
            }
        ) {
            Text(text = if (!isConnected) {
                "Подключиться"
            } else {
                "Отключиться"
            }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RandomNumberScreenPreview() {
    Pract_5_7Theme {
        RandomNumberScreen()
    }
}