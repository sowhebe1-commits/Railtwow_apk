package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.DirectionsBoat
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.Luggage
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Brand Colors from Screenshot
val RailOneDarkBlue = Color(0xFF002244)
val RailOneBrandBlue = Color(0xFF0066FF)
val RailOneLangBg = Color(0xFFEFF5FF)
val RailOneLangBorder = Color(0xFFB8D5FD)

@Composable
fun RailOneHomeTopBar(
  onLanguageClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. Language Switcher (A / अ)
    Surface(
      shape = CircleShape,
      color = RailOneLangBg,
      border = androidx.compose.foundation.BorderStroke(1.5.dp, RailOneLangBorder),
      modifier = Modifier
        .size(44.dp)
        .clickable(onClick = onLanguageClick)
        .testTag("btn_language_switcher")
    ) {
      Box(contentAlignment = Alignment.Center) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = RailOneBrandBlue
          )
          Text(
            text = "अ",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = RailOneBrandBlue,
            modifier = Modifier.offset(y = 2.dp)
          )
        }
      }
    }

    // 2. RailOne Brand Logo (Bold modern typography matching screenshot)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "Rail",
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF263238),
        letterSpacing = (-0.5).sp
      )
      Text(
        text = "One",
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF37474F),
        letterSpacing = (-0.5).sp
      )
    }

    // 3. Notification Bell with Badge "3"
    Surface(
      shape = CircleShape,
      color = Color.White,
      shadowElevation = 2.dp,
      modifier = Modifier
        .size(44.dp)
        .clickable(onClick = onNotificationsClick)
        .testTag("btn_notifications")
    ) {
      Box(contentAlignment = Alignment.Center) {
        BadgedBox(
          badge = {
            Badge(
              containerColor = Color(0xFFE53935),
              contentColor = Color.White,
              modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
            ) {
              Text(
                text = "3",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color(0xFF37474F),
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }
  }
}

@Composable
fun JourneyPlannerSection(
  onReservedClick: () -> Unit,
  onUnreservedClick: () -> Unit,
  onPlatformClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 6.dp)
  ) {
    Text(
      text = "Journey Planner",
      fontSize = 20.sp,
      fontWeight = FontWeight.ExtraBold,
      color = RailOneDarkBlue,
      modifier = Modifier.padding(bottom = 12.dp)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Reserved Card
      JourneyCardItem(
        title = "Reserved",
        onClick = onReservedClick,
        modifier = Modifier.weight(1f),
        canvasContent = { ReservedCoachIllustration() }
      )

      // 2. Unreserved Card
      JourneyCardItem(
        title = "Unreserved",
        onClick = onUnreservedClick,
        modifier = Modifier.weight(1f),
        canvasContent = { UnreservedCoachIllustration() }
      )

      // 3. Platform Card
      JourneyCardItem(
        title = "Platform",
        onClick = onPlatformClick,
        modifier = Modifier.weight(1f),
        canvasContent = { PlatformTrainIllustration() }
      )
    }
  }
}

@Composable
private fun JourneyCardItem(
  title: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  canvasContent: @Composable () -> Unit,
) {
  Column(
    modifier = modifier.clickable(onClick = onClick),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Card(
      shape = RoundedCornerShape(16.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(96.dp)
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        canvasContent()
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = title,
      fontSize = 15.sp,
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      color = RailOneDarkBlue,
      textAlign = TextAlign.Center
    )
  }
}

// 1. Reserved Coach Illustration
@Composable
fun ReservedCoachIllustration() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    // Scenic window background (Sky & mountain gradient)
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF81D4FA), Color(0xFFE1F5FE), Color(0xFFC8E6C9))
      )
    )

    // Mountain silhouettes in distance
    val mountainPath = Path().apply {
      moveTo(0f, height * 0.5f)
      lineTo(width * 0.35f, height * 0.25f)
      lineTo(width * 0.7f, height * 0.45f)
      lineTo(width, height * 0.3f)
      lineTo(width, height)
      lineTo(0f, height)
      close()
    }
    drawPath(mountainPath, color = Color(0xFF4CA5B5).copy(alpha = 0.6f))

    // Train Window Frame
    drawRoundRect(
      color = Color(0xFF37474F),
      topLeft = Offset(width * 0.08f, height * 0.08f),
      size = Size(width * 0.84f, height * 0.84f),
      cornerRadius = CornerRadius(12f, 12f),
      style = Stroke(width = 6f)
    )

    // Window glass reflection shine
    drawLine(
      color = Color.White.copy(alpha = 0.4f),
      start = Offset(width * 0.2f, height * 0.15f),
      end = Offset(width * 0.45f, height * 0.8f),
      strokeWidth = 4f
    )

    // Interior Coach table & passenger silhouettes
    drawRoundRect(
      color = Color(0xFF0288D1),
      topLeft = Offset(width * 0.05f, height * 0.62f),
      size = Size(width * 0.38f, height * 0.35f),
      cornerRadius = CornerRadius(8f, 8f)
    )
    drawRoundRect(
      color = Color(0xFF0288D1),
      topLeft = Offset(width * 0.57f, height * 0.62f),
      size = Size(width * 0.38f, height * 0.35f),
      cornerRadius = CornerRadius(8f, 8f)
    )

    // Passenger Heads
    drawCircle(
      color = Color(0xFFFFB74D),
      radius = width * 0.11f,
      center = Offset(width * 0.24f, height * 0.52f)
    )
    drawCircle(
      color = Color(0xFF81D4FA),
      radius = width * 0.1f,
      center = Offset(width * 0.75f, height * 0.52f)
    )
  }
}

