package com.example.hydrationapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hydrationapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navigateToSettings: () -> Unit = {},
    @DrawableRes settingsResource: Int? = null
) {
    Column {
        CenterAlignedTopAppBar(title = { Text(text = title) }, actions = {
            if (settingsResource != null) {
                IconButton(onClick = { navigateToSettings() }) {
                    Icon(
                        painter = painterResource(id = settingsResource),
                        contentDescription = stringResource(
                            id = R.string.settings
                        )
                    )
                }
            }
        })
        HorizontalDivider(color = colorResource(id = R.color.green), thickness = 2.dp)
    }
}


@Preview
@Composable
fun TopAppBarPreview() {
    AppTopBar(title = "Today's progress", {}, R.drawable.settings_android)
}