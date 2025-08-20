package com.example.esp32app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DistanceDashboard() {
    val viewModel = remember { DistanceViewModel() }
    var ipAddress by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E12))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderRow()
        Spacer(modifier = Modifier.height(16.dp))
        ConnectionRow(ipAddress) { newIp ->
            ipAddress = newIp
            // Trigger mDNS discovery or connect to the new IP
            viewModel.startPolling(ipAddress)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LiveDistanceCard(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressCard()
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("ESP32 · HC-SR04", color = Color(0xFFE6EDF3), fontSize = 20.sp)
        Text("IP: ...", color = Color(0xFFE6EDF3), fontSize = 20.sp)
        // Connection status indicator can be added here
    }
}

@Composable
fun ConnectionRow(ipAddress: String, onIpChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = ipAddress,
            onValueChange = onIpChange,
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFF141A21))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { 
            // Discover button action
            // Call the discover function in the view model
            viewModel.discoverESP32() // Uncomment this line when the function is implemented
        }) {
            Text("Discover")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { 
            // Export CSV action
            val filePath = "esp32_distance.csv" // Define the file path
            viewModel.exportCSV(filePath) // Call the export function
        }) {
            Text("Export CSV")
        }
    }
}

@Composable
fun LiveDistanceCard(viewModel: DistanceViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141A21))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Live Distance", color = Color(0xFFE6EDF3), fontSize = 24.sp)
            Text("Distance: ${viewModel.distance} cm", color = Color(0xFFE6EDF3), fontSize = 32.sp)
            // Add min/max/avg display here
            Text("Min: 100 cm", color = Color(0xFF9FB1C1))
            Text("Max: 200 cm", color = Color(0xFF9FB1C1))
            Text("Avg: 150 cm", color = Color(0xFF9FB1C1))
        }
    }
}

@Composable
fun ProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141A21))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Progress", color = Color(0xFFE6EDF3), fontSize = 24.sp)
            // Add progress bar and sparkline chart here
            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier.fillMaxWidth().height(24.dp),
                color = Color(0xFF21D07A)
            )
            // Sparkline chart implementation can be added here
        }
    }
}
