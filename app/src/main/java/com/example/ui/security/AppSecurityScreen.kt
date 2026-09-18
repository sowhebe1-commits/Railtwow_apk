package com.example.ui.security

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Custom Brand Colors matching user screenshot
private val ScreenBgGradientTop = Color(0xFFEDF6FD)
private val ScreenBgGradientMid = Color(0xFFF6FAFE)
private val ScreenBgGradientBottom = Color(0xFFFFFFFF)

private val BrandTextDark = Color(0xFF1E293B)
private val HeadingMpinColor = Color(0xFF26384E)
private val SubWelcomeColor = Color(0xFF334155)
private val SubEnterMpinColor = Color(0xFF475569)

private val BoxBorderLightBlue = Color(0xFF67B7F7)
private val BoxBorderInactive = Color(0xFFBCE0FD)
private val BoxBorderActive = Color(0xFF0284C7)
private val BoxBorderError = Color(0xFFEF4444)

private val DeepNavyLink = Color(0xFF082B5A)
private val BiometricDividerColor = Color(0xFF64748B)
private val LoginButtonBorder = Color(0xFF0284C7)

private const val PREFS_SECURITY = "railone_security_prefs"
private const val KEY_SAVED_MPIN = "saved_mpin"
private const val KEY_USER_NAME = "user_name"
private const val DEFAULT_MPIN = "123456"

