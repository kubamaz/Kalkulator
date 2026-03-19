package com.example.myapplication

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import net.objecthunter.exp4j.ExpressionBuilder
@Composable
fun SimpleCalcScreen(navController: NavController, padding: PaddingValues) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var currentInput by remember { mutableStateOf("") }
    var historyText by remember { mutableStateOf("") }

    val handleButtonClick = remember {
        { label: String ->
            when (label) {
                "AC" -> {
                    currentInput = ""
                    historyText = ""
                }
                "+", "-", "*", "/" -> {
                    if (currentInput.isNotEmpty() && historyText.isNotEmpty()) {
                        try {
                            val fullExpression = historyText + currentInput
                            val eval = ExpressionBuilder(fullExpression).build()
                            val intermediateResult = eval.evaluate().toString().removeSuffix(".0")

                            historyText = "$intermediateResult $label "
                            currentInput = ""
                        } catch (e: Exception) {
                            currentInput = "Błąd"
                        }
                    } else if (currentInput.isNotEmpty()) {
                        historyText = "$currentInput $label "
                        currentInput = ""
                    } else if (historyText.isNotEmpty()) {
                        historyText = historyText.dropLast(3) + " $label "
                    }
                }
                "+/-" -> {
                    val numericValue = currentInput.toDoubleOrNull()
                    if (numericValue != null && numericValue != 0.0) {
                        currentInput = if (currentInput.startsWith("-")) {
                            currentInput.removePrefix("-")
                        }
                        else {
                            "-$currentInput"
                        }
                    }
                }
                "C/CE" -> if (currentInput.isNotEmpty()) currentInput = currentInput.dropLast(1)
                "=" -> {
                    if (currentInput.isNotEmpty() && historyText.isNotEmpty()) {
                        val fullExpression = historyText + currentInput
                        try {
                            val eval = ExpressionBuilder(fullExpression).build()
                            val result = eval.evaluate()
                            currentInput = result.toString().removeSuffix(".0")
                            historyText = ""
                        } catch (e: Exception) {
                            currentInput = "Błąd"
                        }
                    }
                }
                else -> currentInput += label
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        if (isLandscape) {
            val buttons = listOf("7", "8", "9", "C/CE", "AC", "4", "5", "6", "*", "/", "1", "2", "3", "+", "-", "0", "00", ".", "+/-", "=")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Powrót",
                        modifier = Modifier.size(32.dp)
                    )
                }
                ResultDisplay(currentInput,historyText, modifier = Modifier.weight(1f), true)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.weight(3f)) {
                CalculatorGrid(buttons, true, handleButtonClick)
            }
        } else {
            val buttons = listOf("AC", "C/CE", "+/-", "/", "7", "8", "9", "*", "4", "5", "6", "-", "1", "2", "3", "+", "0","00", ".", "=")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Powrót",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            ResultDisplay(currentInput,historyText, modifier = Modifier.weight(1f), false)
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier.wrapContentHeight()) {
                CalculatorGrid(buttons, false, handleButtonClick)
            }
        }
    }

}