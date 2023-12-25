package com.suitmediatest1.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.suitmediatest1.data.model.UserModel
import com.suitmediatest1.ui.navigation.Screen
import androidx.paging.compose.itemKey
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ScreenViewModel,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.getUser()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            viewModel.uiState.collectAsState().value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                        ) {
                            IconButton(
                                onClick = { navController.navigate(Screen.SecondScreen.route) },
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                            }
                            Text(
                                text = "Third Screen",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .align(Alignment.TopCenter)
                            )
                            LoadingIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    is UiState.Success -> {
                        Log.i("ThirdScreen", "Success")
                        ThirdScreenContent(
                            viewModel = viewModel,
                            navController = navController,
                            pager = uiState.data
                        ) { name ->
                            viewModel.setSelectedUser(name)
                            scope.launch {
                                snackbarHostState.showSnackbar("$name Clicked!")
                            }
                        }
                    }
                    is UiState.Error -> {
                        Log.i("ThirdScreen", "Error")
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                        ) {
                            IconButton(
                                onClick = { navController.navigate(Screen.SecondScreen.route) },
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                            }
                            Text(
                                text = "Third Screen",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .align(Alignment.TopCenter)
                            )
                            Text(
                                text = uiState.errorMessage,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp)
    )
}

@Composable
fun ThirdScreenContent(
    viewModel: ScreenViewModel,
    navController: NavHostController,
    pager: List<UserModel>,
    onItemSelected: (String) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate(Screen.SecondScreen.route) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(
                text = "Third Screen",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.getUser()
            }
        ) {
            LazyColumn {
                items(pager) { user ->
                    UserItem(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        photoUrl = user.avatar,
                        onItemSelected = { name ->
                            onItemSelected(name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserItem(
    firstName: String,
    lastName: String,
    email: String,
    photoUrl: String,
    onItemSelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onItemSelected("$firstName $lastName") }
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(16.dp)
                .size(62.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "$firstName $lastName",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(text = email)
        }
    }
}