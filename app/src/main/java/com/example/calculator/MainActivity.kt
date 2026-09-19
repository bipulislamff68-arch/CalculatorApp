package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {

    var display by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf(0.0) }
    var operator by remember { mutableStateOf("") }
    var newNumber by remember { mutableStateOf(true) }

    fun removeTrailingZero(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toInt().toString()
        } else {
            number.toString()
        }
    }

    fun calculate() {

        val secondNumber = display.toDoubleOrNull() ?: return

        val result = when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> {
                if (secondNumber == 0.0) {
                    display = "Error"
                    return
                }
                firstNumber / secondNumber
            }
            else -> secondNumber
        }

        display = removeTrailingZero(result)
        newNumber = true
        operator = ""
    }

    fun buttonClick(value: String) {

        when (value) {

            "C" -> {
                display = "0"
                firstNumber = 0.0
                operator = ""
                newNumber = true
            }

            "⌫" -> {
                if (display.length > 1) {
                    display = display.dropLast(1)
                } else {
                    display = "0"
                }
            }

            "+" , "-", "×", "÷" -> {

                firstNumber = display.toDoubleOrNull() ?: 0.0
                operator = value
                newNumber = true
            }

            "=" -> {
                if (operator.isNotEmpty()) {
                    calculate()
                }
            }

            "." -> {
                if (newNumber) {
                    display = "0."
                    newNumber = false
                } else if (!display.contains(".")) {
                    display += "."
                }
            }

            else -> {

                if (newNumber || display == "0" || display == "Error") {
                    display = value
                    newNumber = false
                } else {
                    display += value
                }
            }
        }
    }

    val buttons = listOf(
        listOf("C", "⌫", "÷", "×"),
        listOf("7", "8", "9", "-"),
        listOf("4", "5", "6", "+"),
        listOf("1", "2", "3", "="),
        listOf("0", ".", "", "")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101114))
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = "Calculator",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = display,
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        buttons.forEach { row ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                row.forEach { button ->

                    if (button.isNotEmpty()) {

                        val isOperator =
                            button in listOf("+", "-", "×", "÷", "=")

                        val isClear =
                            button == "C" || button == "⌫"

                        Button(
                            onClick = {
                                buttonClick(button)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    button == "=" ->
                                        Color(0xFF4CAF50)

                                    isOperator ->
                                        Color(0xFFFF9800)

                                    isClear ->
                                        Color(0xFF616161)

                                    else ->
                                        Color(0xFF24262B)
                                }
                            )
                        ) {

                            Text(
                                text = button,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                    } else {

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
