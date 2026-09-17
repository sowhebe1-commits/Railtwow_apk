package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DirectionsRailway
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.StationStop
import com.example.model.TicketInfo
import com.example.ui.theme.RailBlueDark
import com.example.ui.theme.RailBluePrimary
import com.example.ui.theme.RailCardBorder
import com.example.ui.theme.RailPillBorder
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg

/**
 * 1. Share Ticket Bottom Sheet (Highlight of user red arrow)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
  ticket: TicketInfo,
  onDismiss: () -> Unit,
  onCopyText: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val shareMessage = """
    🚆 Indian Railways / RailOne Ticket
    PNR: ${ticket.pnrNumber}
    Train: ${ticket.trainNumber} - ${ticket.trainName}
    From: ${ticket.fromStation} (${ticket.fromCode}) at ${ticket.departureTime}, ${ticket.departureDate}
    To: ${ticket.toStation} (${ticket.toCode}) at ${ticket.arrivalTime}, ${ticket.arrivalDate}
    Passenger: ${ticket.passengers.firstOrNull()?.name ?: ""}
    Status: ${ticket.passengers.firstOrNull()?.currentStatus ?: ""}
    Have a safe and happy journey!
  """.trimIndent()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    modifier = modifier.testTag("share_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Share Ticket Details",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A)
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Outlined.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Ticket Summary Preview Card
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, RailCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "${ticket.trainNumber} - ${ticket.trainName}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "PNR: ${ticket.pnrNumber} • ${ticket.fromCode} ➔ ${ticket.toCode}",
            fontSize = 13.sp,
            color = RailBluePrimary,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Passenger: ${ticket.passengers.firstOrNull()?.name} (${ticket.passengers.firstOrNull()?.currentStatus})",
            fontSize = 12.5.sp,
            color = Color(0xFF475569)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action: Copy details
      OutlinedButton(
        onClick = {
          onCopyText(shareMessage)
          onDismiss()
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, RailPillBorder),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("share_copy_button")
      ) {
        Icon(Icons.Outlined.ContentCopy, contentDescription = null, tint = RailBluePrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Copy Journey Details", color = RailBluePrimary, fontWeight = FontWeight.SemiBold)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action: System Share Intent
      Button(
        onClick = {
          val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
          }
          val shareIntent = Intent.createChooser(sendIntent, "Share RailOne Ticket via")
          context.startActivity(shareIntent)
          onDismiss()
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("share_intent_button")
      ) {
        Icon(Icons.Outlined.Share, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Share via Apps / WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

/**
 * 2. Change Boarding Station Dialog
 */
@Composable
fun ChangeBoardingDialog(
  currentStationCode: String,
  stations: List<StationStop>,
  onSelectStation: (StationStop) -> Unit,
  onDismiss: () -> Unit,
) {
  var selectedCode by remember { mutableStateOf(currentStationCode) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White,
      shadowElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("change_boarding_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Change Boarding Station",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A)
        )
        Text(
          text = "Per IRCTC rules, boarding point can be changed up to 4 hours before train departure.",
          fontSize = 12.sp,
          color = Color(0xFF64748B),
          modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        HorizontalDivider(color = RailCardBorder)

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(vertical = 8.dp)
        ) {
          items(stations.filter { it.isSelectableBoarding }) { station ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedCode = station.code }
                .padding(vertical = 8.dp, horizontal = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = (selectedCode == station.code),
                onClick = { selectedCode = station.code },
                colors = RadioButtonDefaults.colors(selectedColor = RailBluePrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "${station.name} (${station.code})",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF0F172A)
                )
                Text(
                  text = "Departure: ${station.departureTime} • Distance: ${station.distanceKm} km",
                  fontSize = 11.5.sp,
                  color = Color(0xFF64748B)
                )
              }
            }
          }
        }

        HorizontalDivider(color = RailCardBorder)

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, RailPillBorder),
            modifier = Modifier.height(40.dp)
          ) {
            Text("Cancel", color = Color(0xFF64748B))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = {
              val chosen = stations.find { it.code == selectedCode }
              if (chosen != null) onSelectStation(chosen)
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
            modifier = Modifier.height(40.dp)
          ) {
            Text("Confirm Change", color = Color.White)
          }
        }
      }
    }
  }
}

