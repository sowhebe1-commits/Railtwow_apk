package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TicketInfo
import com.example.ui.theme.RailBackground
import com.example.ui.theme.RailBluePrimary
import com.example.ui.theme.RailCardBorder
import com.example.ui.theme.RailPillBorder

@Composable
fun TicketCard(
  ticket: TicketInfo,
  onCopyPnr: (String) -> Unit,
  onChangeBoarding: () -> Unit,
  onReturnJourney: () -> Unit,
  onRefundRules: () -> Unit,
  onEditTicket: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp)
  ) {
    // Main White Card Container
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White,
      border = androidx.compose.foundation.BorderStroke(1.dp, RailCardBorder),
      shadowElevation = 2.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("ticket_card")
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // 1. Top Blue Accent Bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(RailBluePrimary)
        )

        // 2. Train and PNR Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onEditTicket)
          ) {
            Column {
              Text(
                text = ticket.trainNumber,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
              )
              Text(
                text = ticket.trainName,
                fontSize = 17.sp,
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = "Edit train details",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(15.dp)
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { onCopyPnr(ticket.pnrNumber) }
              .testTag("copy_pnr_button")
          ) {
            Text(
              text = "PNR: ",
              fontSize = 13.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Normal
            )
            Text(
              text = ticket.pnrNumber,
              fontSize = 14.sp,
              color = Color(0xFF0F172A),
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Outlined.ContentCopy,
              contentDescription = "Copy PNR",
              tint = Color(0xFF64748B),
              modifier = Modifier.size(16.dp)
            )
          }
        }

        // Thin Divider
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFF1F5F9))
        )

        // 3. Journey Timetable & Stations
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditTicket)
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          // Left: Departure
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = ticket.departureTime,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = ticket.departureDate,
                fontSize = 11.5.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Normal
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = ticket.fromStation,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Text(
              text = ticket.fromCode,
              fontSize = 12.5.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Medium
            )
          }

          // Center: Duration
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 6.dp)
          ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "— ${ticket.duration} —",
              fontSize = 12.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Normal
            )
          }

          // Right: Arrival
          Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)
          ) {
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = ticket.arrivalDate,
                fontSize = 11.5.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Normal
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = ticket.arrivalTime,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = ticket.toStation,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A),
              textAlign = TextAlign.End
            )
            Text(
              text = ticket.toCode,
              fontSize = 12.5.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Medium,
              textAlign = TextAlign.End
            )
          }
        }

        // 4. Change Boarding Station Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          OutlinedButton(
            onClick = onChangeBoarding,
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, RailPillBorder),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = Color.White,
              contentColor = RailBluePrimary
            ),
            modifier = Modifier
              .fillMaxWidth(0.72f)
              .height(38.dp)
              .testTag("change_boarding_button")
          ) {
            Text(
              text = "Change Boarding Station",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Perforation Tear Line with Left & Right Semicircle Cutouts
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
        ) {
          // Central dashed line
          Canvas(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.Center)
              .padding(horizontal = 14.dp)
          ) {
            drawLine(
              color = Color(0xFFCBD5E1),
              start = Offset(0f, size.height / 2),
              end = Offset(size.width, size.height / 2),
              strokeWidth = 2.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            )
          }

          // Left notch
          Box(
            modifier = Modifier
              .align(Alignment.CenterStart)
              .offset(x = (-12).dp)
              .size(24.dp)
              .clip(CircleShape)
              .background(RailBackground)
              .border(1.dp, RailCardBorder, CircleShape)
          )

          // Right notch
          Box(
            modifier = Modifier
              .align(Alignment.CenterEnd)
              .offset(x = 12.dp)
              .size(24.dp)
              .clip(CircleShape)
              .background(RailBackground)
              .border(1.dp, RailCardBorder, CircleShape)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 6. Booking Description Lines
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditTicket)
            .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
          Text(
            text = ticket.bookingMeta,
            fontSize = 13.5.sp,
            color = Color(0xFF0F172A),
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Booked on: ${ticket.bookedOn}",
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Normal
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 7. Action Buttons: Return Journey (Solid Blue) + Refund Rules (Outlined)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Return Journey Button (Solid Blue)
          androidx.compose.material3.Button(
            onClick = onReturnJourney,
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = RailBluePrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            modifier = Modifier
              .weight(1.1f)
              .height(42.dp)
              .testTag("return_journey_button")
          ) {
            Text(
              text = "Return Journey",
              color = Color.White,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          // Refund Rules Button (Outlined with RailBlue)
          OutlinedButton(
            onClick = onRefundRules,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, RailPillBorder),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = Color.White,
              contentColor = RailBluePrimary
            ),
            modifier = Modifier
              .weight(1f)
              .height(42.dp)
              .testTag("refund_rules_button")
          ) {
            Text(
              text = "Refund Rules",
              fontSize = 13.5.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}