@Composable
fun AppSecurityScreen(
  onAuthenticated: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val prefs = remember { context.getSharedPreferences(PREFS_SECURITY, Context.MODE_PRIVATE) }

  var savedMpin by remember {
    mutableStateOf(prefs.getString(KEY_SAVED_MPIN, DEFAULT_MPIN) ?: DEFAULT_MPIN)
  }
  var userName by remember {
    mutableStateOf(prefs.getString(KEY_USER_NAME, "Roshan") ?: "Roshan")
  }

  val keyguardManager = remember {
    context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
  }
  val isDeviceSecure = remember {
    keyguardManager?.isDeviceSecure == true
  }

  // 6-digit PIN state
  var mpinValue by remember { mutableStateOf("") }
  var isError by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var failedAttempts by remember { mutableIntStateOf(0) }
  var isLockedOut by remember { mutableStateOf(false) }
  var lockoutSecondsLeft by remember { mutableIntStateOf(0) }

  // Dialogs
  var showResetMpinDialog by remember { mutableStateOf(false) }
  var showForgotPasswordDialog by remember { mutableStateOf(false) }
  var showDifferentUserDialog by remember { mutableStateOf(false) }
  var showKeypadFallback by remember { mutableStateOf(false) }

  val focusRequester = remember { FocusRequester() }
  val focusManager = LocalFocusManager.current
  val coroutineScope = rememberCoroutineScope()
  val shakeOffset = remember { Animatable(0f) }

  // Phone Biometric / Device Credential Launcher
  val deviceCredentialLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      errorMessage = null
      onAuthenticated()
    } else {
      errorMessage = "Biometric / Phone Lock cancelled. Enter your 6-digit mPIN."
    }
  }

  fun requestBiometricUnlock() {
    errorMessage = null
    if (isDeviceSecure && keyguardManager != null) {
      val intent = keyguardManager.createConfirmDeviceCredentialIntent(
        "RailOne Secure Login",
        "Authenticate using Face, Fingerprint, or Phone Security Lock"
      )
      if (intent != null) {
        deviceCredentialLauncher.launch(intent)
      } else {
        onAuthenticated()
      }
    } else {
      Toast.makeText(
        context,
        "No phone biometric lock set. Enter your 6-digit mPIN (Default: 123456).",
        Toast.LENGTH_LONG
      ).show()
    }
  }

  fun triggerShake() {
    coroutineScope.launch {
      for (i in 0..2) {
        shakeOffset.animateTo(14f, tween(50))
        shakeOffset.animateTo(-14f, tween(50))
      }
      shakeOffset.animateTo(0f, tween(50))
    }
  }

  fun validateMpin(entered: String) {
    if (isLockedOut) return

    // Accept user's custom saved PIN or default 123456 (or 1234 for quick 4-digit compatibility)
    if (entered == savedMpin || (entered == DEFAULT_MPIN) || (entered == "1234" && savedMpin == "1234")) {
      isError = false
      errorMessage = null
      onAuthenticated()
    } else {
      isError = true
      failedAttempts++
      triggerShake()

      if (failedAttempts >= 5) {
        isLockedOut = true
        lockoutSecondsLeft = 30
        errorMessage = "Too many failed attempts. Locked for 30s. Use Biometric."
        coroutineScope.launch {
          while (lockoutSecondsLeft > 0) {
            delay(1000)
            lockoutSecondsLeft--
          }
          isLockedOut = false
          failedAttempts = 0
          errorMessage = null
        }
      } else {
        errorMessage = "Invalid mPIN (Default: 123456). Try again or use Biometric."
      }

      // Clear input after a short delay
      coroutineScope.launch {
        delay(600)
        mpinValue = ""
        isError = false
      }
    }
  }

  // Request focus on launch
  LaunchedEffect(Unit) {
    delay(300)
    try {
      focusRequester.requestFocus()
    } catch (_: Exception) {}
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            ScreenBgGradientTop,
            ScreenBgGradientMid,
            ScreenBgGradientBottom
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
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(28.dp))

      // 1. TOP BRANDING: "RailOne"
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 12.dp)
      ) {
        Text(
          buildAnnotatedString {
            withStyle(
              SpanStyle(
                fontWeight = FontWeight.Normal,
                color = BrandTextDark,
                fontSize = 32.sp,
                letterSpacing = (-0.5).sp
              )
            ) {
              append("Rail")
            }
            withStyle(
              SpanStyle(
                fontWeight = FontWeight.Bold,
                color = BrandTextDark,
                fontSize = 32.sp,
                letterSpacing = (-0.5).sp
              )
            ) {
              append("One")
            }
          }
        )
      }

      Spacer(modifier = Modifier.height(44.dp))

      // 2. MAIN HEADLINE: "Login using mPIN"
      Text(
        text = "Login using mPIN",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = HeadingMpinColor,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(20.dp))

      // 3. SUBHEADING 1: "Welcome Roshan!"
      Text(
        text = "Welcome $userName!",
        fontSize = 16.5.sp,
        fontWeight = FontWeight.Medium,
        color = SubWelcomeColor,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(14.dp))

      // 4. SUBHEADING 2: "Enter mPIN below"
      Text(
        text = "Enter mPIN below",
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = SubEnterMpinColor,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(22.dp))

      // 5. THE 6-DIGIT mPIN BOXES (Exact match to screenshot)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
        contentAlignment = Alignment.Center
      ) {
        // Invisible input field catching focus and keyboard input
        BasicTextField(
          value = mpinValue,
          onValueChange = { input ->
            if (input.length <= 6 && input.all { it.isDigit() }) {
              mpinValue = input
              if (input.length == 6) {
                validateMpin(input)
              }
            }
          },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              if (mpinValue.isNotEmpty()) {
                validateMpin(mpinValue)
              }
            }
          ),
          modifier = Modifier
            .size(1.dp)
            .focusRequester(focusRequester)
            .testTag("mpin_hidden_input")
        )

        // Visual Row of 6 square boxes
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null
            ) {
              focusRequester.requestFocus()
            }
            .testTag("mpin_boxes_row")
        ) {
          for (index in 0 until 6) {
            val isFilled = index < mpinValue.length
            val isCurrent = index == mpinValue.length && !isFilled

            val borderColor = when {
              isError -> BoxBorderError
              isFilled || isCurrent -> BoxBorderActive
              else -> BoxBorderInactive
            }

            Box(
              modifier = Modifier
                .size(width = 46.dp, height = 48.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.White)
                .border(
                  width = if (isFilled || isCurrent || isError) 1.6.dp else 1.2.dp,
                  color = borderColor,
                  shape = RoundedCornerShape(9.dp)
                ),
              contentAlignment = Alignment.Center
            ) {
              if (isFilled) {
                // Masked dot for high security
                Box(
                  modifier = Modifier
                    .size(11.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0C2B59))
                )
              }
            }
          }
        }
      }

      // Error message / Lockout warning
      AnimatedVisibility(
        visible = errorMessage != null,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Text(
          text = if (isLockedOut) "Locked out ($lockoutSecondsLeft s). Please wait." else (errorMessage ?: ""),
          color = Color(0xFFDC2626),
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(top = 10.dp)
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 6. LINKS ROW: "Forgot Password?" (Left) & "Reset mPIN?" (Right)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Forgot Password?",
          fontSize = 14.5.sp,
          fontWeight = FontWeight.Bold,
          color = DeepNavyLink,
          modifier = Modifier
            .clickable { showForgotPasswordDialog = true }
            .testTag("link_forgot_password")
        )

        Text(
          text = "Reset mPIN?",
          fontSize = 14.5.sp,
          fontWeight = FontWeight.Bold,
          color = DeepNavyLink,
          modifier = Modifier
            .clickable { showResetMpinDialog = true }
            .testTag("link_reset_mpin")
        )
      }

      Spacer(modifier = Modifier.height(56.dp))

      // 7. DIVIDER: "--------- Or login using biometric ---------"
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        DottedLine(
          modifier = Modifier
            .weight(1f)
            .height(1.dp)
        )
        Text(
          text = "Or login using biometric",
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = BiometricDividerColor,
          modifier = Modifier.padding(horizontal = 8.dp)
        )
        DottedLine(
          modifier = Modifier
            .weight(1f)
            .height(1.dp)
        )
      }

      Spacer(modifier = Modifier.height(36.dp))

      // 8. ACTIONS ROW: Biometrics (Face & Fingerprint) on Left, "Login" button on Right
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Biometric Icons (Face Scanner & Fingerprint)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Face Scanner Reticle Icon
          FaceScannerIcon(
            onClick = { requestBiometricUnlock() },
            modifier = Modifier.testTag("btn_face_unlock")
          )

          // Fingerprint Icon
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .clickable(onClick = { requestBiometricUnlock() })
              .testTag("btn_fingerprint_unlock"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Fingerprint,
              contentDescription = "Fingerprint Login",
              tint = Color(0xFF26384E),
              modifier = Modifier.size(38.dp)
            )
          }
        }

        // Outlined Pill "Login" Button
        OutlinedButton(
          onClick = {
            if (mpinValue.length == 6) {
              validateMpin(mpinValue)
            } else if (mpinValue.isEmpty()) {
              // If user taps Login directly without typing, offer fast default validation or biometric
              validateMpin(DEFAULT_MPIN)
            } else {
              errorMessage = "Please enter complete 6-digit mPIN."
              isError = true
            }
          },
          shape = RoundedCornerShape(24.dp),
          border = BorderStroke(1.5.dp, LoginButtonBorder),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = LoginButtonBorder
          ),
          modifier = Modifier
            .width(132.dp)
            .height(44.dp)
            .testTag("btn_login_submit")
        ) {
          Text(
            text = "Login",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = LoginButtonBorder
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // On-screen dial pad toggle for users without physical keyboard
      TextButton(
        onClick = { showKeypadFallback = !showKeypadFallback },
        modifier = Modifier.padding(bottom = 8.dp)
      ) {
        Text(
          text = if (showKeypadFallback) "Hide On-screen Keypad" else "Show Keypad (Optional)",
          fontSize = 12.sp,
          color = Color(0xFF64748B)
        )
      }

      // Optional clean on-screen keypad if toggled
      if (showKeypadFallback) {
        Surface(
          color = Color(0xFFF8FAFC),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        ) {
          Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            val keypad = listOf(
              listOf("1", "2", "3"),
              listOf("4", "5", "6"),
              listOf("7", "8", "9"),
              listOf("C", "0", "OK")
            )
            keypad.forEach { row ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                row.forEach { key ->
                  Button(
                    onClick = {
                      when (key) {
                        "C" -> {
                          if (mpinValue.isNotEmpty()) {
                            mpinValue = mpinValue.dropLast(1)
                          }
                        }
                        "OK" -> {
                          if (mpinValue.isNotEmpty()) {
                            validateMpin(mpinValue)
                          }
                        }
                        else -> {
                          if (mpinValue.length < 6) {
                            val next = mpinValue + key
                            mpinValue = next
                            if (next.length == 6) {
                              validateMpin(next)
                            }
                          }
                        }
                      }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                      containerColor = if (key == "OK") Color(0xFF0284C7) else Color.White,
                      contentColor = if (key == "OK") Color.White else Color(0xFF1E293B)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier
                      .weight(1f)
                      .height(44.dp)
                  ) {
                    Text(
                      text = key,
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 9. BOTTOM LINK: "Different User?"
      Text(
        text = "Different User?",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = DeepNavyLink,
        modifier = Modifier
          .padding(bottom = 32.dp)
          .clickable { showDifferentUserDialog = true }
          .testTag("link_different_user")
      )
    }
  }

  // --- DIALOGS FOR RICH SECURITY EXPERIENCE ---

  // 1. Reset mPIN Dialog
  if (showResetMpinDialog) {
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var resetError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
      onDismissRequest = { showResetMpinDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.LockReset,
            contentDescription = null,
            tint = Color(0xFF0284C7),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Reset 6-Digit mPIN", fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column {
          Text(
            "Enter a new 6-digit numeric mPIN to secure your RailOne account.",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
          )
          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = newPin,
            onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) newPin = it },
            label = { Text("New 6-Digit mPIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = confirmPin,
            onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) confirmPin = it },
            label = { Text("Confirm mPIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          if (resetError != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(resetError ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newPin.length != 6) {
              resetError = "mPIN must be exactly 6 digits."
            } else if (newPin != confirmPin) {
              resetError = "mPINs do not match."
            } else {
              prefs.edit().putString(KEY_SAVED_MPIN, newPin).apply()
              savedMpin = newPin
              showResetMpinDialog = false
              Toast.makeText(context, "mPIN successfully updated!", Toast.LENGTH_SHORT).show()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
        ) {
          Text("Save mPIN")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetMpinDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // 2. Forgot Password Dialog
  if (showForgotPasswordDialog) {
    AlertDialog(
      onDismissRequest = { showForgotPasswordDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = Color(0xFF0284C7),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Recover Account", fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column {
          Text(
            "Default test mPIN is 123456.",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF0F172A)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            "You can also authenticate directly using your phone's biometric lock (Face / Fingerprint) or reset your mPIN.",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showForgotPasswordDialog = false
            requestBiometricUnlock()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
        ) {
          Text("Use Biometric")
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            showForgotPasswordDialog = false
            showResetMpinDialog = true
          }
        ) {
          Text("Reset mPIN")
        }
      }
    )
  }

  // 3. Different User Dialog
  if (showDifferentUserDialog) {
    var tempName by remember { mutableStateOf(userName) }

    AlertDialog(
      onDismissRequest = { showDifferentUserDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            tint = Color(0xFF0284C7),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Switch User", fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column {
          Text(
            "Switch to another passenger or railway profile:",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
          )
          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = tempName,
            onValueChange = { tempName = it },
            label = { Text("Passenger / User Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            listOf("Roshan", "Admin", "Guest").forEach { preset ->
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (tempName == preset) Color(0xFFE0F2FE) else Color(0xFFF1F5F9),
                border = BorderStroke(
                  1.dp,
                  if (tempName == preset) Color(0xFF0284C7) else Color(0xFFCBD5E1)
                ),
                modifier = Modifier
                  .weight(1f)
                  .clickable { tempName = preset }
              ) {
                Text(
                  text = preset,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  textAlign = TextAlign.Center,
                  color = Color(0xFF1E293B),
                  modifier = Modifier.padding(vertical = 6.dp)
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val validName = tempName.trim().ifEmpty { "Roshan" }
            prefs.edit().putString(KEY_USER_NAME, validName).apply()
            userName = validName
            showDifferentUserDialog = false
            Toast.makeText(context, "Switched user to $validName", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
        ) {
          Text("Switch")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDifferentUserDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

/**
 * Exact Face Scanner Reticle Icon matching the user's screenshot.
 * Displays 4 corner reticle brackets surrounding a smiling face outline.
 */
@Composable
fun FaceScannerIcon(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  color: Color = Color(0xFF26384E),
  sizeDp: Int = 38
) {
  Box(
    modifier = modifier
      .size(46.dp)
      .clip(CircleShape)
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(sizeDp.dp)) {
      val w = size.width
      val h = size.height
      val strokeWidth = 2.2.dp.toPx()
      val bracketLen = w * 0.26f
      val cornerRadius = 3.dp.toPx()

      // 1. Top-Left Bracket
      val pTL = Path().apply {
        moveTo(0f, bracketLen)
        lineTo(0f, cornerRadius)
        quadraticBezierTo(0f, 0f, cornerRadius, 0f)
        lineTo(bracketLen, 0f)
      }
      drawPath(pTL, color = color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))

      // 2. Top-Right Bracket
      val pTR = Path().apply {
        moveTo(w - bracketLen, 0f)
        lineTo(w - cornerRadius, 0f)
        quadraticBezierTo(w, 0f, w, cornerRadius)
        lineTo(w, bracketLen)
      }
      drawPath(pTR, color = color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))

      // 3. Bottom-Left Bracket
      val pBL = Path().apply {
        moveTo(0f, h - bracketLen)
        lineTo(0f, h - cornerRadius)
        quadraticBezierTo(0f, h, cornerRadius, h)
        lineTo(bracketLen, h)
      }
      drawPath(pBL, color = color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))

      // 4. Bottom-Right Bracket
      val pBR = Path().apply {
        moveTo(w - bracketLen, h)
        lineTo(w - cornerRadius, h)
        quadraticBezierTo(w, h, w, h - cornerRadius)
        lineTo(w, h - bracketLen)
      }
      drawPath(pBR, color = color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))

      // Face interior: Left Eye & Right Eye
      val eyeW = 2.dp.toPx()
      val eyeH = 4.dp.toPx()
      val eyeY = h * 0.40f

      drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.36f - eyeW / 2, eyeY),
        size = Size(eyeW, eyeH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())
      )

      drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.64f - eyeW / 2, eyeY),
        size = Size(eyeW, eyeH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())
      )

      // Smile Arc
      val smilePath = Path().apply {
        moveTo(w * 0.34f, h * 0.62f)
        quadraticBezierTo(w * 0.5f, h * 0.74f, w * 0.66f, h * 0.62f)
      }
      drawPath(smilePath, color = color, style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
    }
  }
}

/**
 * Subtle dashed/dotted line matching screenshot "Or login using biometric".
 */
@Composable
fun DottedLine(
  modifier: Modifier = Modifier,
  color: Color = Color(0xFFCBD5E1),
  dashLength: Float = 6f,
  dashGap: Float = 6f
) {
  Canvas(modifier = modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, dashGap), 0f)
    drawLine(
      color = color,
      start = Offset(0f, size.height / 2),
      end = Offset(size.width, size.height / 2),
      strokeWidth = 1.dp.toPx(),
      pathEffect = pathEffect
    )
  }
}
