package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PassengerInfo
import com.example.ui.theme.RailBluePrimary
import com.example.ui.theme.RailCardBorder
import com.example.ui.theme.RailPillBorder
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import kotlinx.coroutines.launch

@Composable
fun PassengerSection(
  passengers: List<PassengerInfo>,
  onRefreshStatus: () -> Unit,
  onBookConnectingUts: () -> Unit,
  onEditPassenger: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val rotation = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp)
  ) {
    // Section Header: Passenger Details + Refresh
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Passenger Details",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )

      IconButton(
        onClick = {
          scope.launch {
            rotation.animateTo(
              targetValue = rotation.value + 360f,
              animationSpec = tween(durationMillis = 600)
            )
          }
          onRefreshStatus()
        },
        modifier = Modifier
          .size(36.dp)
          .testTag("refresh_status_button")
      ) {
        Icon(
          imageVector = Icons.Outlined.Refresh,
          contentDescription = "Refresh Passenger Status",
          tint = Color(0xFF64748B),
          modifier = Modifier
            .size(22.dp)
            .rotate(rotation.value)
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Passenger List
    passengers.forEach { passenger ->
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, RailCardBorder),
        shadowElevation = 1.5.dp,
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onEditPassenger)
          .testTag("passenger_card_${passenger.id}")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          // Row 1: Passenger Name and Gender | Age
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = passenger.name,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Text(
              text = "${passenger.gender} | ${passenger.age}",
              fontSize = 13.5.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Row 2: Booking Status
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Booking Status",
              fontSize = 13.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Normal
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(StatusGreenBg)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = passenger.bookingStatus,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = StatusGreen
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Row 3: Current Status
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Current Status",
              fontSize = 13.sp,
              color = Color(0xFF64748B),
              fontWeight = FontWeight.Normal
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(StatusGreenBg)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = passenger.currentStatus,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = StatusGreen
              )
            }
          }

          // Optional Coach/Berth preview if available
          if (passenger.coach.isNotBlank() && passenger.berthNumber > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Coach / Berth",
                fontSize = 12.5.sp,
                color = Color(0xFF94A3B8)
              )
              Text(
                text = "Coach ${passenger.coach}, Berth ${passenger.berthNumber} (${passenger.berthType})",
                fontSize = 12.5.sp,
                color = Color(0xFF334155),
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(10.dp))
    }

    Spacer(modifier = Modifier.height(4.dp))

    // Button: Book Connecting UTS Journey
    Button(
      onClick = onBookConnectingUts,
      shape = RoundedCornerShape(24.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = RailBluePrimary
      ),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("book_connecting_uts_button")
    ) {
      Text(
        text = "Book Connecting UTS Journey",
        color = Color.White,
        fontSize = 14.5.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
