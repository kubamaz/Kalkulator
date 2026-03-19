package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultDisplay(result: String, history: String, modifier: Modifier = Modifier, orientation: Boolean) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = history,
                color = Color.Gray,
                fontSize = if (orientation) 10.sp else 24.sp,
                textAlign = TextAlign.End,
                maxLines = 1
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = result.ifEmpty { "0" },
                fontSize = if (orientation) 20.sp else 48.sp,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CalculatorGrid(buttons: List<String>, orientation: Boolean, onButtonClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (orientation) 5 else 4),
        verticalArrangement = Arrangement.spacedBy(if (orientation) 1.dp else 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        items(buttons) { label ->
            Button(
                onClick = {onButtonClick(label)},
                modifier = if (orientation) Modifier.fillMaxWidth() else Modifier.fillMaxWidth().aspectRatio(1f),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = label, fontSize = 20.sp)
            }
        }
    }
}