package com.vymo.leadapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vymo.leadapp.lead.ui.LeadFormScreen
import com.vymo.leadapp.ui.theme.LeadappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeadappTheme {
                LeadFormScreen()
            }
        }
    }
}