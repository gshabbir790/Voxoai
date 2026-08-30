package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.ApiKeyStorage

@Composable
fun SettingsDialog(
    apiKeyStorage: ApiKeyStorage,
    onDismiss: () -> Unit,
    onKeyUpdated: () -> Unit
) {
    var apiKeyText by remember { mutableStateOf(apiKeyStorage.getApiKey()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("API Settings") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Please enter your API Key:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = apiKeyText,
                    onValueChange = { apiKeyText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("API Key") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    apiKeyStorage.saveApiKey(apiKeyText)
                    onKeyUpdated()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
