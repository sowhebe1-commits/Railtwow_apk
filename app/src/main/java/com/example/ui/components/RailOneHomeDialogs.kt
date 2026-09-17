package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LanguageSelectionDialog(
  currentLang: String,
  onSelectLanguage: (String) -> Unit,
  onDismiss: () -> Unit,
) {
  val languages = listOf(
    "English" to "English (Default)",
    "हिन्दी" to "Hindi",
    "తెలుగు" to "Telugu",
    "தமிழ்" to "Tamil",
    "मराठी" to "Marathi",
    "বাংলা" to "Bengali"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Select Preferred Language",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = RailOneDarkBlue
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        languages.forEach { (lang, desc) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectLanguage(lang) }
              .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = lang,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = RailOneDarkBlue
              )
              Text(
                text = desc,
                fontSize = 12.sp,
                color = Color(0xFF78909C)
              )
            }
            if (lang == currentLang || (currentLang == "English" && lang == "English")) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF0D6EFD),
                modifier = Modifier.size(20.dp)
              )
            }
          }
          HorizontalDivider(color = Color(0xFFF0F4F8))
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close", color = Color(0xFF0D6EFD), fontWeight = FontWeight.Bold)
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(20.dp)
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
  onDismiss: () -> Unit,
  onViewBooking: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val alerts = listOf(
    "Train 20806 (AP Express) on-time departure confirmed at 21:30 from Platform 1 (BZA)." to "15m ago",
    "Tatkal booking confirmation for PNR 4855528192 is ready in your bookings." to "2h ago",
    "Rail Madad prompt: Rate your recent journey experience." to "1d ago"
  )

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Notifications (3)",
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold,
          color = RailOneDarkBlue
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF546E7A))
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      alerts.forEach { (text, time) ->
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFFF3F6FA),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
              onDismiss()
              onViewBooking()
            }
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
          ) {
            Surface(
              shape = CircleShape,
              color = Color(0xFFE3F2FD),
              modifier = Modifier.size(36.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Notifications,
                  contentDescription = null,
                  tint = Color(0xFF0D6EFD),
                  modifier = Modifier.size(20.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF263238),
                lineHeight = 18.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = time,
                fontSize = 11.sp,
                color = Color(0xFF90A4AE)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}

@Composable
fun PnrStatusQuickDialog(
  currentPnr: String = "4855528192",
  onCheckPnr: (String) -> Unit,
  onViewActiveBooking: () -> Unit,
  onDismiss: () -> Unit,
) {
  var pnrInput by remember { mutableStateOf(currentPnr) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Check PNR Status",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = RailOneDarkBlue
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Enter 10-digit Indian Railways PNR number to check current live charting & confirmation status.",
          fontSize = 13.sp,
          color = Color(0xFF546E7A)
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
          value = pnrInput,
          onValueChange = { if (it.length <= 10) pnrInput = it },
          label = { Text("10-Digit PNR") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onCheckPnr(pnrInput)
          onViewActiveBooking()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
      ) {
        Text("View Ticket", color = Color.White, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFF546E7A))
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(20.dp)
  )
}

@Composable
fun OfferingDetailDialog(
  offering: OfferingItem,
  onDismiss: () -> Unit,
  onAction: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = offering.containerColor,
        modifier = Modifier.size(54.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = offering.icon,
            contentDescription = null,
            tint = offering.iconColor,
            modifier = Modifier.size(30.dp)
          )
        }
      }
    },
    title = {
      Text(
        text = offering.title.replace("\n", " "),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = RailOneDarkBlue
      )
    },
    text = {
      val message = when (offering.id) {
        "search_trains" -> "Search all Indian Railways trains, seat availability, tatkal timings, and fare comparisons between stations."
        "pnr_status" -> "Instant PNR status checker with live chart preparation status and coach allocation."
        "coach_position" -> "Detailed coach composition layout for AP Express (20806) and other express/superfast trains."
        "track_train" -> "Live GPS train tracking & expected delay status at upcoming stations."
        "order_food" -> "IRCTC e-Catering: Order hygienic hot food from top restaurants delivered to your seat on Train 20806."
        "file_refund" -> "Instant ticket cancellation and TDR filing with automated refund to source bank account."
        "rail_madad" -> "Direct grievance integration with Rail Madad 139 for swift in-journey resolution."
        "go_waves" -> "Access Indian Railways official digital magazine, circulars, and announcements."
        else -> "Official RailOne passenger service is available for all Indian Railways passengers."
      }
      Text(
        text = message,
        fontSize = 13.5.sp,
        color = Color(0xFF546E7A),
        lineHeight = 18.sp
      )
    },
    confirmButton = {
      Button(
        onClick = {
          onDismiss()
          onAction()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
      ) {
        Text("Proceed", color = Color.White, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Close", color = Color(0xFF546E7A))
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(20.dp)
  )
}
