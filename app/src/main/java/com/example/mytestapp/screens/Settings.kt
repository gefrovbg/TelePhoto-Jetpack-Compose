package com.example.mytestapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytestapp.screens.factory.SettingsViewModelFactory
import com.example.mytestapp.screens.viewmodel.SettingsViewModel
import com.example.mytestapp.ui.theme.MyTestAppTheme
import com.example.telephoto.domain.models.Client

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(LocalContext.current, LocalLifecycleOwner.current)
    )
) {
    var boolean = remember {
            mutableStateOf(false)
    }
    boolean.value = viewModel.showDescriptionBoolean.value != true
    
    MyTestAppTheme{
        Column(modifier = Modifier
            .fillMaxSize()
            .background(colors.onSecondary)
        ) {
            if (boolean.value) DescriptionDialog(isDialogOpen = boolean)
            TokenInput(viewModel = viewModel)
            ListOfUsers(viewModel = viewModel)
        }
    }
}

@Composable
fun TokenInput(viewModel: SettingsViewModel){

    var text by remember { mutableStateOf(TextFieldValue(if (viewModel.tokenString.value != null) viewModel.tokenString.value!! else "")) }
    var isError by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(1f),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = colors.onPrimary
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp, 5.dp),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = typography.h5,
                    value = text,
                    onValueChange = {
                        text = it
                        isError = false
                    },
                    isError = isError,
                    label = { Text(text = "Token") },
                    placeholder = { Text(text = "Enter your token of bot") }
                )
                if (isError) {
                    Text(
                        text = "Insert token!",
                        color = colors.error,
                        style = typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp, 5.dp, 10.dp, 15.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = { if (text.text == ""){
                        isError = true
                    }else{
                        viewModel.saveToken(text.text)
                    } },
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.onSecondary)
                ) {
                    Text(

                        color = colors.primary,
                        text = "ACCEPT",
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ListOfUsers(viewModel: SettingsViewModel){
    Box(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(20.dp, 20.dp, 20.dp, 92.dp),
        contentAlignment = Alignment.Center
    ){
        Card(
            modifier = Modifier.fillMaxWidth(1f),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = colors.onPrimary
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    style = typography.h5,
                    text = "User`s List"
                )
                LazyColumn(
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.users.size){ i ->
                        UserItem(viewModel = viewModel, i = i)
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(viewModel: SettingsViewModel, i: Int){
    Card(
        modifier = Modifier.fillMaxWidth(1f),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = colors.onSecondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                Text(
                    modifier = Modifier
                        .padding(10.dp, 10.dp,10.dp),
                    style = typography.h5,
                    text = "${viewModel.users[i].firstName} ${viewModel.users[i].lastName}"
                )
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    style = typography.h6,
                    text = "${viewModel.users[i].nickname}"
                )
            }

            Button(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(15.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    if (viewModel.users[i].addStatus == true){
                        viewModel.users = viewModel.users.mapIndexed { j, user ->
                            if(i == j) {
                                viewModel.updateUser(Client(
                                    user.chatId,
                                    user.firstName,
                                    user.lastName,
                                    user.nickname,
                                    false
                                ))
                                user.copy(addStatus = !user.addStatus!!)
                            } else user
                        }
                    }else{
                        if (viewModel.deleteUser(viewModel.users[i])){
                            val _listUsers = viewModel.users.toMutableList()
                            _listUsers.removeAt(i)
                            viewModel.users = _listUsers.toList()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = colors.onSecondary)
            ) {
                if (viewModel.users[i].addStatus == true) {
                    Text(
                        color = colors.primary,
                        text = "ADD",
                        fontSize = 10.sp
                    )
                }else {
                    Text(
                        color = colors.primary,
                        text = "REMOVE",
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    SettingsScreen()
}