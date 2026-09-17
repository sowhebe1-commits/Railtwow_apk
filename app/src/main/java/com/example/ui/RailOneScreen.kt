package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.example.model.SampleTickets
import com.example.model.TicketInfo
import com.example.ui.components.ChangeBoardingDialog
import com.example.ui.components.EditTicketBottomSheet
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.components.OfferingDetailDialog
import com.example.ui.components.OfferingItem
import com.example.ui.components.PnrStatusQuickDialog
import com.example.ui.components.RailOneBottomNavBar
import com.example.ui.components.RailOneNavTab
import com.example.ui.components.RefundRulesBottomSheet
import com.example.ui.components.ReturnJourneyBottomSheet
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.TicketQrDialog
import com.example.ui.components.TriviaItem
import com.example.ui.components.UtsConnectingDialog
import com.example.ui.theme.RailBackground
import kotlinx.coroutines.launch

@Composable
fun RailOneScreen(
  modifier: Modifier = Modifier,
) {
  // Navigation State
  var currentTab by remember { mutableStateOf(RailOneNavTab.HOME) }

  // Ticket State (UTS Monthly Ticket matching user screenshot)
  var ticket by remember { mutableStateOf(SampleTickets.utsMonthly) }
  var selectedLanguage by remember { mutableStateOf("English") }

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val clipboardManager = LocalClipboardManager.current

  // Home Screen Dialogs
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showNotificationsSheet by remember { mutableStateOf(false) }
  var showPnrDialog by remember { mutableStateOf(false) }
  var activeOfferingDialog by remember { mutableStateOf<OfferingItem?>(null) }

  // Ticket Details Dialogs & Sheets
  var showEditSheet by remember { mutableStateOf(false) }
  var showShareSheet by remember { mutableStateOf(false) }
  var showBoardingDialog by remember { mutableStateOf(false) }
  var showReturnSheet by remember { mutableStateOf(false) }
  var showRefundSheet by remember { mutableStateOf(false) }
  var showUtsDialog by remember { mutableStateOf(false) }
  var showQrDialog by remember { mutableStateOf(false) }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      if (currentTab != RailOneNavTab.BOOKINGS) {
        RailOneBottomNavBar(
          currentTab = currentTab,
          onTabSelected = { tab ->
            currentTab = tab
          }
        )
      }
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(RailBackground)
        .padding(innerPadding)
    ) {
      when (currentTab) {
        RailOneNavTab.HOME -> {
          // Official RailOne Home Screen matching user's screenshot
          RailOneHomeScreen(
            userName = "Roshan",
            onNavigateToBookings = {
              currentTab = RailOneNavTab.BOOKINGS
            },
            onOfferingClick = { offering ->
              if (offering.id == "pnr_status") {
                showPnrDialog = true
              } else {
                activeOfferingDialog = offering
              }
            },
            onTriviaClick = { trivia ->
              scope.launch {
                snackbarHostState.showSnackbar(
                  message = "${trivia.title}: ${trivia.snippet}",
                  duration = SnackbarDuration.Short
                )
              }
            },
            onLanguageClick = {
              showLanguageDialog = true
            },
            onNotificationsClick = {
              showNotificationsSheet = true
            }
          )
        }

        RailOneNavTab.BOOKINGS -> {
          // The Complete Ticket inside "My Bookings"
          MyBookingsScreen(
            ticket = ticket,
            onCopyPnr = { pnr ->
              clipboardManager.setText(AnnotatedString(pnr))
              scope.launch {
                snackbarHostState.showSnackbar("PNR $pnr copied to clipboard")
              }
            },
            onBackClick = {
              currentTab = RailOneNavTab.HOME
            },
            onPdfClick = {
              scope.launch {
                snackbarHostState.showSnackbar("e-Ticket PDF downloaded (PNR: ${ticket.pnrNumber})")
              }
            },
            onEmailClick = {
              scope.launch {
                snackbarHostState.showSnackbar("e-Ticket sent to roshan93721p@gmail.com")
              }
            },
            onShareClick = {
              showShareSheet = true
            },
            onEditTicket = {
              showEditSheet = true
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
            onRefreshPassengerStatus = {
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
            onViewQr = {
              showQrDialog = true
            }
          )
        }

        RailOneNavTab.YOU -> {
          // Roshan's User Profile Tab
          YouProfileScreen(
            userName = "Roshan",
            email = "roshan93721p@gmail.com",
            phone = "+91 93721 84022",
            irctcUserId = "ROSHAN_937",
            onItemClick = { item ->
              scope.launch {
                snackbarHostState.showSnackbar("Selected $item")
              }
            }
          )
        }

        RailOneNavTab.MENU -> {
          // RailOne Menu & Services
          MenuScreen(
            onMenuItemClick = { item ->
              scope.launch {
                snackbarHostState.showSnackbar(item)
              }
            }
          )
        }
      }

      // Home Screen Sheets & Dialogs
      if (showLanguageDialog) {
        LanguageSelectionDialog(
          currentLang = selectedLanguage,
          onSelectLanguage = { lang ->
            selectedLanguage = lang
            showLanguageDialog = false
            scope.launch {
              snackbarHostState.showSnackbar("Language switched to $lang")
            }
          },
          onDismiss = { showLanguageDialog = false }
        )
      }

      if (showNotificationsSheet) {
        NotificationsBottomSheet(
          onDismiss = { showNotificationsSheet = false },
          onViewBooking = {
            currentTab = RailOneNavTab.BOOKINGS
          }
        )
      }

      if (showPnrDialog) {
        PnrStatusQuickDialog(
          currentPnr = ticket.pnrNumber,
          onCheckPnr = { pnr ->
            showPnrDialog = false
          },
          onViewActiveBooking = {
            showPnrDialog = false
            currentTab = RailOneNavTab.BOOKINGS
          },
          onDismiss = { showPnrDialog = false }
        )
      }

      val currentOffering = activeOfferingDialog
      if (currentOffering != null) {
        OfferingDetailDialog(
          offering = currentOffering,
          onDismiss = { activeOfferingDialog = null },
          onAction = {
            when (currentOffering.id) {
              "order_food" -> {
                scope.launch {
                  snackbarHostState.showSnackbar("Opening IRCTC e-Catering for Train ${ticket.trainNumber}...")
                }
              }
              "coach_position" -> {
                currentTab = RailOneNavTab.BOOKINGS
              }
              "track_train" -> {
                scope.launch {
                  snackbarHostState.showSnackbar("Train ${ticket.trainNumber} (${ticket.trainName}) is running on time.")
                }
              }
              "rail_madad" -> {
                scope.launch {
                  snackbarHostState.showSnackbar("Connecting to Rail Madad Helpline 139...")
                }
              }
              else -> {
                currentTab = RailOneNavTab.BOOKINGS
              }
            }
          }
        )
      }

      // Ticket Action Dialogs and Sheets
      if (showEditSheet) {
        EditTicketBottomSheet(
          ticket = ticket,
          onDismiss = { showEditSheet = false },
          onSave = { updatedTicket ->
            ticket = updatedTicket
            showEditSheet = false
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
