package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.DirectionsTransit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TicketInfo
import com.example.ui.theme.RailBluePrimary
import com.example.ui.theme.RailCardBorder
import com.example.ui.theme.RailPillBorder
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg

@Composable
fun TicketDetailsExtras(
  ticket: TicketInfo,
  onViewQr: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isFareExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp)
  ) {
    // 1. Digital QR & Live Status Action Bar
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      border = BorderStroke(1.dp, RailCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .background(Color(0xFFEBF1FD), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.QrCode,
              contentDescription = null,
              tint = RailBluePrimary,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Digital e-Ticket QR Code",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Text(
              text = "Scan during TTE ticket inspection",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
          }
        }

        Button(
          onClick = onViewQr,
          shape = RoundedCornerShape(18.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
          modifier = Modifier
            .height(34.dp)
            .testTag("view_qr_code_button")
        ) {
          Text("Show QR", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 2. Coach Position & Platform Indicator
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      border = BorderStroke(1.dp, RailCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Outlined.DirectionsTransit,
              contentDescription = null,
              tint = RailBluePrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Coach Position & Platforms",
              fontSize = 14.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
          }

          Text(
            text = "Coach S3 (6th)",
            fontSize = 12.5.sp,
            color = RailBluePrimary,
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Platform summary
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "${ticket.fromCode}: ${ticket.fromPlatform}",
            fontSize = 12.sp,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "${ticket.toCode}: ${ticket.toPlatform}",
            fontSize = 12.sp,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Train Coach Sequence
        val coaches = listOf("Loco", "SLR", "GS", "S1", "S2", "S3*", "S4", "S5", "S6", "B1", "B2", "A1")
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          coaches.forEach { c ->
            val isMyCoach = c == "S3*"
            Box(
              modifier = Modifier
                .background(
                  color = if (isMyCoach) RailBluePrimary else Color(0xFFF1F5F9),
                  shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Text(
                text = if (isMyCoach) "S3 (You)" else c,
                fontSize = 11.5.sp,
                fontWeight = if (isMyCoach) FontWeight.Bold else FontWeight.Medium,
                color = if (isMyCoach) Color.White else Color(0xFF475569)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 3. Fare Summary Card (Expandable)
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      border = BorderStroke(1.dp, RailCardBorder),
      modifier = Modifier
        .fillMaxWidth()
        .clickable { isFareExpanded = !isFareExpanded }
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Outlined.CurrencyRupee,
              contentDescription = null,
              tint = RailBluePrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Fare Breakdown",
              fontSize = 14.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "₹${ticket.fare.totalAmount}",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Icon(
              imageVector = if (isFareExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
              contentDescription = null,
              tint = Color(0xFF64748B),
              modifier = Modifier.size(20.dp)
            )
          }
        }

        AnimatedVisibility(visible = isFareExpanded) {
          Column(modifier = Modifier.padding(top = 10.dp)) {
            HorizontalDivider(color = RailCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            FareRow(label = "Ticket Base Fare", amount = "₹${ticket.fare.baseFare}")
            FareRow(label = "Tatkal Quota Charges", amount = "₹${ticket.fare.tatkalCharge}")
            FareRow(label = "IRCTC Convenience Fee", amount = "₹${ticket.fare.irctcConvenienceFee}")

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = RailCardBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Total Amount Paid",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
              Text(
                text = "₹${ticket.fare.totalAmount}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RailBluePrimary
              )
            }
            Text(
              text = "Payment Mode: ${ticket.fare.paymentMethod}",
              fontSize = 11.5.sp,
              color = Color(0xFF64748B),
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun FareRow(label: String, amount: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, fontSize = 12.5.sp, color = Color(0xFF475569))
    Text(text = amount, fontSize = 12.5.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
  }
}