/**
 * 3. Return Journey Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnJourneyBottomSheet(
  ticket: TicketInfo,
  onDismiss: () -> Unit,
  onBookTrain: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  data class ReturnTrain(
    val number: String,
    val name: String,
    val depTime: String,
    val arrTime: String,
    val duration: String,
    val slSeats: String,
    val acSeats: String,
    val fare: String,
  )

  val returnTrains = remember {
    listOf(
      ReturnTrain("20805", "AP EXPRESS", "22:00", "04:30", "6h 30m", "AVAILABLE-48", "AVAILABLE-16", "₹495"),
      ReturnTrain("12717", "RATNACHAL SF EXP", "12:55", "19:15", "6h 20m", "AVAILABLE-110", "AVAILABLE-34", "₹185"),
      ReturnTrain("20833", "VANDE BHARAT EXP", "05:45", "09:50", "4h 05m", "CC: AVL-62", "EC: AVL-12", "₹960"),
      ReturnTrain("12861", "LINK DAKSIN EXP", "18:40", "01:10", "6h 30m", "RAC 12", "AVAILABLE-08", "₹480")
    )
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    modifier = modifier.testTag("return_journey_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Return Journey Trains",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          Text(
            text = "${ticket.toStation} (${ticket.toCode}) ➔ ${ticket.fromStation} (${ticket.fromCode})",
            fontSize = 13.sp,
            color = RailBluePrimary,
            fontWeight = FontWeight.SemiBold
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Outlined.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp)
      ) {
        items(returnTrains) { train ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, RailCardBorder),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 5.dp)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${train.number} ${train.name}",
                  fontSize = 14.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )
                Text(
                  text = train.fare,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = RailBluePrimary
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${train.depTime} (${ticket.toCode}) ── ${train.duration} ──> ${train.arrTime} (${ticket.fromCode})",
                  fontSize = 12.sp,
                  color = Color(0xFF64748B)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Box(
                    modifier = Modifier
                      .background(StatusGreenBg, RoundedCornerShape(6.dp))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "SL: ${train.slSeats}",
                      fontSize = 11.5.sp,
                      color = StatusGreen,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                  Box(
                    modifier = Modifier
                      .background(Color(0xFFEFF6FF), RoundedCornerShape(6.dp))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "3A: ${train.acSeats}",
                      fontSize = 11.5.sp,
                      color = RailBluePrimary,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }

                Button(
                  onClick = { onBookTrain(train.name) },
                  shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
                  modifier = Modifier.height(32.dp)
                ) {
                  Text("Book", fontSize = 12.sp, color = Color.White)
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

/**
 * 4. Refund Rules Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundRulesBottomSheet(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    modifier = modifier.testTag("refund_rules_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Outlined.Info, contentDescription = null, tint = RailBluePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Indian Railways Refund Rules",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Outlined.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(320.dp)
      ) {
        item {
          RefundRuleItem(
            title = "Cancellation > 48 hours before departure",
            rule = "Flat clerkage charge of ₹60 for Sleeper (SL) class, ₹180 for 3AC, and ₹240 for 1AC/Executive."
          )
        }
        item {
          RefundRuleItem(
            title = "Between 48 hours and 12 hours",
            rule = "25% of the fare subject to the minimum flat clerkage charge per passenger."
          )
        }
        item {
          RefundRuleItem(
            title = "Between 12 hours and 4 hours (Chart Prep)",
            rule = "50% of the fare subject to the minimum flat clerkage charge per passenger."
          )
        }
        item {
          RefundRuleItem(
            title = "Confirmed Tatkal Tickets",
            rule = "No refund is admissible on cancellation of confirmed Tatkal tickets, as per Railway Board guidelines."
          )
        }
        item {
          RefundRuleItem(
            title = "Train Cancelled / Delayed > 3 Hours",
            rule = "Full refund of ticket fare without any deduction if TDR filed or cancelled online before chart departure."
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = onDismiss,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
      ) {
        Text("I Understand", fontWeight = FontWeight.Bold, color = Color.White)
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun RefundRuleItem(title: String, rule: String) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFFF8FAFC),
    border = BorderStroke(1.dp, RailCardBorder),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = rule, fontSize = 12.sp, color = Color(0xFF475569), lineHeight = 17.sp)
    }
  }
}

/**
 * 5. Connecting UTS Journey Dialog
 */
