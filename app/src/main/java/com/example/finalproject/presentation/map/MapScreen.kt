package com.example.finalproject.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen(viewModel: MapViewModel) {
    var showAlertsList by remember { mutableStateOf(false) }
    var selectedIncident by remember { mutableStateOf<MockIncident?>(null) }
    
    // Mock incidents for Tunisia
    val mockIncidents = listOf(
        MockIncident("Fire at Downtown Tunis", 0.3f, 0.2f, "Fire", "Critical", "Building fire in city center"),
        MockIncident("Traffic Accident", 0.5f, 0.35f, "Traffic Accident", "High", "Multi-car collision on main road"),
        MockIncident("Flood Warning Sfax", 0.2f, 0.5f, "Flood", "Medium", "Rising water levels in low areas"),
        MockIncident("Pollution Alert", 0.6f, 0.15f, "Pollution", "Low", "Air quality degraded"),
        MockIncident("Crime in Progress", 0.1f, 0.6f, "Crime", "High", "Suspicious activity reported"),
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Mock Tunisia Map Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8DCC8)) // Sandy/beige Tunisia color
        )
        
        // Markers Layer
        for (incident in mockIncidents) {
            MarkerCircle(
                incident = incident,
                onIncidentClick = { selectedIncident = incident }
            )
        }
        
        // Legend
        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Alert Severity", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LegendItem(Color.Red, "Critical")
                LegendItem(Color(0xFFFFA500), "High")
                LegendItem(Color.Yellow, "Medium")
                LegendItem(Color.Green, "Low")
            }
        }
        
        // Incident Details Card (when selected)
        selectedIncident?.let { incident ->
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .widthIn(max = 250.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(incident.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Type: ${incident.type}", style = MaterialTheme.typography.labelSmall)
                    Text("Severity: ${incident.severity}", style = MaterialTheme.typography.labelSmall, color = Color.Red)
                    Text(incident.description, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        
        // Floating Action Button
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { showAlertsList = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("${mockIncidents.size}", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        
        // Alerts List Sheet
        if (showAlertsList) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showAlertsList = false }
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Active Incidents (${mockIncidents.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { showAlertsList = false }) {
                                Icon(Icons.Filled.Close, "Close")
                            }
                        }
                        
                        LazyColumn {
                            items(mockIncidents) { incident ->
                                IncidentAlertItem(incident)
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun MarkerCircle(incident: MockIncident, onIncidentClick: () -> Unit) {
    Box(
        modifier = Modifier
            .offset(
                x = 300.dp * incident.xPercent,
                y = 300.dp * incident.yPercent
            )
            .size(24.dp)
            .background(
                color = when (incident.severity) {
                    "Critical" -> Color.Red
                    "High" -> Color(0xFFFFA500)
                    "Medium" -> Color.Yellow
                    else -> Color.Green
                },
                shape = CircleShape
            )
            .clickable { onIncidentClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            "!",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.labelSmall.fontSize
        )
    }
}

@Composable
private fun IncidentAlertItem(incident: MockIncident) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.LocationOn,
            contentDescription = null,
            tint = when (incident.severity) {
                "Critical" -> Color.Red
                "High" -> Color(0xFFFFA500)
                "Medium" -> Color.Yellow
                else -> Color.Green
            },
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(incident.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(incident.description, style = MaterialTheme.typography.bodySmall)
            Text("Type: ${incident.type}", style = MaterialTheme.typography.labelSmall)
        }
        
        Surface(
            color = when (incident.severity) {
                "Critical" -> Color.Red.copy(alpha = 0.2f)
                "High" -> Color(0xFFFFA500).copy(alpha = 0.2f)
                "Medium" -> Color.Yellow.copy(alpha = 0.2f)
                else -> Color.Green.copy(alpha = 0.2f)
            },
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                incident.severity,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class MockIncident(
    val title: String,
    val xPercent: Float,  // 0.0 to 1.0 (x position on map)
    val yPercent: Float,  // 0.0 to 1.0 (y position on map)
    val type: String,
    val severity: String,
    val description: String
)
