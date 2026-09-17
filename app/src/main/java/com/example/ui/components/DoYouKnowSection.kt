package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TriviaItem(
  val id: String,
  val title: String,
  val snippet: String,
  val illustrationType: Int, // 1: Vintage Steam Bridge, 2: Chenab Bridge, 3: Scenic Forest Rail
)

val sampleTrivia = listOf(
  TriviaItem(
    id = "steam_bridge",
    title = "First Train in India (1853)",
    snippet = "Ran between Bori Bunder (Mumbai) & Thane on April 16, 1853 with 400 passengers across 14 carriages.",
    illustrationType = 1
  ),
  TriviaItem(
    id = "chenab_bridge",
    title = "Chenab Rail Bridge",
    snippet = "World's highest railway bridge at 359m above the river bed, 35m higher than Paris's Eiffel Tower.",
    illustrationType = 2
  ),
  TriviaItem(
    id = "scenic_forest",
    title = "Darjeeling & Nilgiri Rails",
    snippet = "UNESCO World Heritage Mountain Railways celebrated for scenic zigzag reverses and lush loops.",
    illustrationType = 3
  )
)

@Composable
fun DoYouKnowSection(
  onTriviaClick: (TriviaItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp)
  ) {
    Text(
      text = "Do You know?",
      fontSize = 20.sp,
      fontWeight = FontWeight.ExtraBold,
      color = RailOneDarkBlue,
      modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )

    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(sampleTrivia, key = { it.id }) { trivia ->
        TriviaCard(
          trivia = trivia,
          onClick = { onTriviaClick(trivia) }
        )
      }
    }
  }
}

@Composable
fun TriviaCard(
  trivia: TriviaItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = modifier
      .width(230.dp)
      .height(230.dp)
      .clickable(onClick = onClick)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top visual canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp)
      ) {
        when (trivia.illustrationType) {
          1 -> VintageSteamBridgeCanvas()
          2 -> ChenabBridgeCanvas()
          else -> ScenicForestRailCanvas()
        }
      }

      // Bottom info
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp)
      ) {
        Text(
          text = trivia.title,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF263238),
          maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = trivia.snippet,
          fontSize = 10.5.sp,
          fontWeight = FontWeight.Normal,
          color = Color(0xFF546E7A),
          lineHeight = 13.sp,
          maxLines = 2
        )
      }
    }
  }
}

// 1. Vintage Black & White Steam Locomotive Bridge
@Composable
fun VintageSteamBridgeCanvas() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // Sepia / B&W vintage sky gradient
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0E0E0), Color(0xFFCCCCCC), Color(0xFFBDBDBD))
      )
    )

    // Billowing Steam Plumes
    drawCircle(
      color = Color(0xFFEEEEEE).copy(alpha = 0.85f),
      radius = w * 0.18f,
      center = Offset(w * 0.38f, h * 0.28f)
    )
    drawCircle(
      color = Color(0xFFE0E0E0).copy(alpha = 0.9f),
      radius = w * 0.15f,
      center = Offset(w * 0.26f, h * 0.35f)
    )
    drawCircle(
      color = Color(0xFFB0BEC5).copy(alpha = 0.7f),
      radius = w * 0.12f,
      center = Offset(w * 0.16f, h * 0.42f)
    )

    // Stone Arch Viaduct Bridge (Historical masonry)
    val bridgeTop = h * 0.62f
    drawRect(
      color = Color(0xFF424242),
      topLeft = Offset(0f, bridgeTop),
      size = Size(w, h - bridgeTop)
    )

    // 4 Classic stone arches
    val archWidth = w * 0.22f
    val gap = w * 0.03f
    for (i in 0..3) {
      val left = i * (archWidth + gap) + gap * 0.5f
      val archPath = Path().apply {
        moveTo(left, h)
        lineTo(left, bridgeTop + h * 0.15f)
        quadraticBezierTo(left + archWidth / 2f, bridgeTop + h * 0.02f, left + archWidth, bridgeTop + h * 0.15f)
        lineTo(left + archWidth, h)
        close()
      }
      drawPath(archPath, color = Color(0xFF616161))
    }

    // Vintage Steam Engine silhouette
    drawRoundRect(
      color = Color(0xFF212121),
      topLeft = Offset(w * 0.35f, bridgeTop - h * 0.22f),
      size = Size(w * 0.45f, h * 0.22f),
      cornerRadius = CornerRadius(4f, 4f)
    )
    // Chimney & Cowcatcher
    drawRect(
      color = Color(0xFF1B1B1B),
      topLeft = Offset(w * 0.4f, bridgeTop - h * 0.32f),
      size = Size(w * 0.08f, h * 0.12f)
    )
    // Driver Cabin
    drawRect(
      color = Color(0xFF333333),
      topLeft = Offset(w * 0.65f, bridgeTop - h * 0.28f),
      size = Size(w * 0.18f, h * 0.28f)
    )
  }
}

