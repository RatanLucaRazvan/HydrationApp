package com.example.hydrationapp.utils

import com.example.hydrationapp.model.Units
import kotlin.math.roundToInt

class Formatter {
    fun calculatePercentage(drunkQuantity: Int, goal: Int): Int {
        return (drunkQuantity.toFloat() / goal.toFloat() * 100).toInt()
    }


    fun convertToOuncesIfNecessary(value: Int, units: Units): Int {
        if (units == Units.OUNCES) {
            return (value.toFloat() / 29.574f).roundToInt()
        } else {
            return value
        }
    }

    fun convertToMillilitersIfNecessary(value: Int, units: Units): Int {
        if (units == Units.OUNCES) {
            return (value.toFloat() * 29.574f).roundToInt()
        } else {
            return value
        }
    }

}