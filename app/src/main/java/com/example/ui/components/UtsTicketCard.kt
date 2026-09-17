package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.rounded.DirectionsSubway
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TicketInfo

val TicketBorderColor = Color(0xFFF6C894)
val TicketBgColor = Color.White
val UtsPurpleBg = Color(0xFFF3E8FF)
val UtsPurpleText = Color(0xFF9333EA)
val DarkCharcoal = Color(0xFF0F172A)
val MutedSlate = Color(0xFF94A3B8)
val ActionSlate = Color(0xFF475569)
val ActionBlue = Color(0xFF0284C7)

@Composable
fun UtsTicketCard(
  ticket: TicketInfo,
  onBookAgain: () -> Unit,
  onViewDetails: () -> Unit,
  onViewQr: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Main Ticket Card with border and custom side notches
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = TicketBgColor,
      border = androidx.compose.foundation.BorderStroke(1.2.dp, TicketBorderColor),
      shadowElevation = 2.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("uts_ticket_card")
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // TOP SECTION
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp)
        ) {
          // Row 1: "Unreserved" badge on left, "UTS: X0F7EE90D1" on right
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = UtsPurpleBg,
              modifier = Modifier.testTag("badge_ticket_category")
            ) {
              Text(
                text = ticket.ticketCategory,
                color = UtsPurpleText,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "UTS: ",
                color = ActionSlate,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = ticket.utsNumber.ifEmpty { ticket.pnrNumber },
                color = DarkCharcoal,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.testTag("text_uts_number")
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Row 2: Ticket Type & Booking Date
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column {
              Text(
                text = "Ticket Type",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Normal,
                color = MutedSlate
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = ticket.ticketType,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                color = DarkCharcoal,
                modifier = Modifier.testTag("text_ticket_type")
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Booking Date",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Normal,
                color = MutedSlate
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = ticket.departureDate,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                color = DarkCharcoal,
                modifier = Modifier.testTag("text_booking_date")
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Row 3: MANKHURD  — 14 km —  NERUL
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = ticket.fromStation.uppercase(),
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold,
              color = DarkCharcoal,
              modifier = Modifier.weight(1f, fill = false).testTag("text_from_station")
            )

            Text(
              text = "— ${ticket.distanceKm} —",
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium,
              color = MutedSlate,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
              text = ticket.toStation.uppercase(),
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold,
              color = DarkCharcoal,
              textAlign = TextAlign.End,
              modifier = Modifier.weight(1f, fill = false).testTag("text_to_station")
            )
          }
        }

        // TICKET NOTCH & PERFORATION DIVIDER
        TicketPerforationDivider()

        // BOTTOM ACTION ROW: Book Again  |  View Details
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clickable(onClick = onBookAgain)
              .testTag("btn_book_again"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Book Again",
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
              color = ActionSlate
            )
          }

          Text(
            text = "|",
            color = Color(0xFFCBD5E1),
            fontSize = 16.sp,
            fontWeight = FontWeight.Light
          )

          Box(
            modifier = Modifier
              .weight(1f)
              .clickable(onClick = {
                isExpanded = !isExpanded
                onViewDetails()
              })
              .testTag("btn_view_details"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (isExpanded) "Hide Details" else "View Details",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = ActionBlue
            )
          }
        }

        // EXPANDABLE DETAILS DRAWER (Shown when View Details is tapped)
        AnimatedVisibility(
          visible = isExpanded,
          enter = fadeIn() + expandVertically(),
          exit = fadeOut() + shrinkVertically()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFFAFCFF))
              .padding(horizontal = 20.dp, vertical = 14.dp)
          ) {
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            // Passenger & Class Info
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Passenger",
                  fontSize = 11.5.sp,
                  color = MutedSlate
                )
                Text(
                  text = ticket.passengers.firstOrNull()?.name ?: "Roshan",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = DarkCharcoal
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Class",
                  fontSize = 11.5.sp,
                  color = MutedSlate
                )
                Text(
                  text = ticket.travelClass,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = DarkCharcoal
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Validity & Fare
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Validity / Status",
                  fontSize = 11.5.sp,
                  color = MutedSlate
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = ticket.liveStatus,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF16A34A)
                  )
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Total Fare",
                  fontSize = 11.5.sp,
                  color = MutedSlate
                )
                Text(
                  text = "₹ ${"%.2f".format(ticket.fare.totalAmount)}",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = DarkCharcoal
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // View QR Button
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFE0F2FE),
              modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onViewQr)
                .testTag("btn_view_qr_uts")
            ) {
              Row(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.QrCode,
                  contentDescription = "QR Code",
                  tint = ActionBlue,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Show TTE Paperless QR Code",
                  color = ActionBlue,
                  fontSize = 13.5.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}

// Custom Perforation line with semi-circle ticket cutouts on left and right borders
@Composable
fun TicketPerforationDivider(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(20.dp)
  ) {
    Canvas(modifier = Modifier.fillMaxWidth().height(20.dp)) {
      val w = size.width
      val h = size.height
      val notchRadius = 10.dp.toPx()
      val centerY = h / 2f

      // Left notch cutout (drawn with background color and border)
      val leftPath = Path().apply {
        arcTo(
          rect = Rect(
            left = -notchRadius,
            top = centerY - notchRadius,
            right = notchRadius,
            bottom = centerY + notchRadius
          ),
          startAngleDegrees = 270f,
          sweepAngleDegrees = 180f,
          forceMoveTo = false
        )
      }
      drawPath(leftPath, color = Color(0xFFF4F7FB))
      drawPath(
        leftPath,
        color = TicketBorderColor,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx())
      )

      // Right notch cutout
      val rightPath = Path().apply {
        arcTo(
          rect = Rect(
            left = w - notchRadius,
            top = centerY - notchRadius,
            right = w + notchRadius,
            bottom = centerY + notchRadius
          ),
          startAngleDegrees = 90f,
          sweepAngleDegrees = 180f,
          forceMoveTo = false
        )
      }
      drawPath(rightPath, color = Color(0xFFF4F7FB))
      drawPath(
        rightPath,
        color = TicketBorderColor,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx())
      )

      // Dashed line between left and right notches
      val startX = notchRadius + 4f
      val endX = w - notchRadius - 4f
      val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)

      drawLine(
        color = TicketBorderColor,
        start = Offset(startX, centerY),
        end = Offset(endX, centerY),
        strokeWidth = 1.2.dp.toPx(),
        pathEffect = dashPathEffect
      )
    }
  }
}