// 2. Chenab Rail Bridge (High arch over misty mountains)
@Composable
fun ChenabBridgeCanvas() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // Majestic blue Himalayan sky
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF29B6F6), Color(0xFF81D4FA), Color(0xFFE1F5FE))
      )
    )

    // Deep mountain peaks
    val mtn1 = Path().apply {
      moveTo(0f, h * 0.7f)
      lineTo(w * 0.3f, h * 0.2f)
      lineTo(w * 0.6f, h * 0.65f)
      lineTo(w, h * 0.35f)
      lineTo(w, h)
      lineTo(0f, h)
      close()
    }
    drawPath(mtn1, color = Color(0xFF1B5E20))

    val mtn2 = Path().apply {
      moveTo(w * 0.2f, h)
      lineTo(w * 0.55f, h * 0.35f)
      lineTo(w * 0.85f, h * 0.75f)
      lineTo(w, h * 0.5f)
      lineTo(w, h)
      close()
    }
    drawPath(mtn2, color = Color(0xFF2E7D32))

    // Dense white mist & cloud cover in gorge
    drawOval(
      color = Color.White.copy(alpha = 0.85f),
      topLeft = Offset(-w * 0.1f, h * 0.55f),
      size = Size(w * 0.7f, h * 0.35f)
    )
    drawOval(
      color = Color.White.copy(alpha = 0.9f),
      topLeft = Offset(w * 0.35f, h * 0.52f),
      size = Size(w * 0.8f, h * 0.38f)
    )

    // World's highest steel arch bridge
    val bridgeY = h * 0.48f
    drawLine(
      color = Color(0xFFECEFF1),
      start = Offset(w * 0.1f, bridgeY),
      end = Offset(w * 0.9f, bridgeY),
      strokeWidth = 6f
    )

    // Massive steel arch below track
    val arch = Path().apply {
      moveTo(w * 0.22f, h * 0.72f)
      quadraticBezierTo(w * 0.5f, bridgeY + 8f, w * 0.78f, h * 0.72f)
    }
    drawPath(arch, color = Color(0xFFCFD8DC), style = Stroke(width = 8f))

    // Vertical bridge pylons
    for (i in 1..4) {
      val x = w * (0.28f + i * 0.09f)
      drawLine(
        color = Color(0xFFB0BEC5),
        start = Offset(x, bridgeY),
        end = Offset(x, bridgeY + h * 0.15f),
        strokeWidth = 3f
      )
    }
  }
}

// 3. Scenic Forest Mountain Rail
@Composable
fun ScenicForestRailCanvas() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // Lush Emerald Forest
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF80CBC4), Color(0xFF4DB6AC), Color(0xFF00695C))
      )
    )

    // Curving railway track through trees
    val trackPath = Path().apply {
      moveTo(w * 0.1f, h)
      cubicTo(w * 0.3f, h * 0.7f, w * 0.5f, h * 0.45f, w * 0.85f, h * 0.2f)
    }
    drawPath(trackPath, color = Color(0xFFFFD54F), style = Stroke(width = 8f))

    // Pine trees silhouettes along the curves
    for (i in 0..5) {
      val treeX = w * (0.08f + i * 0.16f)
      val treeY = h * (0.35f + (i % 3) * 0.15f)
      val treePath = Path().apply {
        moveTo(treeX, treeY)
        lineTo(treeX - w * 0.07f, treeY + h * 0.18f)
        lineTo(treeX + w * 0.07f, treeY + h * 0.18f)
        close()
      }
      drawPath(treePath, color = Color(0xFF004D40))
    }
  }
}
