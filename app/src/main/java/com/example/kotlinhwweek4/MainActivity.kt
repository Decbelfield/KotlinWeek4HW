package com.example.kotlinhwweek4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GPACalculatorApp()
        }
    }
}

@Composable
fun GPACalculatorApp() {
    var grades = remember { mutableStateListOf("", "", "", "", "") }
    var gpa by remember { mutableDoubleStateOf(0.0) }
    var buttonText by remember { mutableStateOf("Compute GPA") }
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    var isValidInput by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GPA Calculator",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields
        grades.forEachIndexed { index, grade ->
            TextField(
                value = grade,
                onValueChange = { value ->
                    grades[index] = value
                    if (buttonText == "Clear Form") {
                        buttonText = "Compute GPA"
                    }
                },
                label = { Text("Grade for Course ${index + 1}") },
                isError = grade.isNotEmpty() && (grade.toIntOrNull() == null || grade.toInt() !in 0..100),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compute GPA & Clear Form Button
        Button(
            onClick = {
                if (buttonText == "Compute GPA") {
                    if (grades.all { it.isNotEmpty() && it.toIntOrNull() != null && it.toInt() in 0..100 }) {
                        gpa = grades.map { it.toInt() }.average()
                        backgroundColor = when {
                            gpa < 60 -> Color.Red
                            gpa in 61.0..79.0 -> Color.Yellow
                            else -> Color.Green
                        }
                        buttonText = "Clear Form"
                        isValidInput = true
                    } else {
                        isValidInput = false
                    }
                } else {
                    // Clear Form
                    grades = mutableStateListOf("", "", "", "", "")
                    gpa = 0.0
                    backgroundColor = Color.Transparent
                    buttonText = "Compute GPA"
                    isValidInput = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(buttonText)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display GPA
        if (buttonText == "Clear Form") {
            Text(
                text = "GPA: %.2f".format(gpa),
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        //error message if input is invalid
        if (!isValidInput) {
            Text(
                text = "Please enter valid grades (0-100).",
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GPACalculatorApp()
}
