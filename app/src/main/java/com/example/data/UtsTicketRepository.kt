package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UtsSavedDetails(
  val passengerName: String = "ROSHAN",
  val mobileNumber: String = "7983961490",
  val fromStation: String = "MANKHURD",
  val toStation: String = "NERUL",
  val distanceText: String = "—14 km—",
  val fareAmount: String = "₹150.00",
  val ticketType: String = "Season Ticket (Monthly Pass)",
  val ticketId: String = "XH6ZE4C002",
  val bookingCode: String = "R25759",
  val bookingDateTime: String = "09 Jul 2026, 20:01",
  val bookedOnDate: String = "09/07/2026 20:01",
  val validTillDate: String = "08/08/2026 23:59",
  val via: String = "-",
  val classType: String = "SECOND | ORDINARY | MONTHLY SEASON PASS",
  val passengerCount: String = "1 Adult (ROSHAN)"
)

object UtsTicketRepository {
  private const val PREFS_NAME = "uts_ticket_preferences"
  private const val KEY_NAME = "passenger_name"
  private const val KEY_MOBILE = "mobile_number"
  private const val KEY_FROM = "from_station"
  private const val KEY_TO = "to_station"
  private const val KEY_DISTANCE = "distance_text"
  private const val KEY_FARE = "fare_amount"
  private const val KEY_TICKET_TYPE = "ticket_type"
  private const val KEY_TICKET_ID = "ticket_id"
  private const val KEY_BOOKING_CODE = "booking_code"
  private const val KEY_BOOKING_DATE_TIME = "booking_date_time"
  private const val KEY_BOOKED_ON = "booked_on"
  private const val KEY_VALID_TILL = "valid_till"
  private const val KEY_VIA = "via"
  private const val KEY_CLASS_TYPE = "class_type"
  private const val KEY_PASSENGER_COUNT = "passenger_count"

  private var prefs: SharedPreferences? = null
  private val _ticketDetails = MutableStateFlow(UtsSavedDetails())
  val ticketDetails: StateFlow<UtsSavedDetails> = _ticketDetails.asStateFlow()

  fun init(context: Context) {
    if (prefs == null) {
      prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      loadFromPrefs()
    }
  }

  private fun loadFromPrefs() {
    val p = prefs ?: return
    val details = UtsSavedDetails(
      passengerName = p.getString(KEY_NAME, "ROSHAN") ?: "ROSHAN",
      mobileNumber = p.getString(KEY_MOBILE, "7983961490") ?: "7983961490",
      fromStation = p.getString(KEY_FROM, "MANKHURD") ?: "MANKHURD",
      toStation = p.getString(KEY_TO, "NERUL") ?: "NERUL",
      distanceText = p.getString(KEY_DISTANCE, "—14 km—") ?: "—14 km—",
      fareAmount = p.getString(KEY_FARE, "₹150.00") ?: "₹150.00",
      ticketType = p.getString(KEY_TICKET_TYPE, "Season Ticket (Monthly Pass)") ?: "Season Ticket (Monthly Pass)",
      ticketId = p.getString(KEY_TICKET_ID, "XH6ZE4C002") ?: "XH6ZE4C002",
      bookingCode = p.getString(KEY_BOOKING_CODE, "R25759") ?: "R25759",
      bookingDateTime = p.getString(KEY_BOOKING_DATE_TIME, "09 Jul 2026, 20:01") ?: "09 Jul 2026, 20:01",
      bookedOnDate = p.getString(KEY_BOOKED_ON, "09/07/2026 20:01") ?: "09/07/2026 20:01",
      validTillDate = p.getString(KEY_VALID_TILL, "08/08/2026 23:59") ?: "08/08/2026 23:59",
      via = p.getString(KEY_VIA, "-") ?: "-",
      classType = p.getString(KEY_CLASS_TYPE, "SECOND | ORDINARY | MONTHLY SEASON PASS") ?: "SECOND | ORDINARY | MONTHLY SEASON PASS",
      passengerCount = p.getString(KEY_PASSENGER_COUNT, "1 Adult (ROSHAN)") ?: "1 Adult (ROSHAN)"
    )
    _ticketDetails.value = details
  }

  fun saveDetails(details: UtsSavedDetails) {
    _ticketDetails.value = details
    prefs?.edit()?.apply {
      putString(KEY_NAME, details.passengerName)
      putString(KEY_MOBILE, details.mobileNumber)
      putString(KEY_FROM, details.fromStation)
      putString(KEY_TO, details.toStation)
      putString(KEY_DISTANCE, details.distanceText)
      putString(KEY_FARE, details.fareAmount)
      putString(KEY_TICKET_TYPE, details.ticketType)
      putString(KEY_TICKET_ID, details.ticketId)
      putString(KEY_BOOKING_CODE, details.bookingCode)
      putString(KEY_BOOKING_DATE_TIME, details.bookingDateTime)
      putString(KEY_BOOKED_ON, details.bookedOnDate)
      putString(KEY_VALID_TILL, details.validTillDate)
      putString(KEY_VIA, details.via)
      putString(KEY_CLASS_TYPE, details.classType)
      putString(KEY_PASSENGER_COUNT, details.passengerCount)
      apply()
    }
  }

  fun getDetails(): UtsSavedDetails = _ticketDetails.value
}
