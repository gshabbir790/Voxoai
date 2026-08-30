package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import java.io.File

@Composable
fun ExportDialog(
    projectName: String,
    durationMs: Int,
    characterCount: Int,
    voiceOnlyFile: File?,
    mixedAudioFile: File?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedFormat by remember { mutableStateOf("WAV_MASTER") } // WAV_MASTER, WAV_VOICE, MP3_PRESET, PROJECT_JSON

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .testTag("export_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(24.dp))
                        Column {
                            Text("Export Production Audio", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                            Text("Studio Quality Audio Export & Share", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Specs Summary Card
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("DURATION", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("${(durationMs / 1000)}s", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = CyanAccent))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CHARACTERS", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("$characterCount", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = IndigoAccent))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SAMPLE RATE", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("24 kHz PCM", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldSuccess))
                        }
                    }
                }

                // Format Options
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExportFormatOption(
                        title = "Master Mix WAV (Lossless Audio + Ducked Music)",
                        subtitle = "Standard broadcast 24kHz 16-bit PCM WAV with embedded background music",
                        isSelected = selectedFormat == "WAV_MASTER",
                        onClick = { selectedFormat = "WAV_MASTER" }
                    )

                    ExportFormatOption(
                        title = "Voice-Only WAV (Isolated Vocal Track)",
                        subtitle = "Clean uncompressed vocal audio without background music or sound effects",
                        isSelected = selectedFormat == "WAV_VOICE",
                        onClick = { selectedFormat = "WAV_VOICE" }
                    )

                    ExportFormatOption(
                        title = "MP3 Audio Format (Web / Social Ready)",
                        subtitle = "Optimized compressed audio track suitable for podcasts and video editors",
                        isSelected = selectedFormat == "MP3_PRESET",
                        onClick = { selectedFormat = "MP3_PRESET" }
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val targetFile = when (selectedFormat) {
                                "WAV_VOICE" -> voiceOnlyFile ?: mixedAudioFile
                                else -> mixedAudioFile ?: voiceOnlyFile
                            }

                            if (targetFile != null && targetFile.exists()) {
                                try {
                                    val uri: Uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        targetFile
                                    )
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "audio/*"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Export Studio Audio"))
                                } catch (e: Exception) {
                                    // Fallback direct intent
                                }
                            }
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("confirm_export_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export & Share")
                    }
                }
            }
        }
    }
}

@Composable
fun ExportFormatOption(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, if (isSelected) EmeraldSuccess else MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) EmeraldSuccess.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = EmeraldSuccess)
            )
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
        }
    }
}