// 2. Unreserved Coach Illustration
@Composable
fun UnreservedCoachIllustration() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    // Subway / Local Train background
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFD7CCC8))
      )
    )

    // Overhead grab rails / luggage rack
    drawLine(
      color = Color(0xFF78909C),
      start = Offset(0f, height * 0.18f),
      end = Offset(width, height * 0.18f),
      strokeWidth = 5f
    )
    for (i in 1..3) {
      val x = width * (i * 0.25f)
      drawLine(
        color = Color(0xFF90A4AE),
        start = Offset(x, height * 0.18f),
        end = Offset(x, height * 0.32f),
        strokeWidth = 3f
      )
      drawCircle(
        color = Color(0xFFE57373),
        radius = 7f,
        center = Offset(x, height * 0.34f)
      )
    }

    // Commuters seated in train
    // Left passenger
    drawCircle(
      color = Color(0xFFFFCC80),
      radius = width * 0.1f,
      center = Offset(width * 0.28f, height * 0.48f)
    )
    drawRoundRect(
      color = Color(0xFFE53935),
      topLeft = Offset(width * 0.16f, height * 0.58f),
      size = Size(width * 0.24f, height * 0.42f),
      cornerRadius = CornerRadius(6f, 6f)
    )

    // Right passenger
    drawCircle(
      color = Color(0xFFFFAB91),
      radius = width * 0.1f,
      center = Offset(width * 0.72f, height * 0.48f)
    )
    drawRoundRect(
      color = Color(0xFF1E88E5),
      topLeft = Offset(width * 0.6f, height * 0.58f),
      size = Size(width * 0.24f, height * 0.42f),
      cornerRadius = CornerRadius(6f, 6f)
    )
  }
}

// 3. Platform & Vande Bharat Train Illustration
@Composable
fun PlatformTrainIllustration() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    // Platform canopy ceiling
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFCFD8DC), Color(0xFFECEFF1))
      )
    )

    // Platform roof trusses
    drawLine(
      color = Color(0xFF90A4AE),
      start = Offset(0f, height * 0.12f),
      end = Offset(width, height * 0.25f),
      strokeWidth = 4f
    )

    // Modern Vande Bharat Train (Orange aerodynamic front)
    val trainPath = Path().apply {
      moveTo(0f, height * 0.3f)
      lineTo(width * 0.65f, height * 0.45f)
      quadraticBezierTo(width * 0.85f, height * 0.55f, width * 0.8f, height * 0.95f)
      lineTo(0f, height * 0.95f)
      close()
    }
    drawPath(trainPath, color = Color(0xFFFF6F00))

    // Sleek black aerodynamic windshield
    val windowPath = Path().apply {
      moveTo(width * 0.15f, height * 0.38f)
      lineTo(width * 0.55f, height * 0.47f)
      lineTo(width * 0.45f, height * 0.62f)
      lineTo(width * 0.12f, height * 0.54f)
      close()
    }
    drawPath(windowPath, color = Color(0xFF212121))

    // White body striping
    drawLine(
      color = Color.White,
      start = Offset(0f, height * 0.66f),
      end = Offset(width * 0.65f, height * 0.72f),
      strokeWidth = 4f
    )

    // Platform floor
    drawRect(
      color = Color(0xFFB0BEC5),
      topLeft = Offset(width * 0.55f, height * 0.75f),
      size = Size(width * 0.45f, height * 0.25f)
    )
    // Platform yellow safety line
    drawLine(
      color = Color(0xFFFFD54F),
      start = Offset(width * 0.56f, height * 0.76f),
      end = Offset(width, height * 0.8f),
      strokeWidth = 3f
    )
  }
}

