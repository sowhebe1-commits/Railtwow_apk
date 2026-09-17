package com.example.model

data class PassengerInfo(
  val id: String,
  val name: String,
  val gender: String,
  val age: Int,
  val bookingStatus: String,
  val currentStatus: String,
  val coach: String,
  val berthNumber: Int,
  val berthType: String, // e.g. Side Upper (SU)
)

data class StationStop(
  val code: String,
  val name: String,
  val departureTime: String,
  val distanceKm: Int,
  val isSelectableBoarding: Boolean = true,
)

data class FareBreakdown(
  val baseFare: Double,
  val tatkalCharge: Double,
  val superfastCharge: Double,
  val cateringCharge: Double,
  val irctcConvenienceFee: Double,
  val totalAmount: Double,
  val paymentMethod: String,
)

data class TicketInfo(
  val transactionId: String,
  val passengerGreeting: String,
  val trainNumber: String,
  val trainName: String,
  val pnrNumber: String,
  val departureTime: String,
  val departureDate: String,
  val fromStation: String,
  val fromCode: String,
  val fromPlatform: String,
  val duration: String,
  val arrivalTime: String,
  val arrivalDate: String,
  val toStation: String,
  val toCode: String,
  val toPlatform: String,
  val bookingMeta: String,
  val bookedOn: String,
  val travelClass: String,
  val quota: String,
  val currentBoardingStation: String,
  val currentBoardingCode: String,
  val liveStatus: String,
  val passengers: List<PassengerInfo>,
  val fare: FareBreakdown,
  val intermediateStations: List<StationStop>,
)

object SampleTickets {
  val apExpress = TicketInfo(
    transactionId = "100005915012767",
    passengerGreeting = "LOKANADHAM DUDDI",
    trainNumber = "20806",
    trainName = "AP EXPRESS",
    pnrNumber = "4855528192",
    departureTime = "21:30",
    departureDate = "Tue, 15 Jul 25",
    fromStation = "VIJAYAWADA JN.",
    fromCode = "BZA",
    fromPlatform = "PF 1",
    duration = "6h:50m",
    arrivalTime = "04:20",
    arrivalDate = "Wed, 16 Jul 25",
    toStation = "VISAKHAPATNAM",
    toCode = "VSKP",
    toPlatform = "PF 4",
    bookingMeta = "1 Adult, 0 Child | SL | TATKAL | VIJAYAWADA...",
    bookedOn = "Sun, 13 Jul 25",
    travelClass = "SL",
    quota = "TATKAL",
    currentBoardingStation = "VIJAYAWADA JN.",
    currentBoardingCode = "BZA",
    liveStatus = "On Time - Expected Departure 21:30",
    passengers = listOf(
      PassengerInfo(
        id = "p1",
        name = "D VARDHAN",
        gender = "Male",
        age = 20,
        bookingStatus = "CNF/S3/40/SU",
        currentStatus = "CNF/S3/40/SU",
        coach = "S3",
        berthNumber = 40,
        berthType = "Side Upper (SU)"
      )
    ),
    fare = FareBreakdown(
      baseFare = 380.0,
      tatkalCharge = 100.0,
      superfastCharge = 0.0,
      cateringCharge = 0.0,
      irctcConvenienceFee = 17.70,
      totalAmount = 497.70,
      paymentMethod = "UPI (RailOne / IRCTC)"
    ),
    intermediateStations = listOf(
      StationStop("BZA", "Vijayawada Jn.", "21:30", 0),
      StationStop("EE", "Eluru", "22:25", 60),
      StationStop("TDD", "Tadepalligudem", "23:05", 108),
      StationStop("RJY", "Rajahmundry", "23:48", 150),
      StationStop("SLO", "Samalkot Jn.", "00:38", 200),
      StationStop("ANV", "Annavaram", "01:08", 237),
      StationStop("TUNI", "Tuni", "01:28", 253),
      StationStop("AKP", "Anakapalle", "02:38", 317),
      StationStop("DVD", "Duvvada", "03:15", 332),
      StationStop("VSKP", "Visakhapatnam", "04:20", 350, isSelectableBoarding = false)
    )
  )
}
