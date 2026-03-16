package com.example.pract_10

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pract_10.ui.theme.Pract_10Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeolocationScreen(
    viewModel: GeoScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        viewModel.onPermissionResult(
            isCoarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true,
            isFineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        )
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.headerText,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        if (!uiState.isLoading) {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = uiState.bodyText,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        } else {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.padding(vertical = 30.dp))

        Button(
            onClick = { viewModel.fetchLocation() },
            enabled = ((uiState.fineGranted && uiState.coarseGranted && !uiState.isLoading))
        ) {
            Text (
                text = stringResource(R.string.getLocationButtonText)
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun GeolocationScreenPreview(){
    Pract_10Theme {
        GeolocationScreen(
            viewModel = koinViewModel()
        )
    }
}