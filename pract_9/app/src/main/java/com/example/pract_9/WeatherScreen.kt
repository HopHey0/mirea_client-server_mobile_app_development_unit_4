package com.example.pract_9

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun ParentWeatherScreen(
    viewModel: WeatherScreenViewModel,
    modifier: Modifier = Modifier
){
    CheckPermission(viewModel, modifier)
}

@Composable
fun WeatherScreen(
    viewModel: WeatherScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.value.isLoaded) {
        Log.d("UI", "isLoaded changed to: ${uiState.value.isLoaded}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.weatherTitle),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        Text(
            text = uiState.value.loadStatus,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.4f).padding(vertical = 10.dp)
        ) {
            items(uiState.value.cityList) { item ->
                WeatherCard(item)
            }
        }

        if (!uiState.value.isLoaded){
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        } else {
            SummaryCard(uiState.value.cityList)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }

        Button(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            onClick = { viewModel.getWeather() },
            enabled = !uiState.value.isLoading
        ) {
            Text(
                text = if (uiState.value.isLoading)
                    stringResource(R.string.startGatheringWeatherButtonTextInProgress)
                else
                    stringResource(R.string.startGatheringWeatherButtonText)
            )
        }
        if (uiState.value.isLoading) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                onClick = { viewModel.cancelGettingWeather() }
            ) {
                Text(
                    text = stringResource(R.string.abortGatheringWeatherButtonText)
                )
            }
        }
    }
}

@Composable
fun WeatherCard(
    cityWeather: CityWeather
){
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(all = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier.weight(8f)
            ) {
                Text(
                    text = cityWeather.name
                )

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                Text(
                    text = cityWeather.state,
                    color = if (!cityWeather.error) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier.weight(2f),
                contentAlignment = Alignment.Center
            ) {
                if (cityWeather.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = cityWeather.temp,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    cityList: List<CityWeather>
){
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
        ) {
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = cityList.toSummaryString()
            )
        }
    }
}

@Composable
fun CheckPermission(
    weatherScreenViewModel: WeatherScreenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else {
            mutableStateOf(true)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasNotificationPermission = isGranted }
    )

    LaunchedEffect(key1 = hasNotificationPermission) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && !hasNotificationPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (hasNotificationPermission) { WeatherScreen(weatherScreenViewModel, modifier) }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showSystemUi = true)
@Composable
fun WeatherScreenPreview(){
    val viewModel = WeatherScreenViewModel(Application())
    MaterialTheme {
        WeatherScreen(viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherCardPreview(){
    MaterialTheme {
        WeatherCard(CityWeather(
            name = "Москва",
            temp = "+10C",
            weather = "Ясно",
            state = "Ожидание"
        ))
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryCardPreview(){
    MaterialTheme {
        SummaryCard(cityList
        )
    }
}