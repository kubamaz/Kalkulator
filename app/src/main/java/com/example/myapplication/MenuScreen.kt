package com.example.myapplication

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController : NavController, padding: PaddingValues) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = if (isLandscape) 36.sp else 50.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isLandscape) 2 else 1),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .widthIn(max = 600.dp)
                .padding(horizontal = 16.dp),
        ) {
            val btnModifier = Modifier.fillMaxWidth().height(60.dp)
            item {
                Button(onClick = { navController.navigate("simple") }, modifier = btnModifier) {
                    Text(text = stringResource(id = R.string.simple), fontSize = 18.sp)
                }
            }
            item {
                Button(onClick = { navController.navigate("advance") }, modifier = btnModifier) {
                    Text(text = stringResource(id = R.string.advance), fontSize = 18.sp)
                }
            }
            item {
                Button(onClick = { navController.navigate("about") }, modifier = btnModifier) {
                    Text(text = stringResource(id = R.string.about), fontSize = 18.sp)
                }
            }
            item {
                Button(onClick = {(context as? Activity)?.finish() }, modifier = btnModifier) {
                    Text(text = stringResource(id = R.string.exit), fontSize = 18.sp)
                }
            }
        }
    }
}