package com.example.checkall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checkall.ui.theme.CheckAllTheme
import java.util.ArrayList

val tmpArray = ArrayList<Int>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckAllTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Column {
                MainScreen(navController)
            }
        }
        composable("check/{first}") {
            Column {
                CheckScreen(navController)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Column {
        Button(onClick = { navController.navigate("check/true") }) {
            Text(text = "이동")
        }
    }
}

@Composable
fun CheckScreen(navController: NavHostController) {
    val list = listOf(1, 2, 3, 4, 5)
    val checkAll = remember { mutableStateOf(CheckState.checkAll) }
    CheckSection(navController, eachList = list, checkAll = checkAll)
}

@Composable
fun CheckSection(navController: NavHostController, eachList: List<Int>, checkAll: MutableState<Boolean>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            tmpArray.forEach {
                if(!CheckState.checkValue.contains(it)) CheckState.checkValue.add(it)
            }
            navController.popBackStack() }) {
            Text(text = "저장하고 이전으로")
        }
        Button(onClick = {
            tmpArray.clear()
            navController.popBackStack() }) {
            Text(text = "저장 안하고 이전으로")
        }
        Row(
            Modifier.clickable {
                checkAll.value = !checkAll.value
                eachList.forEach { item ->
                    if(checkAll.value) {
                        if(!tmpArray.contains(item)) tmpArray.add(item)
                    } else {
                        if(tmpArray.contains(item)) tmpArray.remove(item)
                    }
                }
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = if (checkAll.value) ImageVector.vectorResource(id = R.drawable.ic_check)
                else ImageVector.vectorResource(id = R.drawable.ic_uncheck),
                contentDescription = null
            )
            Text(text = stringResource(id = R.string.check_all))
        }

        Divider(Modifier.border(1.dp, Color.Black))
        eachList.forEach { item ->
            var isChecked by remember { mutableStateOf(tmpArray.contains(item)) }
            LaunchedEffect(checkAll.value) {
                isChecked = tmpArray.contains(item)
            }
            Row(
                Modifier.clickable {
                    isChecked = !isChecked
                    if (isChecked) {
                        if(!tmpArray.contains(item)) tmpArray.add(item)
                    } else {
                        if(tmpArray.contains(item)) tmpArray.remove(item)
                    }
                    checkAll.value = tmpArray.size == eachList.size
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = if (isChecked) ImageVector.vectorResource(id = R.drawable.ic_check)
                    else ImageVector.vectorResource(id = R.drawable.ic_uncheck),
                    contentDescription = null
                )
                Text(text = item.toString())
            }
        }
        CheckState.checkAll= tmpArray.containsAll(eachList)
    }
}