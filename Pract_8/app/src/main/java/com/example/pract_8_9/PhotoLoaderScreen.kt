package com.example.pract_8_9

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pract_8_9.ui.theme.Pract_8_9Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhotoLoaderScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotoLoaderViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(45.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f),
            text = uiState.value.headerStatusText,
            style = MaterialTheme.typography.displayMedium,
            color = if (uiState.value.error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if (uiState.value.isLoading) {
            LinearProgressIndicator()
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Text(
            text = uiState.value.photoStatusText,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = 40.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            onClick = viewModel::startLoading,
            enabled = !uiState.value.isLoading
        ) {
            Text(
                text = stringResource(R.string.photoLoaderButton)
            )
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PhotoLoaderScreenPreview() {
//    Pract_8_9Theme {
//        val vm = (Application())
//        PhotoLoaderScreen(
//            viewModel = vm
//        )
//    }
//}