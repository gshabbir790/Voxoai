package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IceBlueAccent
import com.example.ui.theme.ObsidianBorderSubtle
import com.example.ui.theme.ObsidianNav
import com.example.ui.theme.TextMutedDark
import com.example.ui.viewmodel.StudioScreen

data class NavItem(
    val screen: StudioScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val STUDIO_NAV_ITEMS = listOf(
    NavItem(StudioScreen.STUDIO, "Studio", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
    NavItem(StudioScreen.PROJECTS, "Projects", Icons.Filled.Folder, Icons.Outlined.Folder),
    NavItem(StudioScreen.SAMPLES, "Samples", Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic),
    NavItem(StudioScreen.USAGE, "Usage", Icons.Filled.Analytics, Icons.Outlined.Analytics)
)

@Composable
fun StudioBottomNavigation(
    currentScreen: StudioScreen,
    onScreenSelected: (StudioScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("studio_bottom_nav"),
        color = ObsidianNav,
        tonalElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            STUDIO_NAV_ITEMS.forEach { item ->
                val isSelected = currentScreen == item.screen
                val tintColor by animateColorAsState(
                    targetValue = if (isSelected) IceBlueAccent else TextMutedDark,
                    label = "nav_tint"
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onScreenSelected(item.screen) }
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                        .testTag("nav_item_${item.screen.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = tintColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = item.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 9.sp,
                            letterSpacing = 0.8.sp
                        ),
                        color = tintColor
                    )
                }
            }
        }
    }
}
