package com.example.pract_14

import android.R.attr.maxWidth
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pract_14.ui.theme.Pract_14Theme
import kotlin.io.path.Path
import kotlin.io.path.moveTo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pract_14Theme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CompassScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun CompassScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val accelSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val magSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) }

    val sensorsAvailable = accelSensor != null && magSensor != null

    if (!sensorsAvailable) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Устройство не поддерживает датчик ориентации",
                color = Color.Red,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(32.dp)
            )
        }
        return
    }

    var azimuth by rememberSaveable { mutableStateOf(0f) }
    var smoothedAzimuth by rememberSaveable{ mutableStateOf(0f) }
    val alpha = 0.15f

    fun updateAzimuth(accel: FloatArray, mag: FloatArray) {
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        SensorManager.getRotationMatrix(rotationMatrix, null, accel, mag)
        SensorManager.getOrientation(rotationMatrix, orientation)

        var newAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
        if (newAzimuth < 0f) newAzimuth += 360f

        var delta = newAzimuth - smoothedAzimuth
        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f

        smoothedAzimuth += delta * alpha
        azimuth = smoothedAzimuth
    }

    DisposableEffect(Unit) {

        var lastAccel = FloatArray(3)
        var lastMag = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> lastAccel = event.values.copyOf()
                    Sensor.TYPE_MAGNETIC_FIELD -> lastMag = event.values.copyOf()
                }
                if (lastAccel.any { it != 0f } && lastMag.any { it != 0f }) {
                    updateAzimuth(lastAccel, lastMag)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val animatedAzimuth by animateFloatAsState(
        targetValue = azimuth,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
        label = "compass_rotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Компас",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .background(Color(0xFF1E1E1E), CircleShape)
                    .border(2.dp, Color(0xFF444444), CircleShape)
            ) {
                Text(
                    text = "N",
                    color = Color.Red,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .rotate(-animatedAzimuth)
                ) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val arrowLength = centerY * 0.9f
                    val arrowWidth = size.width * 0.05f

                    drawPath(
                        path = Path().apply {
                            moveTo(centerX, centerY - arrowLength)
                            lineTo(centerX - arrowWidth, centerY)
                            lineTo(centerX + arrowWidth, centerY)
                            close()
                        },
                        color = Color.Red
                    )

                    drawPath(
                        path = Path().apply {
                            moveTo(centerX, centerY + arrowLength)
                            lineTo(centerX - arrowWidth, centerY)
                            lineTo(centerX + arrowWidth, centerY)
                            close()
                        },
                        color = Color(0xFF888888)
                    )
                }
            }
        }

        Text(
            text = "Азимут: ${azimuth.toInt()}°",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}