package com.example.esp32app.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

data class DistanceResponse(val ok: Boolean, val distance_cm: Float)

class NetworkManager {
    private val client = OkHttpClient()
    private var discoveredIP: String? = null
    private val distanceHistory = mutableListOf<Float>()

    fun discoverESP32() {
        val jmdns = JmDNS.create()
        jmdns.addServiceListener("_http._tcp.local.", object : ServiceListener {
            override fun serviceAdded(event: ServiceEvent) {
                // Service found
                val serviceInfo = jmdns.getServiceInfo(event.type, event.name)
                discoveredIP = serviceInfo.inetAddresses[0].hostAddress
                // Connect to the discovered ESP32
            }

            override fun serviceRemoved(event: ServiceEvent) {
                // Service removed
            }

            override fun serviceResolved(event: ServiceEvent) {
                // Service resolved
            }
        })
    }

    fun pollDistance(ip: String): Response? {
        val request = Request.Builder()
            .url("http://$ip/distance")
            .build()

        val response = client.newCall(request).execute()
        if (response?.isSuccessful == true) {
            val jsonData = response.body?.string()
            // Parse JSON and update distance
            val distance = parseDistance(jsonData)
            distanceHistory.add(distance)
        }
        return response
    }

    private fun parseDistance(jsonData: String?): Float {
        val gson = Gson()
        val distanceResponse = gson.fromJson(jsonData, DistanceResponse::class.java)
        return distanceResponse.distance_cm
    }

    fun exportCSV(filePath: String) {
        val file = File(filePath)
        FileOutputStream(file).use { outputStream ->
            outputStream.write("index,distance_cm\n".toByteArray())
            distanceHistory.forEachIndexed { index, distance ->
                outputStream.write("$index,$distance\n".toByteArray())
            }
        }
    }
}
