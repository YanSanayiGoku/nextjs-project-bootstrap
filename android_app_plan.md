# Android App Development Plan

## Project Overview
Create a fully functional Android app in Kotlin using Jetpack Compose and Material 3 that connects to an ESP32 web server.

## Project Structure
- **app/**
  - **src/**
    - **main/**
      - **java/com/example/esp32app/** (Main Kotlin files)
      - **res/** (Resources like layouts, drawables, etc.)
      - **AndroidManifest.xml** (App permissions and configurations)
      - **build.gradle** (Module-level Gradle file)
      - **settings.gradle** (Project settings)

## Dependencies
- OkHttp 4.12.0
- Gson 2.11.0
- JmDNS 3.5.8
- Lifecycle Runtime KTX 2.8.6
- Compose BOM 2024.06.00
- Material3 1.3.0

## AndroidManifest.xml
- Permissions:
  - INTERNET
  - CHANGE_WIFI_MULTICAST_STATE
- FileProvider configuration for CSV export.

## UI Components
1. **Header Row**: 
   - Device name badge
   - IP address display
   - Connection status indicator (green/red dot)
   - Buttons for theme selection, unit toggle, and refresh.

2. **Connection Row**:
   - OutlinedTextField for manual IP entry
   - Discover button for mDNS
   - Export CSV button.

3. **Main Cards**:
   - **Live Distance Card**: Display distance, min/max/avg, and time.
   - **Progress + Sparkline Card**: Progress bar and sparkline chart.

## Networking
- Implement mDNS discovery on startup.
- Poll the distance endpoint every 300 ms.
- Handle successful and failed connections.

## Data Management
- Maintain a history of the last 120 distance values.
- Implement CSV export functionality using FileProvider.

## Follow-Up Steps
- Create the project in Android Studio.
- Implement the above features step by step.
- Test the app on an Android device/emulator.
