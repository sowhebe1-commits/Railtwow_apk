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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RailOneDarkBlue

@Composable
fun MenuScreen(
  onMenuItemClick: (String) -> Unit,
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
        text = "RailOne Menu & Services",
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = RailOneDarkBlue
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Rail Madad & Emergency Help Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onMenuItemClick("Call Rail Madad 139") }
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE65100),
            modifier = Modifier.size(46.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "Railway Helpline 139 (Rail Madad)",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFBF360C)
            )
            Text(
              text = "24x7 Assistance, Security, Medical & Grievances",
              fontSize = 11.5.sp,
              color = Color(0xFFE65100)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Section: Railway Utilities
      Text(
        text = "Railway Services",
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
          MenuOptionRow(
            title = "IRCTC e-Catering & Pantry",
            subtitle = "Order hot meals delivered right to your seat",
            icon = Icons.Default.Fastfood,
            onClick = { onMenuItemClick("IRCTC e-Catering") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          MenuOptionRow(
            title = "File TDR & Ticket Refund Rules",
            subtitle = "Cancel tickets, check refund deductions & guidelines",
            icon = Icons.Default.LocalAtm,
            onClick = { onMenuItemClick("File TDR & Refunds") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          MenuOptionRow(
            title = "Station Facilities & Wi-Fi",
            subtitle = "Check cloak room, waiting halls, battery cars",
            icon = Icons.Default.Train,
            onClick = { onMenuItemClick("Station Facilities") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          MenuOptionRow(
            title = "Rail Madad Grievance Registration",
            subtitle = "Register formal feedback or complain with Indian Railways",
            icon = Icons.Default.Handshake,
            onClick = { onMenuItemClick("Rail Madad") }
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Section: Legal & Info
      Text(
        text = "Information & Support",
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
          MenuOptionRow(
            title = "Help, FAQs & Tatkal Guide",
            subtitle = "Timings, quotas, rules & travel advisory",
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            onClick = { onMenuItemClick("FAQs & Tatkal Guide") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          MenuOptionRow(
            title = "Terms of Service & Privacy Policy",
            subtitle = "CRIS & Indian Railways official passenger guidelines",
            icon = Icons.Default.Policy,
            onClick = { onMenuItemClick("Policies") }
          )
          HorizontalDivider(color = Color(0xFFF0F4F8))
          MenuOptionRow(
            title = "About RailOne App",
            subtitle = "Version 1.0 (Official CRIS IRCTC passenger suite)",
            icon = Icons.Default.Info,
            onClick = { onMenuItemClick("About RailOne") }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun MenuOptionRow(
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
