package com.example.myapplication

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.ui.res.stringResource
import com.ezylang.evalex.Expression

@Composable
fun SimpleCalcScreen(navController: NavController, padding: PaddingValues) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var currentInput by rememberSaveable { mutableStateOf("") }
    var historyText by rememberSaveable { mutableStateOf("") }
    var lastCeClickTime by rememberSaveable { mutableLongStateOf(0L) }
    var isShowingResult by rememberSaveable { mutableStateOf(false) }
    val errorText = stringResource(id = R.string.error)

    val newHandleButtonClick = remember {
        { label: String ->
            if (currentInput == errorText && label != "AC") {
                currentInput = ""
                historyText = ""
            }
            when (label) {
                "AC" -> {
                    currentInput = ""
                    historyText = ""
                    isShowingResult = false
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
                    isShowingResult = false
                }
                "+", "-", "*", "/" -> {
                    isShowingResult = false
                    if (currentInput.isNotEmpty()) {
                        val endsWithOperator = currentInput.endsWith(" + ") ||
                                currentInput.endsWith(" - ") ||
                                currentInput.endsWith(" * ") ||
                                currentInput.endsWith(" / ")
                        if (currentInput.endsWith("(")) {
                            if (label == "-") {
                                currentInput += "-"
                            }
                        }else if (endsWithOperator) {
                            currentInput = currentInput.dropLast(3) + " $label "
                        }
                        else {
                            currentInput += " $label "
                        }
                    }
                }
                "√" -> {
                    isShowingResult = false
                    if (currentInput == "0") {
                        currentInput = "√("
                    } else {
                        currentInput += "√("
                    }
                }
                "%" -> {
                    if (currentInput.isNotEmpty() && !currentInput.endsWith(" " ) && !currentInput.endsWith("(")) {
                        try {
                            val parts = currentInput.split(" ")
                            val lastPart = parts.last()
                            val b = lastPart.toBigDecimal()
                            var replacementValue = ""

                            if (parts.size >= 3) {
                                val operator = parts[parts.size - 2]

                                if (operator == "+" || operator == "-") {
                                    try {
                                        val leftSideExpr = parts.dropLast(2).joinToString(" ").replace("√", "SQRT")
                                        val a = Expression(leftSideExpr).evaluate().numberValue

                                        val percentOfA = a.multiply(b).divide(java.math.BigDecimal("100"))
                                        replacementValue = percentOfA.stripTrailingZeros().toPlainString()
                                    } catch (e: Exception) {
                                        replacementValue = b.divide(java.math.BigDecimal("100")).stripTrailingZeros().toPlainString()
                                    }
                                } else {
                                    replacementValue = b.divide(java.math.BigDecimal("100")).stripTrailingZeros().toPlainString()
                                }
                            } else {
                                replacementValue = b.divide(java.math.BigDecimal("100")).stripTrailingZeros().toPlainString()
                            }

                            currentInput = currentInput.dropLast(lastPart.length) + replacementValue
                            isShowingResult = false

                        } catch (_: Exception) {
                        }
                    }
                }
                "=" -> {
                    if (currentInput.isNotEmpty() && !isShowingResult) {
                        try {
                            val evalText = currentInput.replace("√", "SQRT")
                            val result = Expression(evalText).evaluate().numberValue
                            val roundedResult = result.setScale(12, java.math.RoundingMode.HALF_UP)
                            historyText = "$currentInput = "
                            currentInput = roundedResult.stripTrailingZeros().toPlainString()
                            isShowingResult = true
                        } catch (_: Exception) {
                            currentInput = errorText
                        }
                    }
                }
                "+/-" -> {
                    isShowingResult = false
                    if (currentInput.isEmpty()) {
                        currentInput = "-"
                    } else if (currentInput.startsWith("-")) {
                        currentInput = currentInput.removePrefix("-")
                    } else if (currentInput != "0") {
                        currentInput = "-$currentInput"
                    }
                }
                "." -> {
                    val lastPart = currentInput.split(" ").last()
                    if (!lastPart.contains(".")) {
                        currentInput += if (currentInput.isEmpty() || currentInput.endsWith(" ")) "0." else "."
                        isShowingResult = false
                    }
                }
                "0" -> if (currentInput != "0") {
                    currentInput += "0"
                    isShowingResult = false
                }
                "00" -> {
                    if (currentInput.isEmpty()) currentInput =
                        "0" else if (currentInput != "0") currentInput += "00"
                    isShowingResult = false
                }
                "(", ")" -> {
                    isShowingResult = false
                    if (currentInput == "0") {
                        currentInput = label
                    }
                    else {
                        currentInput += label

                    }
                }
                else -> {
                    if (isShowingResult) {
                        currentInput = label
                        historyText = ""
                        isShowingResult = false
                    }
                    else {
                        if (currentInput == "0") {
                            currentInput = label
                        } else {
                            currentInput += label
                        }
                    }

                }
            }
            if (!isShowingResult && currentInput.isNotEmpty()) {
                try {
                    val cleanInput = if (currentInput.endsWith(" ")) currentInput.dropLast(3) else currentInput

                    if (cleanInput.any { it in "+-*/√" }) {
                        val evalText = cleanInput.replace("√", "SQRT")
                        val previewResult = Expression(evalText).evaluate().numberValue
                        val rounded = previewResult.setScale(12, java.math.RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
                        historyText = rounded
                    } else {
                        historyText = ""
                    }
                } catch (_: Exception) {
                    historyText = ""
                }
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
            val buttons = listOf(
                "AC", "C/CE", "7", "8", "9", "/",
                "(", ")", "4", "5", "6", "*",
                "√", "%", "1", "2", "3", "-",
                "+/-", "00", "0", ".", "=", "+"
            )
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically

            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.Back),
                        modifier = Modifier.size(32.dp)
                    )
                }
                ResultDisplay(currentInput, historyText, orientation = true)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.weight(2f)) {
                CalculatorGrid(buttons, columns = 6, onButtonClick = newHandleButtonClick)
            }
        } else {
            val buttons = listOf(
                "AC", "C/CE", "(", ")",
                "√", "%", "+/-", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "00", "0", ".", "="
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.Back),
                        modifier = Modifier.size(32.dp).padding(bottom = 8.dp)
                    )
                }
            }
            ResultDisplay(currentInput, historyText, modifier = Modifier.weight(1f), false)
            Spacer(modifier = Modifier.height(16.dp)) 
            Box(modifier = Modifier.weight(4f)) {
                CalculatorGrid(buttons, columns= 4, newHandleButtonClick)
            }
        }
    }

}
