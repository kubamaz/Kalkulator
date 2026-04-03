package com.example.myapplication

import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import com.ezylang.evalex.Expression

@Composable
fun SimpleCalcScreen(navController: NavController, padding: PaddingValues) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var currentInput by rememberSaveable { mutableStateOf("") }
    var historyText by rememberSaveable { mutableStateOf("") }
    var lastCeClickTime by rememberSaveable { mutableLongStateOf(0L) }

    val handleButtonClick = remember {
        { label: String ->
            if (currentInput == "Błąd" && label != "AC") {
                currentInput = ""
            }
            when (label) {

                "AC" -> {
                    currentInput = ""
                    historyText = ""
                }
                "+", "-", "*", "/" -> {
                    if (currentInput.isNotEmpty() && historyText.isNotEmpty()) {
                        try {
                            val fullExpression = historyText + currentInput
                            val expression = Expression(fullExpression)
                            val result = expression.evaluate().numberValue
                            val roundedResult = result.setScale(8, java.math.RoundingMode.HALF_UP)
                            val intermediateResult = roundedResult.stripTrailingZeros().toPlainString()

                            historyText = "$intermediateResult $label "
                            currentInput = ""
                        } catch (_: Exception) {
                            currentInput = "Błąd"
                            historyText = ""
                        }
                    } else if (currentInput.isNotEmpty()) {
                        historyText = "$currentInput $label "
                        currentInput = ""
                    } else if (historyText.isNotEmpty()) {
                        historyText = historyText.dropLast(3) + " $label "
                    }
                }
                "+/-" -> {
                    if (currentInput.isEmpty()) {
                        currentInput = "-"
                    } else if (currentInput.startsWith("-")) {
                        currentInput = currentInput.removePrefix("-")
                    } else if (currentInput != "0") {
                        currentInput = "-$currentInput"
                    }
                }
                "C/CE" -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastCeClickTime < 300) {
                        currentInput = ""
                        lastCeClickTime = 0L
                    } else {
                        if (currentInput.isNotEmpty()) currentInput = currentInput.dropLast(1)
                        lastCeClickTime = currentTime
                    }
                }
                "=" -> {
                    if (currentInput.isNotEmpty() && historyText.isNotEmpty()) {
                        val fullExpression = historyText + currentInput
                        try {
                            val expression = Expression(fullExpression)
                            val result = expression.evaluate().numberValue
                            val roundedResult = result.setScale(8, java.math.RoundingMode.HALF_UP)
                            currentInput = roundedResult.stripTrailingZeros().toPlainString()
                            historyText = ""
                        } catch (_: Exception) {
                            currentInput = "Błąd"
                        }
                    }
                }
                "." -> if (!currentInput.contains(".")) currentInput += "."
                "0" -> if (currentInput != "0") currentInput += "0"
                "00" -> if (currentInput.isEmpty()) currentInput = "0" else if (currentInput != "0") currentInput += "00"
                else -> {
                    if (currentInput == "0") {
                        currentInput = label
                    } else {
                        currentInput += label
                    }
                }
//                else -> currentInput += label
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
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically

            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Powrót",
                        modifier = Modifier.size(32.dp)
                    )
                }
                ResultDisplay(currentInput, historyText, orientation = true)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.weight(2f)) {
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
                        modifier = Modifier.size(32.dp).padding(bottom = 8.dp)
                    )
                }
            }
            ResultDisplay(currentInput, historyText, modifier = Modifier.weight(1f), false)
            Spacer(modifier = Modifier.height(16.dp)) 
            Box(modifier = Modifier.weight(4f)) {
                CalculatorGrid(buttons, false, handleButtonClick)
            }
        }
    }

}
