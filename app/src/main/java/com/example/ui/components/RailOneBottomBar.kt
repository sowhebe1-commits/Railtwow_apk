package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class RailOneNavTab {
  HOME,
  BOOKINGS,
  YOU,
  MENU,
}

val RailBarBlue = Color(0xFF0D6EFD)

@Composable
fun RailOneBottomNavBar(
  currentTab: RailOneNavTab,
  onTabSelected: (RailOneNavTab) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    color = RailBarBlue,
    shadowElevation = 8.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .navigationBarsPadding(),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavTabButton(
        title = "Home",
        icon = Icons.Default.Home,
        isSelected = currentTab == RailOneNavTab.HOME,
        onClick = { onTabSelected(RailOneNavTab.HOME) },
        testTag = "nav_home",
        modifier = Modifier.weight(1f)
      )

      NavTabButton(
        title = "My Bookings",
        icon = Icons.Rounded.ConfirmationNumber,
        isSelected = currentTab == RailOneNavTab.BOOKINGS,
        onClick = { onTabSelected(RailOneNavTab.BOOKINGS) },
        testTag = "nav_my_bookings",
        modifier = Modifier.weight(1.2f)
      )

      NavTabButton(
        title = "You",
        icon = Icons.Default.Person,
        isSelected = currentTab == RailOneNavTab.YOU,
        onClick = { onTabSelected(RailOneNavTab.YOU) },
        testTag = "nav_you",
        modifier = Modifier.weight(1f)
      )

      NavTabButton(
        title = "Menu",
        icon = Icons.Default.Menu,
        isSelected = currentTab == RailOneNavTab.MENU,
        onClick = { onTabSelected(RailOneNavTab.MENU) },
        testTag = "nav_menu",
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
private fun NavTabButton(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxHeight()
      .clickable(onClick = onClick)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.72f),
        modifier = Modifier.size(24.dp)
      )

      Text(
        text = title,
        fontSize = 11.5.sp,
        fontStyle = if (isSelected) FontStyle.Italic else FontStyle.Normal,
        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f)
      )
    }
  }
}
