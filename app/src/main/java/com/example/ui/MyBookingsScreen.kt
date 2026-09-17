package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TicketInfo
import com.example.ui.components.PassengerSection
import com.example.ui.components.RailOneDarkBlue
import com.example.ui.components.RailOneHeader
import com.example.ui.components.TicketCard
import com.example.ui.components.TicketDetailsExtras
import com.example.ui.theme.RailBackground
import com.example.ui.theme.RailBluePrimary

@Composable
fun MyBookingsScreen(
  ticket: TicketInfo,
  onCopyPnr: (String) -> Unit,
  onBackClick: () -> Unit,
  onPdfClick: () -> Unit,
  onEmailClick: () -> Unit,
  onShareClick: () -> Unit,
  onEditTicket: () -> Unit,
  onChangeBoarding: () -> Unit,
  onReturnJourney: () -> Unit,
  onRefundRules: () -> Unit,
  onRefreshPassengerStatus: () -> Unit,
  onBookConnectingUts: () -> Unit,
  onViewQr: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val bookingTabs = listOf("Upcoming (1)", "Completed", "Cancelled")

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(RailBackground)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Sub-Tabs Header
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = RailBluePrimary,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = RailBluePrimary,
            height = 3.dp
          )
        }
      ) {
        bookingTabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                color = if (selectedTab == index) RailBluePrimary else Color(0xFF607D8B)
              )
            },
            modifier = Modifier.testTag("booking_tab_$index")
          )
        }
      }

      when (selectedTab) {
        0 -> {
          // UPCOMING: Shows the exact booked ticket from the user's requirement
          LazyColumn(
            modifier = Modifier.fillMaxSize()
          ) {
            // 1. Top Header with Back, PNR greeting, PDF, Email, Share, and Edit actions
            item {
              RailOneHeader(
                transactionId = ticket.transactionId,
                passengerGreeting = ticket.passengerGreeting,
                onBackClick = onBackClick,
                onPdfClick = onPdfClick,
                onEmailClick = onEmailClick,
                onShareClick = onShareClick,
                onEditClick = onEditTicket
              )
            }

            // 2. Main Ticket Card matching the ticket requirement
            item {
              TicketCard(
                ticket = ticket,
                onCopyPnr = onCopyPnr,
                onChangeBoarding = onChangeBoarding,
                onReturnJourney = onReturnJourney,
                onRefundRules = onRefundRules,
                onEditTicket = onEditTicket
              )
            }

            // 3. Passenger Details Section
            item {
              PassengerSection(
                passengers = ticket.passengers,
                onRefreshStatus = onRefreshPassengerStatus,
                onBookConnectingUts = onBookConnectingUts,
                onEditPassenger = onEditTicket
              )
            }

            // 4. Ticket Details Extras (QR code, Coach position, Fare breakdown)
            item {
              TicketDetailsExtras(
                ticket = ticket,
                onViewQr = onViewQr
              )
            }

            item {
              Spacer(modifier = Modifier.height(30.dp))
            }
          }
        }
        1 -> {
          // COMPLETED BOOKINGS
          EmptyBookingsPlaceholder(
            title = "No recent completed journeys",
            subtitle = "Your completed trips in the last 6 months will appear here."
          )
        }
        else -> {
          // CANCELLED BOOKINGS
          EmptyBookingsPlaceholder(
            title = "No cancelled tickets",
            subtitle = "You have not cancelled any e-tickets or UTS bookings."
          )
        }
      }
    }
  }
}

@Composable
private fun EmptyBookingsPlaceholder(
  title: String,
  subtitle: String,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = title,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = RailOneDarkBlue,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = subtitle,
          fontSize = 13.sp,
          color = Color(0xFF78909C),
          textAlign = TextAlign.Center
        )
      }
    }
  }
}
