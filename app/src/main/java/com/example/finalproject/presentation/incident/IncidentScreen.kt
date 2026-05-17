package com.example.finalproject.presentation.incident

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalproject.domain.model.Incident
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import java.io.InputStream
import android.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IncidentScreen(viewModel: IncidentViewModel) {
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Fire") }
    var severity by remember { mutableStateOf("Low") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val incidents = viewModel.incidents.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var typeExpanded by remember { mutableStateOf(false) }
    var severityExpanded by remember { mutableStateOf(false) }
    var showCameraOptions by remember { mutableStateOf(false) }

    val typeOptions = listOf("Fire", "Flood", "Traffic Accident", "Crime", "Pollution", "Other")
    val severityOptions = listOf("Low", "Medium", "High", "Critical")

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Camera intent launcher
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && selectedImageUri != null) {
            val inputStream: InputStream? = context.contentResolver.openInputStream(selectedImageUri!!)
            inputStream?.let {
                val bitmap = BitmapFactory.decodeStream(it)
                imageBitmap = bitmap.asImageBitmap()
            }
        }
    }

    // File picker launcher
    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                val bitmap = BitmapFactory.decodeStream(it)
                imageBitmap = bitmap.asImageBitmap()
            }
        }
    }

    fun openCamera() {
        if (cameraPermissionState.status is PermissionStatus.Granted) {
            val photoFile = java.io.File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            photoFile.parentFile?.mkdirs()
            photoFile.createNewFile()
            val photoUri = androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            selectedImageUri = photoUri
            cameraLauncher.launch(photoUri)
        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Report Incident",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Camera/Photo Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .clickable { showCameraOptions = true },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "Selected photo",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📷", style = MaterialTheme.typography.displaySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tap to take a photo or select from gallery",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Camera Options Menu
        DropdownMenu(expanded = showCameraOptions, onDismissRequest = { showCameraOptions = false }) {
            DropdownMenuItem(
                text = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📷")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Take Photo")
                    }
                },
                onClick = {
                    showCameraOptions = false
                    openCamera()
                }
            )
            DropdownMenuItem(
                text = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🖼️")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Choose from Gallery")
                    }
                },
                onClick = {
                    showCameraOptions = false
                    pickerLauncher.launch("image/*")
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Show form only after photo is taken
        if (imageBitmap != null) {
            // Description
            Text(
                "Description",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Describe what happened...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4,
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Type Dropdown
            Text(
                "Incident Type",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { typeExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(type)
                }
                DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                type = option
                                typeExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Severity Dropdown
            Text(
                "Severity Level",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { severityExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(severity)
                }
                DropdownMenu(expanded = severityExpanded, onDismissRequest = { severityExpanded = false }) {
                    severityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                severity = option
                                severityExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    isLoading = true
                    error = null
                    coroutineScope.launch {
                        try {
                            var mediaUrls = emptyList<String>()
                            if (selectedImageUri != null) {
                                val inputStream = context.contentResolver.openInputStream(selectedImageUri!!)
                                val bytes = inputStream?.readBytes()
                                if (bytes != null) {
                                    val repo = viewModel.javaClass.getDeclaredField("incidentUseCases").apply { isAccessible = true }.get(viewModel) as com.example.finalproject.domain.usecase.IncidentUseCases
                                    val firestoreRepo = repo.javaClass.getDeclaredField("incidentRepository").apply { isAccessible = true }.get(repo)
                                    val uploadMethod = firestoreRepo.javaClass.getMethod("uploadMedia", ByteArray::class.java, String::class.java)
                                    val url = uploadMethod.invoke(firestoreRepo, bytes, "${System.currentTimeMillis()}.jpg") as String
                                    mediaUrls = listOf(url)
                                }
                            }
                            viewModel.reportIncident(
                                Incident(
                                    description = description,
                                    type = type,
                                    severity = severity,
                                    location = Pair(36.8, 10.2),
                                    timestamp = System.currentTimeMillis(),
                                    mediaUrls = mediaUrls
                                )
                            )
                            description = ""
                            type = "Fire"
                            severity = "Low"
                            selectedImageUri = null
                            imageBitmap = null
                        } catch (e: Exception) {
                            error = e.message
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Submit Report", fontWeight = FontWeight.Bold)
            }

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Incidents Section
        Text(
            "Recent Incidents",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        incidents.take(5).forEach { incident ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Type: ${incident.type}", fontWeight = FontWeight.Bold)
                        Surface(
                            color = when (incident.severity) {
                                "Critical" -> MaterialTheme.colorScheme.error
                                "High" -> MaterialTheme.colorScheme.errorContainer
                                else -> MaterialTheme.colorScheme.tertiary
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                incident.severity,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Description: ${incident.description}", style = MaterialTheme.typography.bodySmall)
                    if (incident.mediaUrls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("📷 Photo attached", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

