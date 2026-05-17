package com.example.finalproject.presentation.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class MockIncidentForAuth(
    val id: String,
    val type: String,
    val severity: String,
    val description: String,
    val status: String,
    val reportedBy: String,
    val timestamp: String,
    val location: String
)

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    var incidentsList by remember {
        mutableStateOf(
            listOf(
                MockIncidentForAuth(
                    "1",
                    "Fire",
                    "Critical",
                    "Building fire in downtown area, multiple units responding",
                    "pending",
                    "Citizen_123",
                    "2 hours ago",
                    "Downtown Center, St. 45"
                ),
                MockIncidentForAuth(
                    "2",
                    "Traffic Accident",
                    "High",
                    "Multi-car collision at main intersection",
                    "verified",
                    "Citizen_456",
                    "1 hour ago",
                    "Main St & 5th Ave"
                ),
                MockIncidentForAuth(
                    "3",
                    "Flood",
                    "Critical",
                    "Rising water levels in residential area",
                    "pending",
                    "Citizen_789",
                    "30 min ago",
                    "Low Valley District"
                ),
                MockIncidentForAuth(
                    "4",
                    "Crime",
                    "High",
                    "Suspicious activity near shopping center",
                    "resolved",
                    "Citizen_321",
                    "1 hour ago",
                    "Mall Area"
                ),
                MockIncidentForAuth(
                    "5",
                    "Pollution",
                    "Medium",
                    "Industrial emission detected",
                    "verified",
                    "Citizen_654",
                    "45 min ago",
                    "Industrial Zone"
                )
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Authority Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Statistics Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatisticCard(
                label = "Pending",
                value = incidentsList.count { it.status == "pending" },
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
            StatisticCard(
                label = "Verified",
                value = incidentsList.count { it.status == "verified" },
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
            StatisticCard(
                label = "Resolved",
                value = incidentsList.count { it.status == "resolved" },
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }
        
        Text(
            "Incident Reports",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Incidents List
        incidentsList.forEach { incident ->
            IncidentCard(
                incident = incident,
                onApprove = { 
                    incidentsList = incidentsList.map {
                        if (it.id == incident.id) it.copy(status = "verified") else it
                    }
                },
                onDeny = {
                    incidentsList = incidentsList.map {
                        if (it.id == incident.id) it.copy(status = "denied") else it
                    }
                },
                onResolve = {
                    incidentsList = incidentsList.map {
                        if (it.id == incident.id) it.copy(status = "resolved") else it
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun StatisticCard(label: String, value: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun IncidentCard(
    incident: MockIncidentForAuth,
    onApprove: () -> Unit,
    onDeny: () -> Unit,
    onResolve: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (incident.severity) {
                "Critical" -> Color(0xFFFFEBEE)
                "High" -> Color(0xFFFFF3E0)
                else -> Color(0xFFFAFAFA)
            }
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.foundation.BorderStroke(
                1.dp,
                when (incident.severity) {
                    "Critical" -> Color(0xFFEF5350)
                    "High" -> Color(0xFFFF9800)
                    else -> Color(0xFF9E9E9E)
                }
            ).brush
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = when (incident.severity) {
                                "Critical" -> Color(0xFFEF5350)
                                "High" -> Color(0xFFFF9800)
                                else -> Color(0xFF9E9E9E)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(incident.type, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ID: ${incident.id} • ${incident.timestamp}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
                
                // Status Badge
                Surface(
                    color = when (incident.status) {
                        "pending" -> Color(0xFFFF9800)
                        "verified" -> Color(0xFF2196F3)
                        "resolved" -> Color(0xFF4CAF50)
                        else -> Color(0xFFE91E63)
                    }.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        incident.status.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (incident.status) {
                            "pending" -> Color(0xFFFF9800)
                            "verified" -> Color(0xFF2196F3)
                            "resolved" -> Color(0xFF4CAF50)
                            else -> Color(0xFFE91E63)
                        }
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Details
            Text("Description", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(incident.description, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📍 ${incident.location}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                Surface(
                    color = when (incident.severity) {
                        "Critical" -> Color(0xFFEF5350)
                        "High" -> Color(0xFFFF9800)
                        else -> Color(0xFF4CAF50)
                    }.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        incident.severity,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (incident.status == "pending") {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Filled.Check, "Approve", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve")
                    }
                    Button(
                        onClick = onDeny,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Icon(Icons.Filled.Close, "Deny", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Deny")
                    }
                } else if (incident.status == "verified") {
                    Button(
                        onClick = onResolve,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text("Mark as Resolved")
                    }
                } else if (incident.status == "resolved") {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    ) {
                        Text("✓ Completed")
                    }
                } else {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    ) {
                        Text("✗ Denied")
                    }
                }
            }
        }
    }
}
