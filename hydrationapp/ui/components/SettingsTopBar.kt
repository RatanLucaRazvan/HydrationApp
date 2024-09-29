package com.example.hydrationapp.ui.components

import androidx.annotation.StringRes
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
import androidx.compose.ui.unit.dp
import com.example.hydrationapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    @StringRes title: Int,
    navigateBack: () -> Unit
) {
    Column {

        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = title)) },
            navigationIcon = {
                IconButton(onClick = { navigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_android),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            })
        HorizontalDivider(color = colorResource(id = R.color.green), thickness = 2.dp)
    }
}