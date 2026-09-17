package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TicketInfo
import com.example.ui.components.PassengerSection
import com.example.ui.components.RailOneDarkBlue
import com.example.ui.components.TicketCard
import com.example.ui.components.TicketDetailsExtras
import com.example.ui.components.UtsTicketCard
import com.example.ui.theme.RailBackground
import com.example.ui.theme.RailBluePrimary
import kotlinx.coroutines.launch

val MyBookingsTopBlue = Color(0xFF0D6EFD)
val OrangeUpcoming = Color(0xFFD97706)
val SoftBookingsBg = Color(0xFFF4F7FB)

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
  onLockApp: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  var selectedSubTab by remember { mutableIntStateOf(0) }
  val subTabs = listOf("Upcoming", "Completed", "Cancelled", "All")

  var menuExpanded by remember { mutableStateOf(false) }
  var showFullApExpressCard by remember { mutableStateOf(false) }
  var showingUtsBookingDetails by remember { mutableStateOf(false) }

  val scope = rememberCoroutineScope()
  val rotation = remember { Animatable(0f) }

  if (showingUtsBookingDetails) {
    UtsBookingDetailsScreen(
      ticket = ticket,
      onBackClick = { showingUtsBookingDetails = false },
      modifier = modifier
    )
    return
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(SoftBookingsBg)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // 1. TOP BAR matching screenshot
      Surface(
        color = MyBookingsTopBlue,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // Circle Back Button (←)
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .border(1.2.dp, Color.White, CircleShape)
              .clickable(onClick = onBackClick)
              .testTag("btn_bookings_back"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }

          // Title: "My Bookings"
          Text(
            text = "My Bookings",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp)
          )

          Spacer(modifier = Modifier.weight(1f))

          // Right Icons: Sort & 3-Dots Menu (containing Edit Ticket)
          Row(verticalAlignment = Alignment.CenterVertically) {
            // Sort Icon (↓≡)
            IconButton(
              onClick = {
                scope.launch {
                  onRefreshPassengerStatus()
                }
              },
              modifier = Modifier.testTag("btn_bookings_sort")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort Bookings",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }

            // 3-Dots Menu Button (⋮)
            Box {
              IconButton(
                onClick = { menuExpanded = true },
                modifier = Modifier.testTag("btn_bookings_menu")
              ) {
                Icon(
                  imageVector = Icons.Default.MoreVert,
                  contentDescription = "More Options",
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }

              // Dropdown Menu with "Edit Ticket" as requested
              DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier
                  .background(Color.White)
                  .testTag("bookings_dropdown_menu")
              ) {
                DropdownMenuItem(
                  text = {
                    Text(
                      text = "View Booking Details (UTS)",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold,
                      color = MyBookingsTopBlue
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Outlined.ConfirmationNumber,
                      contentDescription = null,
                      tint = MyBookingsTopBlue,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    showingUtsBookingDetails = true
                  },
                  modifier = Modifier.testTag("menu_item_view_uts_details")
                )

                DropdownMenuItem(
                  text = {
                    Text(
                      text = "Edit Ticket Details",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Outlined.Edit,
                      contentDescription = null,
                      tint = MyBookingsTopBlue,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    onEditTicket()
                  },
                  modifier = Modifier.testTag("menu_item_edit_ticket")
                )

                DropdownMenuItem(
                  text = {
                    Text(
                      text = if (showFullApExpressCard) "Switch to UTS Ticket View" else "Switch to IRCTC Express View",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Medium,
                      color = Color(0xFF0F172A)
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Outlined.SwapHoriz,
                      contentDescription = null,
                      tint = MyBookingsTopBlue,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    showFullApExpressCard = !showFullApExpressCard
                  }
                )

                DropdownMenuItem(
                  text = {
                    Text(
                      text = "Download e-Ticket (PDF)",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Medium,
                      color = Color(0xFF0F172A)
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Outlined.PictureAsPdf,
                      contentDescription = null,
                      tint = MyBookingsTopBlue,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    onPdfClick()
                  }
                )

                DropdownMenuItem(
                  text = {
                    Text(
                      text = "Send to Email",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Medium,
                      color = Color(0xFF0F172A)
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Outlined.Email,
                      contentDescription = null,
                      tint = MyBookingsTopBlue,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    onEmailClick()
                  }
                )

                DropdownMenuItem(
                  text = {
                    Text(
                      text = "Share Ticket",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Medium,
                      color = Color(0xFF0F172A)
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Outlined.Share,
                      contentDescription = null,
                      tint = MyBookingsTopBlue,
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    onShareClick()
                  }
                )

                DropdownMenuItem(
                  text = {
                    Text(
                      text = "Lock App (Phone Security)",
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFFB91C1C)
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Default.Lock,
                      contentDescription = null,
                      tint = Color(0xFFDC2626),
                      modifier = Modifier.size(20.dp)
                    )
                  },
                  onClick = {
                    menuExpanded = false
                    onLockApp()
                  },
                  modifier = Modifier.testTag("menu_item_lock_app")
                )
              }
            }
          }
        }
      }

      // 2. SUBHEADER: "Upcoming (1)" in Orange with Refresh Button on Right
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
          Text(
            text = when (selectedSubTab) {
              0 -> "Upcoming (1)"
              1 -> "Completed (0)"
              2 -> "Cancelled (0)"
              else -> "All Bookings (1)"
            },
            fontSize = 15.5.sp,
            fontWeight = FontWeight.Bold,
            color = OrangeUpcoming,
            textAlign = TextAlign.Center
          )
        }

        // Refresh icon on far right matching screenshot
        IconButton(
          onClick = {
            scope.launch {
              rotation.animateTo(
                targetValue = rotation.value + 360f,
                animationSpec = tween(durationMillis = 600)
              )
            }
            onRefreshPassengerStatus()
          },
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.Refresh,
            contentDescription = "Refresh Bookings",
            tint = Color(0xFF64748B),
            modifier = Modifier
              .size(22.dp)
              .rotate(rotation.value)
          )
        }
      }

      // 3. TICKET CONTENT AREA
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        when (selectedSubTab) {
          0, 3 -> {
            // UPCOMING / ALL BOOKINGS
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              // Exact UTS Ticket Card from Screenshot
              UtsTicketCard(
                ticket = ticket,
                onBookAgain = onReturnJourney,
                onViewDetails = {
                  showingUtsBookingDetails = true
                },
                onViewQr = onViewQr
              )

              // If user toggled or requested to see the rich IRCTC details
              if (showFullApExpressCard) {
                Spacer(modifier = Modifier.height(14.dp))
                TicketCard(
                  ticket = ticket,
                  onCopyPnr = onCopyPnr,
                  onChangeBoarding = onChangeBoarding,
                  onReturnJourney = onReturnJourney,
                  onRefundRules = onRefundRules,
                  onEditTicket = onEditTicket
                )
                PassengerSection(
                  passengers = ticket.passengers,
                  onRefreshStatus = onRefreshPassengerStatus,
                  onBookConnectingUts = onBookConnectingUts,
                  onEditPassenger = onEditTicket
                )
                TicketDetailsExtras(
                  ticket = ticket,
                  onViewQr = onViewQr
                )
              }

              Spacer(modifier = Modifier.height(24.dp))
            }
          }

          1 -> {
            EmptyBookingsPlaceholder(
              title = "No Completed Journeys",
              subtitle = "Your completed trips in the last 6 months will appear here."
            )
          }

          else -> {
            EmptyBookingsPlaceholder(
              title = "No Cancelled Tickets",
              subtitle = "You have not cancelled any e-tickets or UTS season passes."
            )
          }
        }
      }

      // 4. SUB-TABS BAR AT BOTTOM matching screenshot: Upcoming | Completed | Cancelled | All
      Surface(
        color = Color(0xFFEAF2FC),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .navigationBarsPadding(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          subTabs.forEachIndexed { index, title ->
            val isSelected = selectedSubTab == index
            SubTabItem(
              title = title,
              icon = Icons.Outlined.ConfirmationNumber,
              isSelected = isSelected,
              onClick = { selectedSubTab = index },
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SubTabItem(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isSelected) Color.White else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = if (isSelected) Color(0xFFEA580C) else Color(0xFF64748B),
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) Color(0xFFEA580C) else Color(0xFF64748B)
      )
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
