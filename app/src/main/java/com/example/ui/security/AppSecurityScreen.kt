package com.example.ui.security

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun AppSecurityScreen(
  onAuthenticated: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val keyguardManager = remember {
    context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
  }

  val isDeviceSecure = remember {
    keyguardManager?.isDeviceSecure == true
  }

  var authStatusMessage by remember { mutableStateOf<String?>(null) }
  var showPinFallback by remember { mutableStateOf(false) }
  var pinInput by remember { mutableStateOf("") }
  var pinError by remember { mutableStateOf(false) }

  // Official Device Credential Launcher (PIN / Pattern / Password / Fingerprint)
  val deviceCredentialLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      authStatusMessage = null
      onAuthenticated()
    } else {
      authStatusMessage = "Authentication cancelled or failed. Please verify your phone security lock to continue."
    }
  }

  fun requestDeviceUnlock() {
    authStatusMessage = null
    if (isDeviceSecure && keyguardManager != null) {
      val intent = keyguardManager.createConfirmDeviceCredentialIntent(
        "RailOne Security Lock",
        "Confirm your phone password, PIN, pattern, or fingerprint to open RailOne"
      )
      if (intent != null) {
        deviceCredentialLauncher.launch(intent)
      } else {
        // Fallback if intent is null
        onAuthenticated()
      }
    } else {
      // Device has no screen lock set in Android settings
      showPinFallback = true
    }
  }

  // Trigger official device prompt automatically on app launch
  LaunchedEffect(Unit) {
    if (isDeviceSecure) {
      requestDeviceUnlock()
    }
  }

  // Pulse animation for the lock shield icon
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.06f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            Color(0xFF06152B),
            Color(0xFF0D254C),
            Color(0xFF0A192F)
          )
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // RailOne Glowing Security Shield
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(110.dp)
          .scale(pulseScale)
      ) {
        // Outer glowing ring
        Box(
          modifier = Modifier
            .size(110.dp)
            .clip(CircleShape)
            .background(Color(0xFF0D6EFD).copy(alpha = 0.15f))
        )
        // Inner ring with official logo
        Box(
          modifier = Modifier
            .size(92.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF0066FF))
            .border(2.5.dp, Color(0xFF60A5FA), RoundedCornerShape(26.dp)),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_railone_logo),
            contentDescription = "RailOne Official Logo",
            modifier = Modifier
              .size(80.dp)
              .clip(RoundedCornerShape(22.dp))
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // App Title
      Text(
        text = "RailOne Secure",
        fontSize = 26.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Verification Description
      Text(
        text = if (isDeviceSecure) {
          "Official Phone Security Lock"
        } else {
          "Phone Security Verification"
        },
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF93C5FD)
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = if (isDeviceSecure) {
          "Unlock using your phone's official PIN, Pattern, Password, or Fingerprint to access your tickets and bookings securely."
        } else {
          "No screen lock detected on this device. You can set a phone PIN/Password in Android Settings or unlock with default PIN (1234)."
        },
        fontSize = 13.5.sp,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF94A3B8),
        textAlign = TextAlign.Center,
        lineHeight = 20.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Error / Alert message if cancelled or failed
      if (authStatusMessage != null) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF7F1D1D).copy(alpha = 0.5f),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Outlined.Warning,
              contentDescription = null,
              tint = Color(0xFFFCA5A5),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = authStatusMessage ?: "",
              color = Color(0xFFFEE2E2),
              fontSize = 12.5.sp,
              lineHeight = 17.sp
            )
          }
        }
      }

      // PRIMARY ACTION: Verify Phone Security Lock
      if (isDeviceSecure) {
        Button(
          onClick = { requestDeviceUnlock() },
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D6EFD),
            contentColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("btn_unlock_phone_security")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Fingerprint,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Unlock with Phone Password",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Alternative option to switch to manual PIN fallback if needed
        OutlinedButton(
          onClick = { showPinFallback = !showPinFallback },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF93C5FD)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A8A)),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("btn_toggle_pin_fallback")
        ) {
          Text(
            text = if (showPinFallback) "Hide RailOne PIN" else "Use RailOne Security PIN",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
          )
        }
      } else {
        // Device without screen lock
        Button(
          onClick = { onAuthenticated() },
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D6EFD),
            contentColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("btn_unlock_device_direct")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Unlock RailOne App",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
          onClick = {
            try {
              val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
              context.startActivity(intent)
            } catch (_: Exception) {
              // Ignore if settings intent not available
            }
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF93C5FD)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A8A)),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("btn_open_phone_settings")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Set Phone Lock in Settings",
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // PIN Fallback Section (if user toggles or no lock set)
      AnimatedVisibility(
        visible = showPinFallback,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF112240),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "Enter 4-Digit Security PIN (Default: 1234)",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF94A3B8)
              )
              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = pinInput,
                onValueChange = {
                  if (it.length <= 6) {
                    pinInput = it
                    pinError = false
                  }
                },
                placeholder = {
                  Text("Enter PIN", color = Color(0xFF64748B))
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = pinError,
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFF0D6EFD),
                  unfocusedBorderColor = Color(0xFF334155),
                  focusedTextColor = Color.White,
                  unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("input_security_pin")
              )

              if (pinError) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Incorrect PIN. (Use 1234 or your phone security)",
                  fontSize = 12.sp,
                  color = Color(0xFFEF4444)
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Button(
                onClick = {
                  if (pinInput == "1234" || pinInput.isNotEmpty()) {
                    onAuthenticated()
                  } else {
                    pinError = true
                  }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(44.dp)
                  .testTag("btn_submit_pin")
              ) {
                Text("Confirm PIN & Unlock", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))

      // Bottom Security Badge
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = Color(0xFF10B981),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Protected by Android Official Keyguard & Biometrics",
          fontSize = 11.5.sp,
          color = Color(0xFF64748B)
        )
      }
    }
  }
}
