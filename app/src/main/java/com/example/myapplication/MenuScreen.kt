package com.example.myapplication

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController : NavController, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            modifier =
                Modifier.padding(bottom = 20.dp)
        )
        //Simple Calc
        Button(
            onClick = {
                navController.navigate("simple")
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .widthIn(max = 400.dp)
                .padding(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.simple),
                fontSize = 18.sp

            )
        }
        //advance calc
        Button(
            onClick = {
                navController.navigate("advance")
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .widthIn(max = 400.dp)
                .padding(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.advance),
                fontSize = 18.sp
            )
        }
        //about us
        Button(
            onClick = {
                navController.navigate("about")
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .widthIn(max = 400.dp)
                .padding(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                stringResource(id = R.string.about),
                fontSize = 18.sp
            )
        }
        //exit
        val context = LocalContext.current
        Button(
            onClick = {
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .widthIn(max = 400.dp)
                .padding(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                stringResource(id = R.string.exit),
                fontSize = 18.sp
            )
        }
    }
}