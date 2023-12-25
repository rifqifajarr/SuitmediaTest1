package com.suitmediatest1.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.suitmediatest1.R
import com.suitmediatest1.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToSecondScreen: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var palindrome by remember { mutableStateOf("") }
    var isPalindrome by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    when {
        openDialog -> {
            showDialog(
                onDismissRequest = { openDialog = false },
                text = if (isPalindrome) "isPalindrome" else "not palindrome"
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_gradient),
            contentDescription = null,modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.FillBounds,
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_photo), contentDescription = null)

            Spacer(modifier = Modifier.height(64.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = palindrome,
                onValueChange = { palindrome = it },
                label = { Text("Palindrome") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                isPalindrome = checkPalindrome(palindrome)
                openDialog = true },
                modifier = Modifier
                    .fillMaxWidth(),
                // button hanya bisa ditekan ketika palindrome nya tidak kosong
                enabled = palindrome.isNotEmpty()
            ) {
                Text(text = "CHECK")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // navigasi ke second screen
                    navigateToSecondScreen(name)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                // button hanya bisa ditekan ketika name nya tidak kosong
                enabled = name.isNotEmpty()
            ) {
                Text(text = "NEXT")
            }
        }
    }
}

fun checkPalindrome(text: String): Boolean {
    // hapus spasi dari text
    val textWithoutSpace = text.replace(" ", "")
    // bandingkan dengan text terbalik
    return textWithoutSpace == textWithoutSpace.reversed()
}

@Composable
fun showDialog(onDismissRequest: () -> Unit, text: String) {

    // fungsi untuk menampilkan dialog, menerina parameter text sebagai String untuk kemudian ditampilkan

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
            )
        }
    }
}