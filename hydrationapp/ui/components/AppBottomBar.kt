package com.example.hydrationapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hydrationapp.R
import com.example.hydrationapp.ui.theme.RobotoType


@Composable
fun AppBottomBar(
    fromHistory: Boolean,
    fromHome: Boolean,
    navigateToHistory: () -> Unit,
    navigateToHome: () -> Unit
) {

    BottomAppBar(containerColor = colorResource(id = R.color.black)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    navigateToHome()
                },
                modifier = Modifier.weight(0.5f, true),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.today),
                        contentDescription = stringResource(
                            id = R.string.today
                        ),
                        tint = if (fromHome) colorResource(id = R.color.green) else colorResource(
                            id = R.color.white
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.today),
                        color = if (fromHome) colorResource(id = R.color.green) else colorResource(
                            id = R.color.white
                        ),
                        style = RobotoType.Navigation
                    )
                }
            }
            IconButton(onClick = {
                navigateToHistory()
            }, modifier = Modifier.weight(0.5f, true)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.history_android),
                        contentDescription = stringResource(
                            id = R.string.history
                        ),
                        tint = if (fromHistory) colorResource(id = R.color.green) else colorResource(
                            id = R.color.white
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.history),
                        color = if (fromHistory) colorResource(id = R.color.green) else colorResource(
                            id = R.color.white
                        ),
                        style = RobotoType.Navigation
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AppBottomBarPreview() {
    AppBottomBar(true, false, {}, {})
}