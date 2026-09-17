package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SampleTickets
import com.example.model.TicketInfo
import com.example.ui.components.ChangeBoardingDialog
import com.example.ui.components.EditTicketBottomSheet
import com.example.ui.components.PassengerSection
import com.example.ui.components.RailOneHeader
import com.example.ui.components.RefundRulesBottomSheet
import com.example.ui.components.ReturnJourneyBottomSheet
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.TicketCard
import com.example.ui.components.TicketDetailsExtras
import com.example.ui.components.TicketQrDialog
import com.example.ui.components.UtsConnectingDialog
import com.example.ui.theme.RailBackground
import com.example.ui.theme.RailBluePrimary
import kotlinx.coroutines.launch

@Composable
fun RailOneScreen(
  modifier: Modifier = Modifier,
) {
  var ticket by remember { mutableStateOf(SampleTickets.apExpress) }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val clipboardManager = LocalClipboardManager.current

  // Dialog & Sheet states
  var showEditSheet by remember { mutableStateOf(false) }
  var showShareSheet by remember { mutableStateOf(false) }
  var showBoardingDialog by remember { mutableStateOf(false) }
  var showReturnSheet by remember { mutableStateOf(false) }
  var showRefundSheet by remember { mutableStateOf(false) }
  var showUtsDialog by remember { mutableStateOf(false) }
  var showQrDialog by remember { mutableStateOf(false) }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showEditSheet = true },
        icon = {
          Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit All Ticket Details",
            tint = Color.White
          )
        },
        text = {
          Text(
            text = "Edit Ticket",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
          )
        },
        containerColor = RailBluePrimary,
        contentColor = Color.White,
        modifier = Modifier.testTag("floating_edit_ticket_button")
      )
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(RailBackground)
        .padding(innerPadding)
    ) {
      LazyColumn(
        modifier = Modifier.fillMaxSize()
      ) {
        // 1. Top Header with back button, ticket title, greeting row, and Edit button
        item {
          RailOneHeader(
            transactionId = ticket.transactionId,
            passengerGreeting = ticket.passengerGreeting,
            onBackClick = {
              scope.launch {
                snackbarHostState.showSnackbar(
                  message = "You are viewing your active booked journey",
                  duration = SnackbarDuration.Short
                )
              }
            },
            onPdfClick = {
              scope.launch {
                snackbarHostState.showSnackbar(
                  message = "Ticket PDF downloaded successfully (PNR: ${ticket.pnrNumber})",
                  duration = SnackbarDuration.Short
                )
              }
            },
            onEmailClick = {
              scope.launch {
                snackbarHostState.showSnackbar(
                  message = "e-Ticket sent to registered email address",
                  duration = SnackbarDuration.Short
                )
              }
            },
            onShareClick = {
              showShareSheet = true
            },
            onEditClick = {
              showEditSheet = true
            }
          )
        }

        // 2. Main Ticket Card matching the user's screenshot
        item {
          TicketCard(
            ticket = ticket,
            onCopyPnr = { pnr ->
              clipboardManager.setText(AnnotatedString(pnr))
              scope.launch {
                snackbarHostState.showSnackbar(
                  message = "PNR $pnr copied to clipboard",
                  duration = SnackbarDuration.Short
                )
              }
            },
            onChangeBoarding = {
              showBoardingDialog = true
            },
            onReturnJourney = {
              showReturnSheet = true
            },
            onRefundRules = {
              showRefundSheet = true
            },
            onEditTicket = {
              showEditSheet = true
            }
          )
        }

        // 3. Passenger Details Section
        item {
          PassengerSection(
            passengers = ticket.passengers,
            onRefreshStatus = {
              scope.launch {
                val currentStatus = ticket.passengers.firstOrNull()?.currentStatus ?: "CNF"
                snackbarHostState.showSnackbar(
                  message = "Status verified: $currentStatus (Chart Not Prepared)",
                  duration = SnackbarDuration.Short
                )
              }
            },
            onBookConnectingUts = {
              showUtsDialog = true
            },
            onEditPassenger = {
              showEditSheet = true
            }
          )
        }

        // 4. Ticket Details Extras (QR code, coach position, and fare breakdown)
        item {
          TicketDetailsExtras(
            ticket = ticket,
            onViewQr = {
              showQrDialog = true
            }
          )
        }
      }

      // Dialogs and Bottom Sheets
      if (showEditSheet) {
        EditTicketBottomSheet(
          ticket = ticket,
          onDismiss = { showEditSheet = false },
          onSave = { updatedTicket ->
            ticket = updatedTicket
            scope.launch {
              snackbarHostState.showSnackbar("Ticket details updated successfully!")
            }
          }
        )
      }

      if (showShareSheet) {
        ShareBottomSheet(
          ticket = ticket,
          onDismiss = { showShareSheet = false },
          onCopyText = { text ->
            clipboardManager.setText(AnnotatedString(text))
            scope.launch {
              snackbarHostState.showSnackbar("Journey details copied to clipboard")
            }
          }
        )
      }

      if (showBoardingDialog) {
        ChangeBoardingDialog(
          currentStationCode = ticket.currentBoardingCode,
          stations = ticket.intermediateStations,
          onSelectStation = { newStation ->
            ticket = ticket.copy(
              currentBoardingStation = newStation.name,
              currentBoardingCode = newStation.code,
              fromStation = newStation.name.uppercase(),
              fromCode = newStation.code,
              departureTime = newStation.departureTime,
              bookingMeta = "1 Adult, 0 Child | SL | TATKAL | ${newStation.name.uppercase()}"
            )
            showBoardingDialog = false
            scope.launch {
              snackbarHostState.showSnackbar("Boarding station changed to ${newStation.name} (${newStation.code})")
            }
          },
          onDismiss = { showBoardingDialog = false }
        )
      }

      if (showReturnSheet) {
        ReturnJourneyBottomSheet(
          ticket = ticket,
          onDismiss = { showReturnSheet = false },
          onBookTrain = { trainName ->
            showReturnSheet = false
            scope.launch {
              snackbarHostState.showSnackbar("Selected return train: $trainName")
            }
          }
        )
      }

      if (showRefundSheet) {
        RefundRulesBottomSheet(
          onDismiss = { showRefundSheet = false }
        )
      }

      if (showUtsDialog) {
        UtsConnectingDialog(
          onDismiss = { showUtsDialog = false },
          onConfirmUts = { destination ->
            showUtsDialog = false
            scope.launch {
              snackbarHostState.showSnackbar("Connecting UTS paperless ticket booked to $destination")
            }
          }
        )
      }

      if (showQrDialog) {
        TicketQrDialog(
          ticket = ticket,
          onDismiss = { showQrDialog = false }
        )
      }
    }
  }
}
