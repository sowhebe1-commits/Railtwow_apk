package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RailOneDarkBlue

@Composable
fun YouProfileScreen(
  userName: String = "Roshan",
  email: String = "roshan93721p@gmail.com",
  phone: String = "+91 93721 84022",
  irctcUserId: String = "ROSHAN_937",
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF3F6FA))
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      Text(
        text = "Profile & Account",
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = RailOneDarkBlue
      )

      Spacer(modifier = Modifier.height(16.dp))

      // User Profile Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = CircleShape,
              color = Color(0xFF0D6EFD),
              modifier = Modifier.size(60.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = userName.take(1).uppercase(),
                  fontSize = 28.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
              Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RailOneDarkBlue
              )
              Text(
                text = "IRCTC ID: $irctcUserId",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0D6EFD)
              )
              Text(
                text = email,
                fontSize = 12.sp,
                color = Color(0xFF607D8B)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = Color(0xFFECEFF1))
          Spacer(modifier = Modifier.height(14.dp))

          // R-Wallet balance row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = Color(0xFF0D6EFD),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "RailOne R-Wallet",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = RailOneDarkBlue
              )
            }
            Text(
              text = "₹ 1,250.00",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF2E7D32)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Section: Personal & Bookings
      Text(
        text = "Preferences & Management",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF546E7A),
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
      )

      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          ProfileOptionRow(
            title = "Saved Master Passenger List",
            subtitle = "D VARDHAN (20, M), Roshan (24, M)",
            icon = Icons.Rounded.Groups,
            onClick = { onItemClick("Saved Passengers") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          ProfileOptionRow(
            title = "Transaction History & GST Invoices",
            subtitle = "View past e-ticket tax invoices & receipts",
            icon = Icons.Rounded.History,
            onClick = { onItemClick("Transaction History") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          ProfileOptionRow(
            title = "Linked Mobile & Security",
            subtitle = "$phone (Verified)",
            icon = Icons.Default.Lock,
            onClick = { onItemClick("Security Settings") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          ProfileOptionRow(
            title = "App Preferences & Language",
            subtitle = "English / हिन्दी / Notifications enabled",
            icon = Icons.Default.Settings,
            onClick = { onItemClick("Preferences") }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun ProfileOptionRow(
  title: String,
  subtitle: String,
  icon: ImageVector,
  onClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = Color(0xFFEFF5FE),
      modifier = Modifier.size(40.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Color(0xFF0D6EFD),
          modifier = Modifier.size(22.dp)
        )
      }
    }

    Spacer(modifier = Modifier.width(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = RailOneDarkBlue
      )
      Text(
        text = subtitle,
        fontSize = 11.5.sp,
        color = Color(0xFF78909C)
      )
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = Color(0xFFB0BEC5),
      modifier = Modifier.size(16.dp)
    )
  }
}
