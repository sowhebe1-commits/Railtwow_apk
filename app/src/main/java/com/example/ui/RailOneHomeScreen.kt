package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DoYouKnowSection
import com.example.ui.components.JourneyPlannerSection
import com.example.ui.components.MoreOfferingsSection
import com.example.ui.components.OfferingItem
import com.example.ui.components.RailOneHomeTopBar
import com.example.ui.components.TriviaItem

@Composable
fun RailOneHomeScreen(
  userName: String = "Roshan",
  onNavigateToBookings: () -> Unit,
  onOfferingClick: (OfferingItem) -> Unit,
  onTriviaClick: (TriviaItem) -> Unit,
  onLanguageClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
      // 1. Top Bar with A/अ, RailOne logo, and Bell (3)
      RailOneHomeTopBar(
        onLanguageClick = onLanguageClick,
        onNotificationsClick = onNotificationsClick
      )

      // 2. Greeting: "Hi, Roshan!" exactly matching screenshot
      Text(
        text = "Hi, $userName!",
        fontSize = 20.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF002244),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
      )

      // 3. Journey Planner (Reserved, Unreserved, Platform)
      JourneyPlannerSection(
        onReservedClick = onNavigateToBookings,
        onUnreservedClick = {
          onOfferingClick(
            OfferingItem(
              id = "unreserved_journey",
              title = "Unreserved\nJourney",
              icon = androidx.compose.material.icons.Icons.Default.Train,
              containerColor = Color(0xFFE3F7E8),
              iconColor = Color(0xFF28A745)
            )
          )
        },
        onPlatformClick = {
          onOfferingClick(
            OfferingItem(
              id = "platform_ticket",
              title = "Platform\nTicket",
              icon = androidx.compose.material.icons.Icons.Default.Train,
              containerColor = Color(0xFFFEF5DE),
              iconColor = Color(0xFFF59E0B)
            )
          )
        }
      )

      Spacer(modifier = Modifier.height(10.dp))

      // 4. More Offerings (8 grid buttons)
      MoreOfferingsSection(
        onOfferingClick = onOfferingClick
      )

      Spacer(modifier = Modifier.height(6.dp))

      // 5. Do You know? (Horizontal scrolling cards)
      DoYouKnowSection(
        onTriviaClick = onTriviaClick
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
