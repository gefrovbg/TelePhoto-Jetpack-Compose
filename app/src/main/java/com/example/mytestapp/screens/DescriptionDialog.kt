package com.example.mytestapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytestapp.R
import com.example.mytestapp.screens.factory.DescriptionDialogViewModelFactory
import com.example.mytestapp.screens.viewmodel.DescriptionDialogViewModel
import com.example.mytestapp.ui.theme.MyTestAppTheme


@Composable
fun DescriptionDialog(
    viewModel: DescriptionDialogViewModel = viewModel(
        factory = DescriptionDialogViewModelFactory(
            LocalContext.current
        )
    ),
    isDialogOpen: MutableState<Boolean>
){
    MyTestAppTheme {
        Dialog(
            onDismissRequest = { isDialogOpen.value = true }
        ) {
            Surface(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(1f),
                shape = RoundedCornerShape(20.dp),
                color = colors.onSecondary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 20.dp),
                        style = typography.h5,
                        text = stringResource(R.string.description)
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(vertical = 10.dp, horizontal = 20.dp),
                        shape = RoundedCornerShape(20.dp),
                        onClick = {
                            if (viewModel.saveDescriptionToSharedPreferences(true)) isDialogOpen.value = false
                                  },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colors.onSecondary)
                    ) {
                        Text(
                            color = colors.primary,
                            text = "ACCEPT"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DescriptionDialogPreview() {
    DescriptionDialog(
        isDialogOpen =  remember {
        mutableStateOf<Boolean>(true)
    })
}