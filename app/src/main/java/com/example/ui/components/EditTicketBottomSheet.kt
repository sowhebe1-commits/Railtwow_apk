package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsRailway
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FareBreakdown
import com.example.model.PassengerInfo
import com.example.model.TicketInfo
import com.example.ui.theme.RailBluePrimary
import com.example.ui.theme.RailCardBorder
import com.example.ui.theme.RailPillBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTicketBottomSheet(
  ticket: TicketInfo,
  onDismiss: () -> Unit,
  onSave: (TicketInfo) -> Unit,
  modifier: Modifier = Modifier,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  // Header & Transaction
  var transactionId by remember { mutableStateOf(ticket.transactionId) }
  var passengerGreeting by remember { mutableStateOf(ticket.passengerGreeting) }

  // Train & PNR
  var trainNumber by remember { mutableStateOf(ticket.trainNumber) }
  var trainName by remember { mutableStateOf(ticket.trainName) }
  var pnrNumber by remember { mutableStateOf(ticket.pnrNumber) }

  // Journey & Stations
  var departureTime by remember { mutableStateOf(ticket.departureTime) }
  var departureDate by remember { mutableStateOf(ticket.departureDate) }
  var fromStation by remember { mutableStateOf(ticket.fromStation) }
  var fromCode by remember { mutableStateOf(ticket.fromCode) }
  var fromPlatform by remember { mutableStateOf(ticket.fromPlatform) }

  var duration by remember { mutableStateOf(ticket.duration) }

  var arrivalTime by remember { mutableStateOf(ticket.arrivalTime) }
  var arrivalDate by remember { mutableStateOf(ticket.arrivalDate) }
  var toStation by remember { mutableStateOf(ticket.toStation) }
  var toCode by remember { mutableStateOf(ticket.toCode) }
  var toPlatform by remember { mutableStateOf(ticket.toPlatform) }

  // Booking details
  var bookingMeta by remember { mutableStateOf(ticket.bookingMeta) }
  var bookedOn by remember { mutableStateOf(ticket.bookedOn) }
  var travelClass by remember { mutableStateOf(ticket.travelClass) }
  var quota by remember { mutableStateOf(ticket.quota) }

  // Passenger 1
  val firstPassenger = ticket.passengers.firstOrNull()
  var passName by remember { mutableStateOf(firstPassenger?.name ?: "D VARDHAN") }
  var passGender by remember { mutableStateOf(firstPassenger?.gender ?: "Male") }
  var passAge by remember { mutableStateOf(firstPassenger?.age?.toString() ?: "20") }
  var passBookingStatus by remember { mutableStateOf(firstPassenger?.bookingStatus ?: "CNF/S3/40/SU") }
  var passCurrentStatus by remember { mutableStateOf(firstPassenger?.currentStatus ?: "CNF/S3/40/SU") }
  var passCoach by remember { mutableStateOf(firstPassenger?.coach ?: "S3") }
  var passBerth by remember { mutableStateOf(firstPassenger?.berthNumber?.toString() ?: "40") }
  var passBerthType by remember { mutableStateOf(firstPassenger?.berthType ?: "Side Upper (SU)") }

  // Fare
  var totalAmount by remember { mutableStateOf(ticket.fare.totalAmount.toString()) }
  var baseFare by remember { mutableStateOf(ticket.fare.baseFare.toString()) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    modifier = modifier.testTag("edit_ticket_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .verticalScroll(rememberScrollState())
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Outlined.Edit, contentDescription = null, tint = RailBluePrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Edit Ticket Details",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Text(
              text = "Apne hisab se sabhi values customize karein",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
          }
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Outlined.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = RailCardBorder)
      Spacer(modifier = Modifier.height(14.dp))

      // Section 1: Train & PNR Details
      SectionLabel(icon = Icons.Outlined.DirectionsRailway, title = "Train & PNR Details")
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Train No.",
          value = trainNumber,
          onValueChange = { trainNumber = it },
          modifier = Modifier.weight(1f),
          keyboardType = KeyboardType.Number
        )
        EditField(
          label = "PNR Number",
          value = pnrNumber,
          onValueChange = { pnrNumber = it },
          modifier = Modifier.weight(1.5f),
          keyboardType = KeyboardType.Number
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      EditField(
        label = "Train Name",
        value = trainName,
        onValueChange = { trainName = it },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Section 2: Journey Timings & Stations
      SectionLabel(icon = Icons.Outlined.ConfirmationNumber, title = "Stations & Journey Times")
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "From Station",
          value = fromStation,
          onValueChange = { fromStation = it },
          modifier = Modifier.weight(1.6f)
        )
        EditField(
          label = "Code",
          value = fromCode,
          onValueChange = { fromCode = it },
          modifier = Modifier.weight(0.9f)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Departure Time",
          value = departureTime,
          onValueChange = { departureTime = it },
          modifier = Modifier.weight(1f)
        )
        EditField(
          label = "Departure Date",
          value = departureDate,
          onValueChange = { departureDate = it },
          modifier = Modifier.weight(1.4f)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
      EditField(
        label = "Duration (e.g. 6h:50m)",
        value = duration,
        onValueChange = { duration = it },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(10.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "To Station",
          value = toStation,
          onValueChange = { toStation = it },
          modifier = Modifier.weight(1.6f)
        )
        EditField(
          label = "Code",
          value = toCode,
          onValueChange = { toCode = it },
          modifier = Modifier.weight(0.9f)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Arrival Time",
          value = arrivalTime,
          onValueChange = { arrivalTime = it },
          modifier = Modifier.weight(1f)
        )
        EditField(
          label = "Arrival Date",
          value = arrivalDate,
          onValueChange = { arrivalDate = it },
          modifier = Modifier.weight(1.4f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Section 3: Passenger Information
      SectionLabel(icon = Icons.Outlined.Person, title = "Passenger Details")
      EditField(
        label = "Passenger Name",
        value = passName,
        onValueChange = {
          passName = it
          passengerGreeting = it
        },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Gender (Male/Female)",
          value = passGender,
          onValueChange = { passGender = it },
          modifier = Modifier.weight(1f)
        )
        EditField(
          label = "Age",
          value = passAge,
          onValueChange = { passAge = it },
          modifier = Modifier.weight(1f),
          keyboardType = KeyboardType.Number
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Booking Status",
          value = passBookingStatus,
          onValueChange = { passBookingStatus = it },
          modifier = Modifier.weight(1f)
        )
        EditField(
          label = "Current Status",
          value = passCurrentStatus,
          onValueChange = { passCurrentStatus = it },
          modifier = Modifier.weight(1f)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Coach (e.g. S3)",
          value = passCoach,
          onValueChange = { passCoach = it },
          modifier = Modifier.weight(1f)
        )
        EditField(
          label = "Berth No. (e.g. 40)",
          value = passBerth,
          onValueChange = { passBerth = it },
          modifier = Modifier.weight(1f),
          keyboardType = KeyboardType.Number
        )
        EditField(
          label = "Berth Type",
          value = passBerthType,
          onValueChange = { passBerthType = it },
          modifier = Modifier.weight(1.3f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Section 4: Header & Booking Meta
      SectionLabel(icon = Icons.Outlined.Payments, title = "Transaction & Booking Info")
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        EditField(
          label = "Transaction ID",
          value = transactionId,
          onValueChange = { transactionId = it },
          modifier = Modifier.weight(1.4f)
        )
        EditField(
          label = "Total Fare (₹)",
          value = totalAmount,
          onValueChange = { totalAmount = it },
          modifier = Modifier.weight(1f),
          keyboardType = KeyboardType.Decimal
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      EditField(
        label = "Booking Description line",
        value = bookingMeta,
        onValueChange = { bookingMeta = it },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(8.dp))
      EditField(
        label = "Booked Date (e.g. Sun, 13 Jul 25)",
        value = bookedOn,
        onValueChange = { bookedOn = it },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedButton(
          onClick = onDismiss,
          shape = RoundedCornerShape(24.dp),
          border = BorderStroke(1.dp, RailPillBorder),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
        ) {
          Text("Cancel", color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
        }

        Button(
          onClick = {
            val updatedPassenger = (firstPassenger ?: PassengerInfo(
              id = "p1",
              name = passName,
              gender = passGender,
              age = passAge.toIntOrNull() ?: 20,
              bookingStatus = passBookingStatus,
              currentStatus = passCurrentStatus,
              coach = passCoach,
              berthNumber = passBerth.toIntOrNull() ?: 40,
              berthType = passBerthType
            )).copy(
              name = passName,
              gender = passGender,
              age = passAge.toIntOrNull() ?: 20,
              bookingStatus = passBookingStatus,
              currentStatus = passCurrentStatus,
              coach = passCoach,
              berthNumber = passBerth.toIntOrNull() ?: 40,
              berthType = passBerthType
            )

            val parsedTotal = totalAmount.toDoubleOrNull() ?: ticket.fare.totalAmount
            val parsedBase = baseFare.toDoubleOrNull() ?: ticket.fare.baseFare

            val updatedTicket = ticket.copy(
              transactionId = transactionId,
              passengerGreeting = passengerGreeting,
              trainNumber = trainNumber,
              trainName = trainName,
              pnrNumber = pnrNumber,
              departureTime = departureTime,
              departureDate = departureDate,
              fromStation = fromStation,
              fromCode = fromCode,
              fromPlatform = fromPlatform,
              duration = duration,
              arrivalTime = arrivalTime,
              arrivalDate = arrivalDate,
              toStation = toStation,
              toCode = toCode,
              toPlatform = toPlatform,
              bookingMeta = bookingMeta,
              bookedOn = bookedOn,
              passengers = listOf(updatedPassenger),
              fare = ticket.fare.copy(
                totalAmount = parsedTotal,
                baseFare = parsedBase
              )
            )

            onSave(updatedTicket)
            onDismiss()
          },
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RailBluePrimary),
          modifier = Modifier
            .weight(1.3f)
            .height(48.dp)
            .testTag("save_ticket_changes_button")
        ) {
          Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun SectionLabel(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(bottom = 8.dp)
  ) {
    Icon(icon, contentDescription = null, tint = RailBluePrimary, modifier = Modifier.size(17.dp))
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = title,
      fontSize = 13.5.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF0F172A)
    )
  }
}

@Composable
private fun EditField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  keyboardType: KeyboardType = KeyboardType.Text,
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, fontSize = 12.sp) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = RailBluePrimary,
      unfocusedBorderColor = RailCardBorder,
      focusedLabelColor = RailBluePrimary
    ),
    shape = RoundedCornerShape(10.dp),
    modifier = modifier
  )
}
