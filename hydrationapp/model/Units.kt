package com.example.hydrationapp.model

import androidx.annotation.StringRes
import com.example.hydrationapp.R
import com.example.hydrationapp.model.Units.MILLILITERS

enum class Units(
    val key: String,
    @StringRes
    val abbreviation: Int
) {
    MILLILITERS("ml", R.string.milliliters_abbreviation), OUNCES(
        "oz",
        R.string.ounces_abbreviation
    );
}

fun getByKey(key: String): Units {
    return Units.entries.find { it.key == key } ?: MILLILITERS
}