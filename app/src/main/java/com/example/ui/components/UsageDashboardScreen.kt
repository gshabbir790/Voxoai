package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.UsageStats
import com.example.ui.theme.*

@Composable
fun UsageDashboardScreen(
    usageStats: UsageStats?,
    savedProjectsCount: Int,
    modifier: Modifier = Modifier
) {
    val totalChars = usageStats?.totalCharactersProcessed ?: 0
    val totalAudioMinutes = ((usageStats?.totalAudioSecondsGenerated ?: 0f) / 60f)
    val limitChars = usageStats?.creditLimitCharacters ?: 100000
    val usagePercent = (totalChars.toFloat() / limitChars.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("usage_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Tier Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(24.dp))
                            Column {
                                Text("Voxora Studio Tier", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Enterprise Creative Suite", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                            }
                        }

                        Text(
                            text = "PRO STUDIO",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = CyanAccent),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyanAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Progress Bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Monthly Production Quota", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("$totalChars / $limitChars characters", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent))
                        }
                        LinearProgressIndicator(
                            progress = usagePercent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = CyanAccent,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }

        item {
            // Metrics 3-Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Characters",
                    value = "$totalChars",
                    icon = Icons.Default.TextFields,
                    tint = CyanAccent,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Audio Generated",
                    value = String.format("%.1fm", totalAudioMinutes),
                    icon = Icons.Default.GraphicEq,
                    tint = IndigoAccent,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Projects",
                    value = "$savedProjectsCount",
                    icon = Icons.Default.Folder,
                    tint = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Feature Matrix Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "STUDIO CAPABILITIES MATRIX",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                    )

                    FeatureRow("Official Google Gemini 2.5 Flash TTS", "Active")
                    FeatureRow("Strict 7-Language Studio Suite", "Urdu, English, Arabic, Persian, Pashto, Chinese, Hindi")
                    FeatureRow("AI Script Director (Smart Voice Mode)", "Gemini 3.5 Flash Engine")
                    FeatureRow("3-Track Mixer with Auto-Ducking", "Hardware-accelerated Envelope detection")
                    FeatureRow("Multi-Scene Script Production", "Up to 50 scenes per project")
                    FeatureRow("30 Voice Character Gallery", "Full acoustic range")
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
            Text(text = title, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}

@Composable
fun FeatureRow(title: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(14.dp))
            Text(text = title, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface))
        }
        Text(text = status, style = MaterialTheme.typography.labelSmall.copy(color = CyanAccent, fontSize = 10.sp))
    }
}
