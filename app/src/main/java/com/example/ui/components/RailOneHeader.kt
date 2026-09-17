package com.example.ui.components

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RailBlueDark
import com.example.ui.theme.RailBluePrimary

@Composable
fun RailOneHeader(
  transactionId: String,
  passengerGreeting: String,
  onBackClick: () -> Unit,
  onPdfClick: () -> Unit,
  onEmailClick: () -> Unit,
  onShareClick: () -> Unit,
  onEditClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(RailBluePrimary, RailBlueDark)
        )
      )
  ) {
    // Top Bar with Status Bar padding
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Circular back button matching the screenshot
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.15f))
          .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
          .clickable(onClick = onBackClick)
          .testTag("back_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Ticket Details",
          color = Color.White,
          fontSize = 19.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Transaction Id: $transactionId",
          color = Color.White.copy(alpha = 0.88f),
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Normal
        )
      }

      // 3 dots overflow menu on top right containing Edit Ticket option
      Box {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            .clickable(onClick = { menuExpanded = true })
            .testTag("ticket_overflow_menu_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More options",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }

        DropdownMenu(
          expanded = menuExpanded,
          onDismissRequest = { menuExpanded = false },
          modifier = Modifier
            .background(Color.White)
            .testTag("ticket_dropdown_menu")
        ) {
          DropdownMenuItem(
            text = {
              Text(
                text = "Edit Ticket Details",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = RailBluePrimary,
                modifier = Modifier.size(20.dp)
              )
            },
            onClick = {
              menuExpanded = false
              onEditClick()
            },
            modifier = Modifier.testTag("menu_item_edit_ticket")
          )
          DropdownMenuItem(
            text = {
              Text(
                text = "Download e-Ticket (PDF)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B)
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Outlined.PictureAsPdf,
                contentDescription = null,
                tint = RailBluePrimary,
                modifier = Modifier.size(20.dp)
              )
            },
            onClick = {
              menuExpanded = false
              onPdfClick()
            }
          )
          DropdownMenuItem(
            text = {
              Text(
                text = "Send to Email",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B)
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null,
                tint = RailBluePrimary,
                modifier = Modifier.size(20.dp)
              )
            },
            onClick = {
              menuExpanded = false
              onEmailClick()
            }
          )
          DropdownMenuItem(
            text = {
              Text(
                text = "Share Journey",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E293B)
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = null,
                tint = RailBluePrimary,
                modifier = Modifier.size(20.dp)
              )
            },
            onClick = {
              menuExpanded = false
              onShareClick()
            }
          )
        }
      }
    }

    // Subheader greeting bar on white/light surface
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "Thank You $passengerGreeting, Happy Journey !",
        color = Color(0xFF1E293B),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.weight(1f)
      )

      // Icons: PDF, Mail, Share
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // PDF Icon
        IconButton(
          onClick = onPdfClick,
          modifier = Modifier
            .size(38.dp)
            .testTag("pdf_button")
        ) {
          Icon(
            imageVector = Icons.Outlined.PictureAsPdf,
            contentDescription = "Download Ticket PDF",
            tint = Color(0xFF0D59EC),
            modifier = Modifier.size(22.dp)
          )
        }

        // Email Icon
        IconButton(
          onClick = onEmailClick,
          modifier = Modifier
            .size(38.dp)
            .testTag("email_button")
        ) {
          Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = "Email Ticket",
            tint = Color(0xFF0D59EC),
            modifier = Modifier.size(22.dp)
          )
        }

        // Share Icon
        IconButton(
          onClick = onShareClick,
          modifier = Modifier
            .size(38.dp)
            .testTag("share_button")
        ) {
          Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = "Share Ticket",
            tint = Color(0xFF0D59EC),
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }
  }
}
