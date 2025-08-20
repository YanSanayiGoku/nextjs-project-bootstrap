package com.example.esp32app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.esp32app.network.NetworkManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.FileWriter
import java.io.IOException

data class DistanceResponse(val ok: Boolean, val distance_cm: Double)

class DistanceViewModel : ViewModel() {
    private val networkManager = NetworkManager()
    var distance by mutableStateOf(0.0)
    private var history = mutableListOf<Double>()

    fun discoverESP32() {
        // Implement mDNS discovery logic
        networkManager.discoverESP32()
    }

    fun startPolling(ip: String) {
        viewModelScope.launch {
            while (true) {
                val response = withContext(Dispatchers.IO) {
                    networkManager.pollDistance(ip)
                }
                if (response != null && response.isSuccessful) {
                    val json = response.body?.string()
                    // Parse JSON and update distance
                    distance = parseDistance(json)
                    updateHistory(distance)
                }
                // Delay for 300 ms
                kotlinx.coroutines.delay(300)
            }
        }
    }

    private fun parseDistance(json: String?): Double {
        return try {
            val distanceResponse = Gson().fromJson(json, DistanceResponse::class.java)
            if (distanceResponse.ok) {
                distanceResponse.distance_cm
            } else {
                0.0 // Handle error case
            }
        } catch (e: JsonSyntaxException) {
            0.0 // Handle parsing error
        }
    }

    private fun updateHistory(newDistance: Double) {
        if (history.size >= 120) {
            history.removeAt(0) // Remove oldest value
        }
        history.add(newDistance)
    }

    fun exportCSV(filePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File(filePath)
                val writer = FileWriter(file)
                writer.append("Index,Distance_cm\n")
                history.forEachIndexed { index, distance ->
                    writer.append("$index,$distance\n")
                }
                writer.flush()
                writer.close()
            } catch (e: IOException) {
                // Handle file writing error
            }
        }
    }
}
