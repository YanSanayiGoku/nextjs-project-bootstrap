package com.example.esp32app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.esp32app.network.NetworkManager
import com.example.esp32app.ui.DistanceDashboard

class MainActivity : ComponentActivity() {
    private val networkManager = NetworkManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                DistanceDashboard()
            }
        }
        networkManager.discoverESP32() // Start mDNS discovery
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        DistanceDashboard()
    }
}