// Data model for More Offerings grid items
data class OfferingItem(
  val id: String,
  val title: String,
  val icon: ImageVector,
  val containerColor: Color,
  val iconColor: Color,
)

val sampleOfferings = listOf(
  OfferingItem(
    id = "search_trains",
    title = "Search\nTrains",
    icon = Icons.Default.Search,
    containerColor = Color(0xFFFDE7EC),
    iconColor = Color(0xFFF04E69)
  ),
  OfferingItem(
    id = "pnr_status",
    title = "PNR\nStatus",
    icon = Icons.Rounded.ConfirmationNumber,
    containerColor = Color(0xFFE3F7E8),
    iconColor = Color(0xFF28A745)
  ),
  OfferingItem(
    id = "coach_position",
    title = "Coach\nPosition",
    icon = Icons.Default.Train,
    containerColor = Color(0xFFE1F3FE),
    iconColor = Color(0xFF1E88E5)
  ),
  OfferingItem(
    id = "track_train",
    title = "Track Your\nTrain",
    icon = Icons.Rounded.Navigation,
    containerColor = Color(0xFFFEF5DE),
    iconColor = Color(0xFFF59E0B)
  ),
  OfferingItem(
    id = "order_food",
    title = "Order\nFood",
    icon = Icons.Default.Fastfood,
    containerColor = Color(0xFFE3E8FD),
    iconColor = Color(0xFF5C6BC0)
  ),
  OfferingItem(
    id = "file_refund",
    title = "File\nRefund",
    icon = Icons.Default.LocalAtm,
    containerColor = Color(0xFFE6EDED),
    iconColor = Color(0xFF5A738E)
  ),
  OfferingItem(
    id = "rail_madad",
    title = "Rail\nMadad",
    icon = Icons.Default.Handshake,
    containerColor = Color(0xFFFDE7E7),
    iconColor = Color(0xFFE53935)
  ),
  OfferingItem(
    id = "go_waves",
    title = "Go To\nWAVES",
    icon = Icons.Rounded.ElectricBolt,
    containerColor = Color(0xFF4C5773),
    iconColor = Color.White
  )
)

@Composable
fun MoreOfferingsSection(
  onOfferingClick: (OfferingItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 10.dp)
  ) {
    Text(
      text = "More Offerings",
      fontSize = 20.sp,
      fontWeight = FontWeight.ExtraBold,
      color = RailOneDarkBlue,
      modifier = Modifier.padding(bottom = 12.dp)
    )

    // Row 1 (4 items)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      sampleOfferings.take(4).forEach { item ->
        OfferingGridButton(
          item = item,
          onClick = { onOfferingClick(item) },
          modifier = Modifier.weight(1f)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Row 2 (4 items)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      sampleOfferings.drop(4).take(4).forEach { item ->
        OfferingGridButton(
          item = item,
          onClick = { onOfferingClick(item) },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun OfferingGridButton(
  item: OfferingItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .clickable(onClick = onClick)
      .padding(horizontal = 2.dp)
      .testTag("offering_${item.id}"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = item.containerColor,
      modifier = Modifier.size(64.dp)
    ) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = item.icon,
          contentDescription = item.title,
          tint = item.iconColor,
          modifier = Modifier.size(32.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = item.title,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = RailOneDarkBlue,
      textAlign = TextAlign.Center,
      lineHeight = 15.sp,
      maxLines = 2
    )
  }
}
