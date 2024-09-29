package com.example.hydrationapp.model

import androidx.annotation.StringRes
import com.example.hydrationapp.R

enum class EditScreenType(
    @StringRes
    val titleRes: Int,
    @StringRes
    val descriptionRes: Int
) {
    GOAL_SCREEN(R.string.daily_goal, R.string.update_goal_description),
    CONTAINER_ONE_SCREEN(R.string.container_one, R.string.update_container_description),
    CONTAINER_TWO_SCREEN(R.string.container_two, R.string.update_container_description),
    CONTAINER_THREE_SCREEN(R.string.container_three, R.string.update_container_description)
}