@Composable
fun UtsConnectingDialog(
  onDismiss: () -> Unit,
  onConfirmUts: (String) -> Unit,
) {
  val destinations = listOf(
    "Simhachalam (SCM) - 10 km",
    "Kottavalasa (KTV) - 26 km",
    "Vizianagaram Jn (VZM) - 61 km",
    "Anakapalle (AKP) - 33 km",
    "Bobbili Jn (VBL) - 115 km"
  )
  var selectedDest by remember { mutableStateOf(destinations[0]) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White,
      shadowElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("uts_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Outlined.DirectionsRailway, contentDescription = null, tint = RailBluePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Book Connecting UTS",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
        }

        Text(
          text = "Select your unreserved connecting station from Visakhapatnam (VSKP):",
          fontSize = 12.5.sp,
          color = Color(0xFF64748B),
          modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
        )

        destinations.forEach { dest ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedDest = dest }
              .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = (selectedDest == dest),
              onClick = { selectedDest = dest },
              colors = RadioButtonDefaults.colors(selectedColor = RailBluePrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = dest, fontSize = 13.5.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, RailPillBorder),
            modifier = Modifier.height(40.dp)
          ) {
            Text("Cancel", color = Color(0xFF64748B))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = { onConfirmUts(selectedDest) },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
            modifier = Modifier.height(40.dp)
          ) {
            Text("Book UTS (₹15)", color = Color.White)
          }
        }
      }
    }
  }
}

/**
 * 6. Digital IRCTC Ticket QR Code Dialog (for TTE inspection)
 */
@Composable
fun TicketQrDialog(
  ticket: TicketInfo,
  onDismiss: () -> Unit,
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("ticket_qr_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Digital IRCTC Ticket QR",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Outlined.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Simulated QR Code with Canvas
        Box(
          modifier = Modifier
            .size(190.dp)
            .background(Color.White)
            .border(2.dp, RailBluePrimary, RoundedCornerShape(12.dp))
            .padding(12.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.size(160.dp)) {
            val step = size.width / 16f
            for (i in 0 until 16) {
              for (j in 0 until 16) {
                // Pseudo QR pattern based on coordinates
                val isCornerFinder =
                  (i < 5 && j < 5) || (i < 5 && j > 10) || (i > 10 && j < 5)
                val isBlack = if (isCornerFinder) {
                  (i == 0 || i == 4 || j == 0 || j == 4 || (i in 2..2 && j in 2..2)) ||
                    (i == 0 || i == 4 || j == 11 || j == 15 || (i in 2..2 && j in 13..13)) ||
                    (i == 11 || i == 15 || j == 0 || j == 4 || (i in 13..13 && j in 2..2))
                } else {
                  (i * 7 + j * 13 + (i xor j)) % 3 == 0
                }
                if (isBlack) {
                  drawRect(
                    color = Color(0xFF0D59EC),
                    topLeft = Offset(i * step, j * step),
                    size = Size(step * 0.92f, step * 0.92f)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "PNR: ${ticket.pnrNumber}",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A)
        )
        Text(
          text = "Cryptographically verified by Indian Railways CRIS",
          fontSize = 11.5.sp,
          color = StatusGreen,
          textAlign = TextAlign.Center
        )
        Text(
          text = "Coach ${ticket.passengers.firstOrNull()?.coach}, Berth ${ticket.passengers.firstOrNull()?.berthNumber} • Valid for Travel",
          fontSize = 12.sp,
          color = Color(0xFF64748B),
          modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
        ) {
          Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
