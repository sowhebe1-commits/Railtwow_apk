package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.RailOneScreen
import com.example.ui.security.AppSecurityScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var isUnlocked by rememberSaveable { mutableStateOf(false) }

        if (!isUnlocked) {
          AppSecurityScreen(
            onAuthenticated = {
              isUnlocked = true
            },
            modifier = Modifier.fillMaxSize()
          )
        } else {
          RailOneScreen(
            onLockApp = {
              isUnlocked = false
            },
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }
  }
}

// Maintained for Robolectric and Screenshot test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun RailOnePreview() {
  MyApplicationTheme {
    RailOneScreen()
  }
}
