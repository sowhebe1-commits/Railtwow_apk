package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TicketInfo
import kotlinx.coroutines.delay

val UtsBookingHeaderBlue = Color(0xFF0066FF)
val UtsTicketMintGreen = Color(0xFFB9E5B3)
val UtsTimerOrange = Color(0xFFFF3700)
val UtsGoldDate = Color(0xFFFFB300)
val UtsGoldLabel = Color(0xFFE5A93C)
val UtsNoteBg = Color(0xFFFDE8E8)
val UtsNoteText = Color(0xFFD32F2F)

@Composable
fun UtsBookingDetailsScreen(
  ticket: TicketInfo,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  // Mobile & Passenger info (matching screenshot, with defaults)
  var mobileNumber by remember { mutableStateOf("7983961490") }
  var passengerGreeting by remember { mutableStateOf(if (ticket.passengerGreeting.isNotEmpty() && ticket.passengerGreeting != "ROSHAN") ticket.passengerGreeting else "MEERA DEVI") }
  var ticketId by remember { mutableStateOf("XH6ZE4C002") }
  var bookingCode by remember { mutableStateOf("R25759") }
  var bookingDateTime by remember { mutableStateOf("09 Jul 2025, 20:01") }
  var bookedOnDate by remember { mutableStateOf("09/07/2025 20:01") }
  var validTillDate by remember { mutableStateOf("09/07/2025 23:01") }
  var fareAmount by remember { mutableStateOf("₹20.00") }
  var distanceText by remember { mutableStateOf("—17 km—") }
  var originStation by remember { mutableStateOf(ticket.fromStation.ifBlank { "MANKHURD" }) }
  var destStation by remember { mutableStateOf(ticket.toStation.ifBlank { "NERUL" }) }

  // 5:00 countdown timer
  var secondsRemaining by remember { mutableIntStateOf(300) } // 5 mins = 300 seconds
  var showEditDialog by remember { mutableStateOf(false) }
  var menuExpanded by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(1000L)
      if (secondsRemaining > 0) {
        secondsRemaining--
      } else {
        secondsRemaining = 300 // restart dynamic preview loop
      }
    }
  }

  val minutes = secondsRemaining / 60
  val seconds = secondsRemaining % 60
  val formattedTimer = String.format("%02d:%02d", minutes, seconds)

  // Edit Ticket Dialog
  if (showEditDialog) {
    var editMobile by remember { mutableStateOf(mobileNumber) }
    var editName by remember { mutableStateOf(passengerGreeting) }
    var editTicketId by remember { mutableStateOf(ticketId) }
    var editFare by remember { mutableStateOf(fareAmount) }
    var editBookingCode by remember { mutableStateOf(bookingCode) }
    var editFrom by remember { mutableStateOf(originStation) }
    var editTo by remember { mutableStateOf(destStation) }
    var editDistance by remember { mutableStateOf(distanceText) }
    var editBookingDateTime by remember { mutableStateOf(bookingDateTime) }
    var editBookedOnDate by remember { mutableStateOf(bookedOnDate) }
    var editValidTillDate by remember { mutableStateOf(validTillDate) }

    AlertDialog(
      onDismissRequest = { showEditDialog = false },
      title = {
        Text("Edit UTS Ticket Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("Passenger Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editMobile,
            onValueChange = { editMobile = it },
            label = { Text("Mobile Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = editFrom,
              onValueChange = { editFrom = it },
              label = { Text("From") },
              singleLine = true,
              modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
              value = editTo,
              onValueChange = { editTo = it },
              label = { Text("To") },
              singleLine = true,
              modifier = Modifier.weight(1f)
            )
          }
          OutlinedTextField(
            value = editDistance,
            onValueChange = { editDistance = it },
            label = { Text("Distance (e.g. —17 km—)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editTicketId,
            onValueChange = { editTicketId = it },
            label = { Text("Ticket Code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editFare,
            onValueChange = { editFare = it },
            label = { Text("Fare Amount") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editBookingCode,
            onValueChange = { editBookingCode = it },
            label = { Text("Booking Reference (e.g. R25759)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editBookingDateTime,
            onValueChange = { editBookingDateTime = it },
            label = { Text("Booking Date & Time (Header)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editBookedOnDate,
            onValueChange = { editBookedOnDate = it },
            label = { Text("Booked on (Ticket)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editValidTillDate,
            onValueChange = { editValidTillDate = it },
            label = { Text("Valid Till") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            mobileNumber = editMobile
            passengerGreeting = editName
            ticketId = editTicketId
            fareAmount = editFare
            bookingCode = editBookingCode
            originStation = editFrom
            destStation = editTo
            distanceText = editDistance
            bookingDateTime = editBookingDateTime
            bookedOnDate = editBookedOnDate
            validTillDate = editValidTillDate
            showEditDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = UtsBookingHeaderBlue)
        ) {
          Text("Save Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFEEF2F6))
  ) {
    // 1. TOP APP BAR (Blue matching screenshot)
    Surface(
      color = UtsBookingHeaderBlue,
      shadowElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Circular Back Button (←)
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .border(1.2.dp, Color.White, CircleShape)
            .clickable(onClick = onBackClick)
            .testTag("btn_uts_details_back"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Title and Mobile Number
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Booking Details",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(1.dp))
          Text(
            text = "Mobile: $mobileNumber",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFD6E4FF)
          )
        }

        // 3-dot Overflow Menu
        Box {
          IconButton(
            onClick = { menuExpanded = true },
            modifier = Modifier
              .size(38.dp)
              .testTag("btn_uts_details_3dot")
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "More Options",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(Color.White)
          ) {
            DropdownMenuItem(
              text = {
                Text(
                  text = "Edit Ticket Details",
                  fontSize = 14.5.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF1E293B)
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Outlined.Edit,
                  contentDescription = null,
                  tint = UtsBookingHeaderBlue,
                  modifier = Modifier.size(20.dp)
                )
              },
              onClick = {
                menuExpanded = false
                showEditDialog = true
              },
              modifier = Modifier.testTag("menu_item_edit_ticket_3dot")
            )

            DropdownMenuItem(
              text = {
                Text(
                  text = "Reset Timer (05:00)",
                  fontSize = 14.5.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF1E293B)
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Outlined.Refresh,
                  contentDescription = null,
                  tint = UtsBookingHeaderBlue,
                  modifier = Modifier.size(20.dp)
                )
              },
              onClick = {
                menuExpanded = false
                secondsRemaining = 300
              },
              modifier = Modifier.testTag("menu_item_reset_timer_3dot")
            )
          }
        }
      }
    }

    // 2. THANK YOU BANNER STRIP
    Surface(
      color = Color.White,
      shadowElevation = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "Thank You $passengerGreeting, Happy Journey !",
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF475569),
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp, horizontal = 16.dp)
      )
    }

    // 3. SCROLLABLE TICKET CONTAINER
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 14.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // MAIN TICKET CARD
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("uts_journey_ticket_card")
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // A. Top Mint Green Accent Band
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(22.dp)
              .background(UtsTicketMintGreen)
          )

          // B. DYNAMIC PREVIEW BLACK SECTION
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
              .background(Color(0xFF0D0D0D))
          ) {
            // Background Watermark Diamond Pattern
            Canvas(modifier = Modifier.fillMaxSize()) {
              val step = 42f
              val cols = (size.width / step).toInt() + 2
              val rows = (size.height / step).toInt() + 2

              // Draw subtle diamond lattice
              for (i in 0..cols) {
                for (j in 0..rows) {
                  val cx = i * step
                  val cy = j * step
                  val diamond = Path().apply {
                    moveTo(cx, cy - step / 2)
                    lineTo(cx + step / 2, cy)
                    lineTo(cx, cy + step / 2)
                    lineTo(cx - step / 2, cy)
                    close()
                  }
                  drawPath(
                    path = diamond,
                    color = Color(0xFF181818),
                    style = Stroke(width = 1f)
                  )
                }
              }
            }

            // Watermark Text "CRIS tech knowledge" in subtle grey
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "CRIS tech knowledge",
                color = Color(0x35777777),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.rotate(-16f)
              )
            }

            // Left Edge: INDIAN RAILWAYS with vertical dashed line
            Row(
              modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "INDIAN RAILWAYS",
                color = Color(0xFFB0B0B0),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.8.sp,
                modifier = Modifier
                  .rotate(-90f)
                  .padding(bottom = 2.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              // Dashed vertical separator
              Canvas(
                modifier = Modifier
                  .width(1.dp)
                  .fillMaxHeight()
                  .padding(vertical = 10.dp)
              ) {
                drawLine(
                  color = Color(0xFF666666),
                  start = Offset(0f, 0f),
                  end = Offset(0f, size.height),
                  pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
                  strokeWidth = 1.5f
                )
              }
            }

            // Right Edge: भारतीय रेल with vertical dashed line
            Row(
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Dashed vertical separator
              Canvas(
                modifier = Modifier
                  .width(1.dp)
                  .fillMaxHeight()
                  .padding(vertical = 10.dp)
              ) {
                drawLine(
                  color = Color(0xFF666666),
                  start = Offset(0f, 0f),
                  end = Offset(0f, size.height),
                  pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
                  strokeWidth = 1.5f
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "भारतीय रेल",
                color = Color(0xFFB0B0B0),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.8.sp,
                modifier = Modifier
                  .rotate(90f)
                  .padding(top = 2.dp)
              )
            }

            // Center Countdown and Booking Meta
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 46.dp, vertical = 12.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Text(
                text = "Dynamic preview will close in",
                color = Color.White,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(4.dp))

              // 05:00 Big Timer in Bright Red-Orange
              Text(
                text = formattedTimer,
                color = UtsTimerOrange,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = "Ticket Booking Date & Time",
                color = UtsGoldLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = bookingDateTime,
                color = UtsGoldDate,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = bookingCode,
                color = Color(0xFF888888),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
              )
            }
          }

          // C. WHITE TICKET DETAILS SECTION
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.White)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
              // 1. Header: "Journey Ticket" and "XH6ZE4C002"
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Journey Ticket",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E293B)
                )

                Text(
                  text = ticketId,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp,
                  color = Color(0xFF1E293B)
                )
              }

              Spacer(modifier = Modifier.height(16.dp))

              // 2. ROUTE LINE: MANKHURD  —17 km—  NERUL
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Origin Station
                Text(
                  text = originStation,
                  fontSize = 17.5.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF0F172A),
                  modifier = Modifier.weight(1f)
                )

                // Distance indicator
                Text(
                  text = distanceText,
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF64748B),
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(horizontal = 6.dp)
                )

                // Destination Station
                Text(
                  text = destStation,
                  fontSize = 17.5.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF0F172A),
                  textAlign = TextAlign.End,
                  modifier = Modifier.weight(1f)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              // 3. VIA and PASSENGER ROW
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Column {
                  Text(
                    text = "Via",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Normal
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = "-",
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold
                  )
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "Passenger",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = "1 Adult, 0 Child",
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // 4. BOOKED ON and VALID TILL ROW
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Column {
                  Text(
                    text = "Booked on",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Normal
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = bookedOnDate,
                    fontSize = 13.5.sp,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold
                  )
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "*Valid Till",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = validTillDate,
                    fontSize = 13.5.sp,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                  )
                }
              }

              Spacer(modifier = Modifier.height(18.dp))

              // 5. CLASS & FARE STRIP
              Text(
                text = "SECOND | ORDINARY | JOURNEY | $fareAmount",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Dashed Horizontal Divider
              Canvas(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(1.dp)
              ) {
                drawLine(
                  color = Color(0xFFCBD5E1),
                  start = Offset(0f, 0f),
                  end = Offset(size.width, 0f),
                  pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                  strokeWidth = 1.2f
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              // 6. TICKET DISCLAIMER / FOOTNOTE
              Text(
                text = "*Valid for start of journey within 3 hour or until departure of the first train.",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.fillMaxWidth()
              )
            }

            // Authentic UTS circular notches on left and right edge
            Box(
              modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-12).dp)
                .clip(CircleShape)
                .background(Color(0xFFEEF2F6))
            )

            Box(
              modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 12.dp)
                .clip(CircleShape)
                .background(Color(0xFFEEF2F6))
            )
          }

          // D. Bottom Mint Green Accent Band
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(18.dp)
              .background(UtsTicketMintGreen)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 4. BOTTOM NOTICE BOX (Pink container with red warning text matching screenshot)
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = UtsNoteBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("uts_ticket_notice_box")
      ) {
        Text(
          text = "Note: This ticket is non refundable. Ticket is stored locally on the device. Please do not change your handset or perform factory reset.",
          color = UtsNoteText,
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Medium,
          lineHeight = 18.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
      Spacer(modifier = Modifier.navigationBarsPadding())
    }
  }
}
