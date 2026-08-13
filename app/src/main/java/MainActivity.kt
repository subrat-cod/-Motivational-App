package com.example.myfirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MotivationalAppScreen()
                }
            }
        }
    }
}

// --- Motivational Message Generator Function ---
fun getMotivationalMessageForUser(mobileNumber: String): String {
    return "Hello! Number (+91 $mobileNumber): Haari jiba katha nahein! Bada developer seye nuhe jebe error asena, bada developer seye bae jeye error face kariki taaku thik kare. Keep coding, you've got this! 💪"
}

// --- Composable UI for Number & Motivation Trigger ---
@Composable
fun MotivationalAppScreen() {
    var mobileNumber by remember { mutableStateOf("") }
    var displayedMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Anunaya Motivation Portal",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Enter Mobile Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                val user = hashMapOf(
                    "mobileNumber" to mobileNumber
                )
                db.collection("motivations")
                    .add(user)
                    .addOnSuccessListener {
                        displayedMessage = "Motivation Saved Successfully!"
                    }
                    .addOnFailureListener {
                        displayedMessage = "Error saving data"
                    }
                if (mobileNumber.length == 10) {
                    displayedMessage = getMotivationalMessageForUser(mobileNumber)
                } else {
                    displayedMessage = "Please enter a valid 10-digit mobile number!"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Send Motivation Message")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (displayedMessage.isNotBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = displayedMessage,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}